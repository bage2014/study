# Test Case: lark-sheets

- **Case ID**: `TC-lark-sheets`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu Sheets: create, manage sheets, read/write cells, find/replace, charts, pivot tables, formatting.

## Trigger condition

create or edit spreadsheets, batch read/write, stats

## Prerequisites

- Lark authorization
- A sheet token

## Test prompt (send this to the assistant)

```text
Read cells A1:C3 from my Feishu sheet 'xxxx'.
```

## Expected behavior

Skill loads; reads the specified range.

## Assertions (pass criteria)

- [ ] Routes to lark-sheets
- [ ] Reads range A1:C3
- [ ] Uses sheet token

## Notes / Actual result

_fill in after running_
