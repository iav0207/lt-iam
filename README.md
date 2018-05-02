# Identity and Access Management

This is a solution to a coding task, not a real app.

[![Build Status](https://travis-ci.org/iav0207/lt-iam.svg?branch=master)](https://travis-ci.org/iav0207/lt-iam)

### Supported API methods

```
POST    /organizations
DELETE  /organizations/{id}
GET     /organizations/{id}
POST    /organizations/{id}
POST    /sessions
DELETE  /sessions/{id}
GET     /sessions/{id}
POST    /users
DELETE  /users/{id}
GET     /users/{id}
POST    /users/{id}
```

### Project structure

* [api](/src/main/java/task/lt/api) – API objects: JSON models, typical requests, responses and errors
* [core](/src/main/java/task/lt/core) – IDs and passwords handling
* [db](/src/main/java/task/lt/db) – Data access layer: JDBI DAOs and mappers
* [resources](/src/main/java/task/lt/resources) – API resources (endpoints)
* [main class](/src/main/java/task/lt/IamApplication.java)

### Technical info

Frameworks and libs used:

* Dropwizard framework with embedded Jetty server, Jersey HTTP client, Jackson for serialization.
* No ORM. Data access implemented via JDBI / SQL Object interface.
* No DI framework.
* DBMS: in-memory H2 DB.
* Testing: TestNG + Dropwizard resources testing support, AssertJ + Hamcrest matchers
* Guava
* HashIds
* Apache commons

Run it!
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/iam-1.0-SNAPSHOT.jar server config.yml`

To run integration tests run `mvn clean install && mvn failsafe:integration-test`
