# Test Case: lark-task

- **Case ID**: `TC-lark-task`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu tasks: create todos, update status, subtasks, lists, assignees, attachments, task agents.

## Trigger condition

create or manage tasks/todos

## Prerequisites

- Lark authorization

## Test prompt (send this to the assistant)

```text
Create a Feishu task 'Buy milk' due tomorrow.
```

## Expected behavior

Skill loads; creates a task with due date.

## Assertions (pass criteria)

- [ ] Routes to lark-task
- [ ] Creates task
- [ ] Due tomorrow

## Notes / Actual result

_fill in after running_
