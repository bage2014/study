# Test Case: gp-agent-handoff

- **Case ID**: `TC-gp-agent-handoff`
- **Category**: other
- **Status**: untested
- **Requires external setup**: no

## Description

Create a truthful, evidence-bounded handoff to the Master Agent before a General Purpose subagent returns its final result.

## Trigger condition

immediately before a GP subagent returns its final result

## Prerequisites

- A GP subagent task completing

## Test prompt (send this to the assistant)

```text
(Internal) A GP subagent finished researching 'DB migration tools'. Produce a handoff summary.
```

## Expected behavior

Skill loads; agent writes a concise handoff with evidence, no fabrication, bounded claims.

## Assertions (pass criteria)

- [ ] Handoff is concise
- [ ] Claims are evidence-bounded
- [ ] No fabrication

## Notes / Actual result

_fill in after running_
