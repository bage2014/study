# Test Case: TRAE-debugger

- **Case ID**: `TC-TRAE-debugger`
- **Category**: trae
- **Status**: untested
- **Requires external setup**: no

## Description

Debug complex issues via runtime evidence collection; starts a debug server, scientific workflow (hypothesize->instrument->reproduce->analyze->fix->verify).

## Trigger condition

debugging complex issues that need runtime evidence, or repeated static analysis failed

## Prerequisites

- A reproducible bug scenario
- Permission to run a debug server

## Test prompt (send this to the assistant)

```text
I have a Spring Boot endpoint that returns 500 on POST /api/order. Help me debug it systematically.
```

## Expected behavior

Skill loads; agent follows scientific debugging workflow, proposes hypotheses, asks for/collects runtime evidence, not just guesses.

## Assertions (pass criteria)

- [ ] Mentions hypotheses or evidence collection
- [ ] Asks for runtime info (logs/trace) rather than guessing the fix outright

## Notes / Actual result

_fill in after running_
