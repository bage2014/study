# Test Case: TRAE-generate-mini-app

- **Case ID**: `TC-TRAE-generate-mini-app`
- **Category**: trae
- **Status**: untested
- **Requires external setup**: no

## Description

Create/modify runnable Taro-based mini-program code (WeChat/Alipay/Douyin mini-program).

## Trigger condition

user explicitly asks to build/generate/develop/edit a mini-program

## Prerequisites

- Clear mini-program feature requirement

## Test prompt (send this to the assistant)

```text
Generate a WeChat mini-program with one page showing a button that logs 'hello' on tap.
```

## Expected behavior

Skill loads; agent emits Taro mini-program files (page config, component) rather than generic web code.

## Assertions (pass criteria)

- [ ] Output contains mini-program / Taro files
- [ ] Code is runnable mini-program structure
- [ ] Not plain React web app

## Notes / Actual result

_fill in after running_
