# Test Case: TRAE-browseruse

- **Case ID**: `TC-TRAE-browseruse`
- **Category**: trae
- **Status**: untested
- **Requires external setup**: yes

## Description

Browser automation: navigate URLs, scrape content, test frontend UI, multi-step browser actions.

## Trigger condition

user wants to browse websites / access URLs / scrape web / test frontend UI / browser interaction

## Prerequisites

- A live http(s) URL to test against
- Browser environment available

## Test prompt (send this to the assistant)

```text
Open https://example.com and tell me the page title and the first heading text.
```

## Expected behavior

Skill loads browser-use guidance; agent navigates to URL, reads DOM, returns title (Example Domain) and h1 (Example Domain).

## Assertions (pass criteria)

- [ ] Response mentions page title 'Example Domain'
- [ ] Response mentions heading 'Example Domain'
- [ ] Did not fabricate content

## Notes / Actual result

_fill in after running_
