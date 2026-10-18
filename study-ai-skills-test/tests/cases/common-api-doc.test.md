# Test Case: common-api-doc

- **Case ID**: `TC-common-api-doc`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Generate API documentation from OpenAPI spec; supports Swagger UI and ReDoc.

## Trigger condition

need API docs / OpenAPI / Swagger / ReDoc

## Prerequisites

- An OpenAPI spec or controller source

## Test prompt (send this to the assistant)

```text
Generate API docs (Swagger UI + ReDoc) for a REST endpoint GET /api/users/{id} returning a User JSON.
```

## Expected behavior

Skill loads; produces OpenAPI spec and references Swagger UI/ReDoc presentation.

## Assertions (pass criteria)

- [ ] Output includes OpenAPI YAML/JSON
- [ ] Mentions Swagger UI or ReDoc
- [ ] Endpoint GET /api/users/{id} is documented

## Notes / Actual result

_fill in after running_
