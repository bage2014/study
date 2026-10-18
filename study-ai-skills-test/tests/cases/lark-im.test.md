# Test Case: lark-im

- **Case ID**: `TC-lark-im`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu IM: send/reply messages, search chats, manage groups, upload/download files, emoji replies, urgent, interactive cards, card callbacks.

## Trigger condition

send messages, manage chats/groups, interactive cards

## Prerequisites

- Lark authorization
- A chat/user target

## Test prompt (send this to the assistant)

```text
Send a 'hello' message to user 'zhangsan' in Feishu.
```

## Expected behavior

Skill loads; resolves recipient and sends IM message.

## Assertions (pass criteria)

- [ ] Routes to lark-im
- [ ] Sends message
- [ ] Uses correct recipient

## Notes / Actual result

_fill in after running_
