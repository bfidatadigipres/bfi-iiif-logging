# Use of technology/framework in API implementation

* Status: proposed
* Deciders: Daniel Grant <daniel.grant@digirati.com>, Gary Tierney <gary.tierney@digirati.com>, Stephen Fraser <stephen.fraser@digirati.com>
* Date: 2021-02-16

## Context and Problem Statement

Digirati must decide on the technologies (and frameworks, if applicable) used to implement the API backend used to serve the audit log requirements by the BFI.

## Decision Drivers <!-- optional -->

* Rapid development on Digirati's side
* Long-term support and stability
* Ease of deployment

## Considered Options

* Kotlin, Spring
* TypeScript, Express
* Python, Django

<!-- ## Decision Outcome -->

<!-- Chosen option: "[option 1]", because [justification. e.g., only option, which meets k.o. criterion decision driver | which resolves force force | … | comes out best (see below)]. -->

<!-- ### Positive Consequences optional -->

<!-- * [e.g., improvement of quality attribute satisfaction, follow-up decisions required, …] -->
<!-- * … -->

<!-- ### Negative Consequences optional -->

<!-- * [e.g., compromising quality attribute, follow-up decisions required, …] -->
<!-- * … -->

## Pros and Cons of the Options

### Kotlin, Spring

Spring is a well-known solution to building applications on JVM platforms.
It has been the industry leader in the Java space for a long time and as a result has no trouble solving the majority of technical problems we'd encounter out of the box.

Kotlin is relatively new to the scene when compared to Java, however, it is still a JVM language and offers interoperability both ways between Java and Kotlin code.
The main selling point of Kotlin is the ability to write code unconstrained by the boilerplate of Java while still having the entire Java ecosystem available as tooling.

#### Pros

* 20+ years of combined experience in JVM/Spring applications
* Tooling for the JVM and Kotlin makes developing the project a plug-n-play experience
* Spring Boot framework suits itself well to rapid development
* Zero-configuration OAuth2 implementation
* JVM as a deployment target may make bare-metal deployment easier when running existing JVM applications
* Rich documentation and examples

#### Cons

* No experience at BFI with writing code for the JVM

### TypeScript, Express

Express is a barebones web server implementation for the NodeJS platform.
The web server itself is mostly a skeleton, which is usually extended using middleware that are able to intercept requests and responses, reading and writing to them as needed to add additional functionality.
There is a lot of existing middleware for Express developed by the community that we would use to fulfill our needs here (e.g. sessions, oauth2).

TypeScript is a loosely typed programming language that sits over the top of JavaScript and adds strict validation over program source code, much like in a Java application.
This allows us to write code that stays clear and maintainable over time, by using the type system to document itself.
A fully TypeScript codebase requires that dependencies also have TypeScript bindings available.

#### Pros

* Experience building lightweight Express applications
* Rapid development from constant recompilation (e.g. watch style commands)

#### Cons

* Less of a complete solution out of the box
* Poor documentation, outdated examples, frequently changing ecosystem

### Python, Django

Django is a web framework intended for rapid development in Python.
Like Spring, it comes with the majority of the tools that are needed in building a modern website out of the box and allows you to get started writing domain specific code.

Python seems to be the language of choice for BFI, however it is not a typical choice for Digirati and while we have experience building applications in Python our general preference is to use something we use frequently and allows us to not think about framework details.


#### Pros

* Preferred by BFI
* Experience at Digirati building Python applications, albeit not as much as JVM
* Django itself is build  for rapid development, changing the source code is all you need to do to make a change (less of an issue with other technologies in recent times)
* Rich documentation, lots of examples, stable versioning

#### Cons

* Likely not the best choice for rapid development at Digirati.
