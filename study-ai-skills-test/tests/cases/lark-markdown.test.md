# Test Case: lark-markdown

- **Case ID**: `TC-lark-markdown`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu native Markdown: view, create, upload, edit, compare remote vs local draft.

## Trigger condition

operate on Feishu native Markdown files

## Prerequisites

- Lark authorization
- A markdown file token

## Test prompt (send this to the assistant)

```text
Create a new Feishu native Markdown file with content '# Hello'.
```

## Expected behavior

Skill loads; creates a native markdown file.

## Assertions (pass criteria)

- [ ] Routes to lark-markdown
- [ ] Creates markdown file
- [ ] Content is '# Hello'

## Notes / Actual result

_fill in after running_
