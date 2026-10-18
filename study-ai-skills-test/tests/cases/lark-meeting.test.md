# Test Case: lark-meeting

- **Case ID**: `TC-lark-meeting`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu meetings: query records/minutes/transcripts, upload/edit minutes, bot participation, real-time meeting content,妙记 search.

## Trigger condition

query meeting records, minutes,妙记; participate in meetings

## Prerequisites

- Lark authorization
- A meeting/妙记 id or URL

## Test prompt (send this to the assistant)

```text
Search my Feishu meeting minutes for 'Q3 review'.
```

## Expected behavior

Skill loads; searches meeting minutes.

## Assertions (pass criteria)

- [ ] Routes to lark-meeting
- [ ] Searches minutes
- [ ] Keyword 'Q3 review'

## Notes / Actual result

_fill in after running_
