# BFI - IIIF Universal Viewer Logging Platform

## Introduction

This project contains an implementation of the
[Universal Viewer](https://universalviewer.io/) served by a Kotlin
backend, which logs interactions with the Universal Viewer (and thus,
the underlying IIIF manifests and images) in a MySQL database for long
term auditing and analytics.

This project is delivered as part of the British Film Institute's wider
Data and Digital Preservation platform.

The project is built using
[GitHub Actions](https://github.com/bfidatadigipres/bfi-iiif-load-balancer/actions),
and the produced containers are persisted in
[GitHub Container Registry](https://github.com/orgs/bfidatadigipres/packages/container/package/bfi-iiif-load-balancer).

## Build

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

## Deployment

### Installation

The [`deploy/`](deploy/) directory contains a directory hierarchy and
configuration files required for a deployment to a Linux environment,
scoped to a specific environment (e.g. `DEV`, `UAT`, `PROD`, etc):

- `/etc/`
  - `/opt/bfi/iiif-logging/<environment>/`
    - `secrets/`
      - Contains the secrets required to run the application,
        specifically the MySQL password and root password, Java KeyStore
        passwords and the Auth0 client secret. Note that these are
        delivered as text files, so that they can be provided as secrets
        to the Docker Compose manifests.
    - `ssl/`
      - Contains any SSL related configuration, specifically the Java
        KeyStore containing the key and certificate for serving the
        application via SSL.
    - `config.env`
      - A newline separated list of non-secret environment variables,
        which is provided as an `--env-file` parameter to the
        `docker-compose` start and stop operations.
  - `/systemd/system/<environment>-iiif-logging.service`
    - A systemd unit used for starting and stopping the application.
      Note that this file must be edited to set the `<environment>`
      placeholder.
- `/opt/bfi/iiif-logging/<environment>/docker-compose.yml`
  - The Docker Compose manifest which carries out the container
    orchestration. This also includes the required MySQL instance.
- `/var/opt/bfi/iiif-logging/<environment>/mounts`
  - The directory under which any Docker mounts are created. This is
    used for persisting the MySQL data directory between restarts of the
    application.

### Configuration

The application (and thus the built Docker image) requires a number of
environment variables:

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

### Starting & Stopping

Once the requisite installation files from the `deploy/` directory are
in place, the application can be started and stopped using the given
systemd unit:

```bash
systemctl start <environment>-iiif-logging
systemctl stop <environment>-iiif-logging
```

Enable the systemd unit to have the application start at system boot:

```bash
systemctl enable <environment>-iiif-logging
```

## Contributors

[![contributors](https://contrib.rocks/image?repo=bfidatadigipres/bfi-iiif-logging)](https://github.com/bfidatadigipres/bfi-iiif-logging/graphs/contributors)

## Versioning

We use SemVer for versioning. For the versions available, see the [tags
on this repository](https://github.com/bfidatadigipres/bfi-iiif-logging/tags).

## License

This project is licensed under the MIT license - see the
[`LICENSE`](LICENSE) file for details.
