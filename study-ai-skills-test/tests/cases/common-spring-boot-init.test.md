# Test Case: common-spring-boot-init

- **Case ID**: `TC-common-spring-boot-init`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Quickly create Spring Boot project structure with multiple DB/dependency options.

## Trigger condition

need to scaffold a Spring Boot project

## Prerequisites

- Project requirements (DB, deps)

## Test prompt (send this to the assistant)

```text
Scaffold a Spring Boot 3 project with Maven, H2, Spring Web, and Spring Data JPA.
```

## Expected behavior

Skill loads; produces pom.xml and base package structure with the requested dependencies.

## Assertions (pass criteria)

- [ ] Produces pom.xml with Spring Boot 3
- [ ] Includes H2, Web, Data JPA
- [ ] Has base package structure

## Notes / Actual result

_fill in after running_
