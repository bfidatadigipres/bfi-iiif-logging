# Choice of RDBMS for User Event storage

* Status: proposed
* Deciders: Daniel Grant <daniel.grant@digirati.com>, Gary Tierney <gary.tierney@digirati.com>, Stephen Fraser <stephen.fraser@digirati.com>
* Date: 2021-02-15

## Context and Problem Statement

The BFI requires that each event that happens on the frontend of their Universal Viewer deployment is logged and stored for long-term use.
The

## Decision Drivers

* There is familiararity at BFI with maintaining and managing the chosen RDBMS implementation.
* Ease of use, rapid development at Digirati.
* Open source, readily available, and well supported.

## Considered Options

* PostgreSQL
* MySQL

<!-- ## Decision Outcome

Chosen option: "[option 1]", because [justification. e.g., only option, which meets k.o. criterion decision driver | which resolves force force | … | comes out best (see below)].

### Positive Consequences

* [e.g., improvement of quality attribute satisfaction, follow-up decisions required, …]
* …

### Negative Consequences

* [e.g., compromising quality attribute, follow-up decisions required, …]
* … -->

## Pros and Cons of the Options

### PostgreSQL

PostgreSQL is a community driven RDBMS implementation.
It is an industry leader in the open-source RBDMS field and for most use cases will easily stand alongside the likes of Oracle and Microsofts RDBMS implementations.

It benefits from a full implementation of the SQL standard and as a result queries from other RDBMS become easily portable.
In addition, it caters to more modern web usecases by allowing storage and indexing of unstructured data, such as JSON, in the same way strictly tabular data is indexed within the database.

#### Pros

* Digirati has many years of experience designing, deploying, and maintaining PostgreSQL databases.
* PostgreSQL will easily handle queries for more complex reporting reporting, due to the availability of powerful SQL features like windowing, partitioning, CTEs, etc.
* PostgreSQL has a strong following in the container/cloud computing ecosystem, making troubleshooting, debugging, and implementation easier with the resources provided by the community.

#### Cons

* Replication is a very new feature to PostgreSQL. If BFI required that this data is replicated to a backup PostgreSQL server this may be tricky to implement.

### MySQL

MySQL is an open-source RDBMS implementation maintained by Oracle.
It is used in many technology stacks for its fast storage engine: InnoDB.

The MySQL user-base is somewhat fragmented, with two popular forks (PerconaDB and MariaDB) gaining more popularity than the upstream distribution in recent years.
MySQL had stagnated for a long time in regards to releasing patches for bugs and implementing newer parts of the SQL standard.
PerconaDB and MariaDB took charge on bringing MySQL up to speed in their respective forks, and more recently upstream MySQL has begun to adopt these changes in MySQL 8+ onwards.

#### Pros

* Digirati have years of experience deploying and maintaining PostgreSQL databases.
* BFI have experience deploying and maintaining MySQL based solutions
* A straight-forward replication implementation. MySQL offers the most simple approach to setting up a backup replicated server from a main source of truth out of any existing RDBMS implementations.
* Good performance from the InnoDB engine. The InnoDB storage engine is not as capable as its competitor in regards to flexibility, however, it is very capable at storing flat tabular data.

#### Cons

* Designing an efficient data model is more difficult than in other RDBMS, due to missing standard SQL features
* Does not cover the full SQL standard, especially in versions before MySQL 8.
* Documentation, deployment guides, etc. tend to be split between MariaDB and PerconaDB.
