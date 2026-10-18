# Test Case: personal-brief-style

- **Case ID**: `TC-personal-brief-style`
- **Category**: personal
- **Status**: pass
- **Requires external setup**: no

## Description

Concise style: no fluff, caveman-style short direct expressions.

## Trigger condition

need to enforce brief/caveman response style

## Prerequisites

- A question to answer briefly

## Test prompt (send this to the assistant)

```text
Answer briefly: what is TDD?
```

## Expected behavior

Skill loads; response is short, direct, no fluff (caveman-style).

## Assertions (pass criteria)

- [ ] Response is very short
- [ ] No filler words
- [ ] Direct answer

## Notes / Actual result

Actual answer: 'TDD: 测试先行。红-绿-重构。先写失败测试，再写实现，再重构。' — short, direct, no filler words. All assertions met.
