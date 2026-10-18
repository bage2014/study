# Test Case: lark-workflow-standup-report

- **Case ID**: `TC-lark-workflow-standup-report`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Workflow: calendar agenda + tasks -> summary of schedule and open tasks for a date.

## Trigger condition

understand today/tomorrow/this week schedule and open tasks

## Prerequisites

- Lark authorization
- A target date

## Test prompt (send this to the assistant)

```text
Give me a standup report for today: my schedule and open tasks.
```

## Expected behavior

Skill loads; combines calendar + tasks into a standup summary.

## Assertions (pass criteria)

- [ ] Routes to lark-workflow-standup-report
- [ ] Combines calendar + tasks
- [ ] For today's date

## Notes / Actual result

_fill in after running_
