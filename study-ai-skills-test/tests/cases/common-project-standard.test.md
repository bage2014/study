# Test Case: common-project-standard

- **Case ID**: `TC-common-project-standard`
- **Category**: common
- **Status**: pass
- **Requires external setup**: no

## Description

Project structure, document management, .gitignore standards and best practices.

## Trigger condition

need project structure / conventions / standards

## Prerequisites

- A project to standardize

## Test prompt (send this to the assistant)

```text
Recommend project structure and .gitignore for a Java Spring Boot + Vue monorepo.
```

## Expected behavior

Skill loads; gives standardized directory layout and .gitignore entries for the stack.

## Assertions (pass criteria)

- [ ] Provides directory structure
- [ ] Provides .gitignore content
- [ ] Tailored to Spring Boot + Vue

## Notes / Actual result

Returned Spring Boot+Vue monorepo directory layout (backend/ frontend/ shared/) and a combined .gitignore (target/, node_modules/, .idea/, *.log, etc.). All assertions met.
