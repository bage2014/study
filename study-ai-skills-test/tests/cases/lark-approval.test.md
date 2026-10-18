# Test Case: lark-approval

- **Case ID**: `TC-lark-approval`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu approvals: query/process approval todos/done/instances, search definitions, view details, initiate native approvals.

## Trigger condition

handle approval tasks / view instances / search or initiate approvals

## Prerequisites

- Lark authorization (lark-shared auth)
- Approval instance/definition context

## Test prompt (send this to the assistant)

```text
List my pending approval todos in Feishu.
```

## Expected behavior

Skill loads; uses lark-approval to fetch pending approval todos (requires auth).

## Assertions (pass criteria)

- [ ] Routes to lark-approval
- [ ] Returns approval todo list (or auth-needed message)
- [ ] Does not use lark-task

## Notes / Actual result

_fill in after running_
