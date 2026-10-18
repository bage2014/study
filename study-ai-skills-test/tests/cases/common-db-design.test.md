# Test Case: common-db-design

- **Case ID**: `TC-common-db-design`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Database design: table structures and entity classes.

## Trigger condition

need DB schema / entity design

## Prerequisites

- Domain entities to model

## Test prompt (send this to the assistant)

```text
Design DB tables and JPA entities for User and Order (one user has many orders).
```

## Expected behavior

Skill loads; produces DDL for users/orders with FK and JPA entity classes with relationships.

## Assertions (pass criteria)

- [ ] Produces DDL with foreign key
- [ ] Produces JPA entities
- [ ] Models one-to-many relationship

## Notes / Actual result

_fill in after running_
