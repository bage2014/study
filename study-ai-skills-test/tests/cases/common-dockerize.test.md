# Test Case: common-dockerize

- **Case ID**: `TC-common-dockerize`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Docker containerization: generate Docker configuration files.

## Trigger condition

need Dockerfile / docker-compose / containerization

## Prerequisites

- An app to containerize

## Test prompt (send this to the assistant)

```text
Generate a Dockerfile and docker-compose for a Spring Boot app on port 8080.
```

## Expected behavior

Skill loads; produces Dockerfile (multi-stage optional) and docker-compose.yml with port mapping.

## Assertions (pass criteria)

- [ ] Produces Dockerfile
- [ ] Produces docker-compose.yml
- [ ] Maps port 8080

## Notes / Actual result

_fill in after running_
