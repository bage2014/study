# Test Case: personal-frontend-coding-standard

- **Case ID**: `TC-personal-frontend-coding-standard`
- **Category**: personal
- **Status**: untested
- **Requires external setup**: no

## Description

Frontend coding standards and best practices guidance.

## Trigger condition

need frontend coding standards

## Prerequisites

- Frontend code to standardize

## Test prompt (send this to the assistant)

```text
Review this Vue component against frontend coding standards:
```vue
<template><div>{{msg}}</div></template>
<script>export default{data(){return{msg:'hi'}}}</script>
```
```

## Expected behavior

Skill loads; checks frontend standards (naming, structure, reactivity, eslint conventions).

## Assertions (pass criteria)

- [ ] Mentions frontend standards
- [ ] Points out convention issues
- [ ] Gives concrete fixes

## Notes / Actual result

_fill in after running_
