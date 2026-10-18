# Test Case: lark-workflow-meeting-summary

- **Case ID**: `TC-lark-workflow-meeting-summary`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Workflow: summarize meeting minutes within a time range into a structured report.

## Trigger condition

organize meeting minutes / weekly report

## Prerequisites

- Lark authorization
- A time range

## Test prompt (send this to the assistant)

```text
Summarize my Feishu meeting minutes from this week into a report.
```

## Expected behavior

Skill loads; fetches minutes in range and produces a structured report.

## Assertions (pass criteria)

- [ ] Routes to lark-workflow-meeting-summary
- [ ] Summarizes minutes
- [ ] Structured report

## Notes / Actual result

_fill in after running_
