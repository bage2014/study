# Test Case: personal-doc-manager

- **Case ID**: `TC-personal-doc-manager`
- **Category**: personal
- **Status**: pass
- **Requires external setup**: no

## Description

Personal doc management: changelog, spec, test reports under docs/ with structured subdirs and naming {module}-{content}-{date}.md.

## Trigger condition

manage change logs / spec docs / test reports in docs/

## Prerequisites

- A doc to manage

## Test prompt (send this to the assistant)

```text
Where should I store a spec doc for the 'order' module dated today, and what filename?
```

## Expected behavior

Skill loads; agent answers with docs/spec/order-spec-YYYYMMDD.md naming convention and structure.

## Assertions (pass criteria)

- [ ] Mentions docs/spec/ path
- [ ] Uses {module}-{content}-{date}.md format
- [ ] Correct subdir (spec)

## Notes / Actual result

Answered: store under docs/spec/ with filename order-<content>-2026-10-18.md, following {module}-{content}-{date}.md. All assertions met.
