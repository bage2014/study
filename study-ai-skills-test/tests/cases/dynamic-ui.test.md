# Test Case: dynamic-ui

- **Case ID**: `TC-dynamic-ui`
- **Category**: other
- **Status**: pass
- **Requires external setup**: no

## Description

Render visual content inline (diagrams, charts, interactive demos) alongside text.

## Trigger condition

a compact visual makes the answer clearer

## Prerequisites

- A concept/numbers to visualize

## Test prompt (send this to the assistant)

```text
Show a small bar chart of sales: Jan=10, Feb=20, Mar=15.
```

## Expected behavior

Skill loads; agent invokes PureShowWidget with an SVG/HTML chart rendering the data.

## Assertions (pass criteria)

- [ ] A visual widget is rendered
- [ ] Chart reflects Jan=10, Feb=20, Mar=15
- [ ] Inline with text response

## Notes / Actual result

Rendered inline SVG bar chart via PureShowWidget: Jan=10, Feb=20 (focal 'highest'), Mar=15; light+dark token themes; value labels. All assertions met.
