# Choice of Authentication Provider

* Status: proposed
* Deciders: Daniel Grant <daniel.grant@digirati.com>, Gary Tierney
  <gary.tierney@digirati.com>, Stephen Fraser
  <stephen.fraser@digirati.com>
* Date: 2021-02-15

## Context and Problem Statement

In order to deliver BFI's IIIF Universal Viewer auditing platform, an
identity and access management solution must be provisioned which
supports user creation and registration, user management, and
authentication and authorisation.

## Decision Drivers

* Ease of initial deployment and configuration of the solution.
* Ongoing hosting and maintenance costs of the solution.
* Availability of core features to satisfy the project requirements with
  no / minimal costs.

## Considered Options

* Auth0 (https://auth0.com/)
* Keycloak (https://www.keycloak.org/)

## Decision Outcome

Auth0 is selected as the solution, due to its managed nature requiring
zero deployment and effort. After an evaluation of features compared
against the project requirements, it has been determined that Auth0's
free tier will suffice and as a result there are no ongoing costs
either.

### Positive Consequences

* As a managed solution, there are no initial deployment or ongoing
  hosting / infrastructure costs.
* Patching and maintenance of the solution is provided by Auth0 at no
  additional cost / interruption to the customer.
* It is expected that (at least initially) the free tier will suffice to
  meet the requirements of the project.

### Negative Consequences

* The features and customisations available are limited without
  upgrading to a paid tier:
  * Email templates cannot be customised on the free plan.
  * A custom domain cannot be used to serve the Auth0 tenant.
  * Enterprise integrations (AD, LDAP, etc) are not available.
  * Multifactor authentication is not available.
* Auth0 hosted UI's can be customised but only to a limited extent.
* Limit of 7000 monthly active users.

## Pros and Cons of the Options

### Auth0

* Good, because it offers a free tier with basic features, with the
  option to upgrade / downgrade over time.
* Good, because it's easy and fast to get started with.
* Good, because it has no deployment and ongoing hosting /
  infrastructure costs.
* Good, because patches and maintenance are provided by Auth0 at no cost
  / interruption to the customer.
* Good, because it can be configured / bootstrapped using Infrastructure
  as Code (i.e., Terraform).
* Bad, because the ability to customise the product are limited by both
  the subscription tier and by the product in general.
* Bad, because only community support is provided in the free tier with
  varying SLA's in the paid tiers.

### Keycloak

* Good, because it's free and open source and thus has no ongoing
  licensing or subscription costs.
* Good, because it contains a full suite of advanced and sophisticated
  features.
* Good, because it can be tailored and customised to meet virtually any
  use case / requirements.
* Good, because it can be configured / bootstrapped using Infrastructure
  as Code (i.e., Terraform).
* Bad, because it requires deployment and ongoing hosting /
  infrastructure costs.
* Bad, because it requires regular patching and upgrading for security
  and to stay within a supported version.
* Bad, because support requires procurements of an expensive license
  from Red Hat, Inc.
