# Choice of Authentication Provider

* Status: proposed
* Deciders: Daniel Grant <daniel.grant@digirati.com>, Gary Tierney
  <gary.tierney@digirati.com>, Stephen Fraser
  <stephen.fraser@digirati.com>
* Date: 2021-02-15

## Context and Problem Statement

In order to deliver BFI's IIIF's Universal Viewer auditing platform, a
user database which supports standards-based authentication and user
management functionalities must be deployed.

## Decision Drivers

* Ease of deployment and maintenance of the solution with minimal
  ongoing costs.
* Availability of features to satisfy the project requirements for the
  free tier.

## Considered Options

* Auth0
* Keycloak

## Decision Outcome

Auth0 is selected as the solution, due to its managed nature requiring
zero deployment and effort. After an evaluation of features compared
against the project requirements, it has been determined that Auth0's
free tier will suffice and as a result there are no ongoing costs
either.

### Positive Consequences

* No deployment, hosting or maintenance costs.
* Support is available via paid tiers.
* Ease of configuration of tenant via Terraform provider.

### Negative Consequences

* Limited feature set available without upgrading to paid tiers.
* UI customisations for hosted interfaces are limited.
* Email templates cannot be customised on the free plan.
* Limit of 1000 monthly active users.
