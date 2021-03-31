# BFI - IIIF Universal Viewer Logging Platform

## Introduction

This project contains an implementation of a Kotlin server, which logs
interactions users make with BFI's IIIF resources into a MySQL database
for long term auditing and analytics.

Specifically, the project references as a Git Submodule BFI's fork of
the
[Universal Viewer](https://github.com/bfidatadigipres/bfi_universal_viewer),
which itself contains the necessary customisations to call the servers
`/api/event` endpoint on certain events. The Universal Viewer is then
available via the root resource path `/` on the running server.

To track individual users, the Kotlin server integrates with
[Auth0](https://auth0.com/) via OAuth 2.0 / OpenID and requires users to
authenticate before they are able to access the Universal Viewer.

The project is built using
[GitHub Actions](https://github.com/bfidatadigipres/bfi-iiif-load-balancer/actions),
and the produced containers are persisted in
[GitHub Container Registry](https://github.com/orgs/bfidatadigipres/packages/container/package/bfi-iiif-load-balancer).

## Getting Started

### Repository Layout

The repository is split into the following components:

- [`Dockerfile`](Dockerfile) and [`docker/`](docker/)
  - This image contains the compiled Kotlin server application, bundled
    with the Universal Viewer modifications pulled from the forked
    GitHub repository. It exposes port `8080` for HTTP / HTTPS access.
  - A running instance of the container requires a number of environment
    variables to be provided, containing the necessary configuration.
- [`infra/`](infra/)
  - Contains Terraform which drives the configuration of the Auth0
    tenants.
- [`src/`](src/)
  - Contains the Kotlin source code and associated resources, plus
    testing configurations.
- [`ssl/`](ssl/)
  - Contains SSL and TLS related assets, including the Java KeyStore
    containing the required key and certificates for the Kotlin servers
    SSL configuration, and the required passphrases to access both the
    KeyStore and the key contained within it.
- [`deploy`](deploy/)
  - Contains the folder structure and configuration files required to
    deploy the logging platform. Specifically:
    - [`/etc/opt/bfi/iiif-logging/<environment>/`](deploy/etc/opt/bfi/iiif-logging/<environment>/):
      contains configuration files and assets used by the logging
      platform.
    - [`/etc/systemd/system/<environment>-iiif-logging.service`](deploy/etc/systemd/system/iiif-load-balancer.service):
      the systemd unit used for starting and stopping the underlying
      Kotlin server.
    - [`/opt/bfi/iiif-logging/<environment>/docker-compose.yml`](deploy/opt/bfi/iiif-logging/<environment>/docker-compose.yml):
      the Docker Compose manifest, defining the logging platform
      application, dependencies and relationships therein.
    - [`/var/opt/bfi/iiif-logging/<environment>/mounts`](deploy/var/opt/bfi/iiif-logging/<environment>/mounts):
      directory where the various mounted volumes used by the containers
      are located.

### Secrets Management

All secrets checked in as part of the repository have been encrypted
using [git-secret.io](https://git-secret.io/). This includes the
contents of the
[`ssl`](https://github.com/bfidatadigipres/bfi-iiif-logging/blob/master/ssl)
directory, which contains various SSL / TLS related certificates and
keys.

Existing users who already exist in the keyring can decrypt secrets with
the following command:

```bash
git secret reveal
```

To decrypt secrets, you must first be added to the keyring by an
existing user (assuming a key for `some.user@example.com` already exists
in your local GPG keyring):

```bash
git secret reveal
git secret tell some.user@example.com
git secret hide
```

New secrets can be added with the following commands:

```bash
git secret add path/to/my/secret.txt
git secret hide
```

## Building

[![build](https://github.com/bfidatadigipres/bfi-iiif-logging/actions/workflows/build.yml/badge.svg)](https://github.com/bfidatadigipres/bfi-iiif-logging/actions/workflows/build.yml)

The project uses Kotlin and is built using Gradle:

```bash
./gradlew clean build
```

The path [`src/main/resources/static`](src/main/resources/static) is a
Git Submodule, pointing to a repository containing a configuration of
the Universal Viewer (i.e. an `index.html` and associated
`uv-config.json`, `manifests.json`, etc). During the build process, the
Universal Viewer bundle is downloaded and unpacked into this directory.

A `Dockerfile` is provided which builds and exposes the application on
port `8080`.

A `docker-compose.dev.yml` manifest is provided for running the
application on a local development machine. The manifest will build,
run, and expose the application:

```bash
docker-compose --file docker-compose.dev.yml up --build --remove-orphans
```

Note that the application requires a number of environment variables,
which are provided through both the Docker Compose manifest and directly
/ through environment configuration files:

| Environment Variable          | Description                                                                                                                       |
|:------------------------------|:----------------------------------------------------------------------------------------------------------------------------------|
| `AUTH0_DOMAIN`                | The Auth0 domain for Auth0 authentication integration. Usually `<TENANT_NAME>.<REGION>.auth0.com`, or a configured custom domain. |
| `AUTH0_CLIENT_ID`             | The ID of the Auth0 client to use for authentication.                                                                             |
| `AUTH0_CLIENT_SECRET_FILE`    | A text file containing the secret for the Auth0 client to use for authentication.                                                 |
| `SSL_ENABLED`                 | Whether to enable SSL on the HTTP endpoint, `true` or `false`.                                                                    |
| `SSL_KEY_STORE`               | The Java KeyStore containing the key and certificate for SSL. Required if `SSL_ENABLED` is `true`.                                |
| `SSL_KEY_STORE_PASSWORD_FILE` | The password to the Java KeyStore containing the key and certificate for SSL. Required if `SSL_ENABLED` is `true`.                |
| `SSL_KEY_ALIAS`               | The alias of the key and certificate in the Java Keystore for SSL. Required if `SSL_ENABLED` is `true`.                           |
| `SSL_KEY_PASSWORD_FILE`       | The password of the key and certificate in the Java KeyStore for SSL. Required if `SSL_ENABLED` is `true`.                        |
| `MYSQL_DATABASE`              | The name of the MySQL database, where audit log events are stored.                                                                |
| `MYSQL_HOST`                  | The hostname of the MySQL database, where audit log events are stored.                                                            |
| `MYSQL_USERNAME`              | The username of the MySQL database, where audit log events are stored.                                                            |
| `MYSQL_PASSWORD_FILE`         | A text file containing the password of the MySQL database, where audit log events are stored.                                     |

## Deployment

### Prerequisites

The application requires Docker and Docker Compose. It is recommended
that these are installed from the official Docker repositories:

- https://docs.docker.com/engine/install/
- https://docs.docker.com/compose/install/

The application deployment should mirror the contents of the
[`deploy/`](https://github.com/bfidatadigipres/bfi-iiif-logging/blob/master/deploy)
directory. Start by creating the necessary directories:

### Deploy Configuration

Deployments are scoped to a specific environment, e.g. `DEV`, `UAT`,
`PROD` etc. The environment is defined in both the paths to the
installation configuration (i.e. `<environment>`) and in the
`config.env` configuration file.

```bash
sudo -i
mkdir -p /etc/opt/bfi/iiif-logging/<environment>/secrets
mkdir -p /etc/opt/bfi/iiif-logging/<environment>/ssl
mkdir -p /opt/bfi/iiif-logging/<environment>
mkdir -p /var/opt/bfi/iiif-logging/<environment>/mounts
chmod 775 /var/opt/bfi/iiif-logging/<environment>/mounts
```

Create the MySQL database passwords:

```bash
cat /dev/urandom | tr -dc '_A-Z-a-z-0-9' | head -c${1:-32} > /etc/opt/bfi/iiif-logging/<environment>/secrets/mysql_password
cat /dev/urandom | tr -dc '_A-Z-a-z-0-9' | head -c${1:-32} > /etc/opt/bfi/iiif-logging/<environment>/secrets/mysql_root_password
```

Deploy the Java KeyStore and associated passphrase files:

```bash
cp bk-ci-data4.dpi.bfi.org.uk.p12 /etc/opt/bfi/iiif-logging/<environment>/ssl/ssl_key_store
cp bk-ci-data4.dpi.bfi.org.uk-key_password /etc/opt/bfi/iiif-logging/<environment>/secrets/ssl_key_password
cp bk-ci-data4.dpi.bfi.org.uk-key_store_password /etc/opt/bfi/iiif-logging/<environment>/secrets/ssl_key_store_password
```

Create the Auth0 client secret file:

```bash
echo '<AUTH0_CLIENT_SECRET>' > /etc/opt/bfi/iiif-logging/<environment>/secrets/auth0_client_secret
```

Update
[`/etc/opt/bfi/iiif-logging/<environment>/config.env`](deploy/etc/opt/bfi/iiif-logging/<environment>/config.env)
to set the desired configuration:

```text
ENVIRONMENT=prod
LOGGING_IMAGE_TAG=1.0.0
LOGGING_HOSTNAME=<LOGGING_HOSTNAME>
LOGGING_PORT=49001
AUTH0_DOMAIN=<AUTH0_DOMAIN>
AUTH0_CLIENT_ID=<AUTH0_CLIENT_ID>
SSL_KEY_ALIAS=bk-ci-data4.dpi.bfi.org.uk
MYSQL_IMAGE_TAG=8.0.23
MYSQL_PORT=49011
```

Add the Docker Compose manifest:

```bash
cp docker-compose.yml /opt/bfi/iiif-logging/<environment>
```

Add the systemd unit:

```bash
cp <environment>-iiif-logging.service /etc/systemd/system
```

### Start Load Balancer

Enable the systemd unit to start at boot:

```bash
systemctl enable <environment>-iiif-logging
```

Start the load balancer:

```bash
systemctl start <environment>-iiif-logging
```

The Kotlin server can now be accessed on port `49001`.

## Contributors

[![contributors](https://contrib.rocks/image?repo=bfidatadigipres/bfi-iiif-logging)](https://github.com/bfidatadigipres/bfi-iiif-logging/graphs/contributors)

## Versioning

We use SemVer for versioning. For the versions available, see the [tags
on this repository](https://github.com/bfidatadigipres/bfi-iiif-logging/tags).

## License

This project is licensed under the MIT license - see the
[`LICENSE`](LICENSE) file for details.
