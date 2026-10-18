# Test Case: common-data-migration

- **Case ID**: `TC-common-data-migration`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Database migration via Flyway or Liquibase.

## Trigger condition

need DB migration / Flyway / Liquibase

## Prerequisites

- A schema change requirement

## Test prompt (send this to the assistant)

```text
Generate a Flyway migration to add a 'email' column to table 'users'.
```

## Expected behavior

Skill loads; produces a Flyway migration file (Vx__add_email.sql) compliant with Flyway naming.

## Assertions (pass criteria)

- [ ] Migration file follows Flyway naming V..__..sql
- [ ] ALTER TABLE users ADD COLUMN email
- [ ] Mentions Flyway

## Notes / Actual result

_fill in after running_
