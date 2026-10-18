# Test Case: skill-creator

- **Case ID**: `TC-skill-creator`
- **Category**: personal
- **Status**: untested
- **Requires external setup**: no

## Description

Create or update project/global skills, or install skills globally.

## Trigger condition

asked to create/edit/explain/install a skill

## Prerequisites

- A skill to create or edit

## Test prompt (send this to the assistant)

```text
What does this skill do and when should it be used? (Explain the skill-creator skill).
```

## Expected behavior

Skill loads; agent explains skill-creator purpose and usage (note: not a callable tool itself).

## Assertions (pass criteria)

- [ ] Explains skill-creator purpose
- [ ] States when to use it
- [ ] Mentions it creates/updates skills

## Notes / Actual result

_fill in after running_
