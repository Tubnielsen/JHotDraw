# Actualization (Clean Architecture / SOLID)

## Implemented change
The Bezier tool was improved to complete curves safely and consistently by guarding against invalid tool state and by isolating the path-manipulation logic used during curve fitting.

## SOLID / Clean Code justification
- **SRP (Single Responsibility):** `mouseReleased(...)` now mainly coordinates the completion process, while `extractTailPath(...)` performs a focused data transformation.
- **Fail-fast preconditions:** `extractTailPath(...)` validates `startIndex` and throws `IllegalArgumentException` for invalid ranges.
- **Reduced coupling to UI:** the extracted helper is a pure function over `BezierPath`, making it testable without UI automation.
- **Clean Code readability:** guard clauses flatten control flow and reduce nested conditions.

## Outcome
The change reduces crash risk (null-state during event handling) and improves maintainability by making the most error-prone logic small, named, and covered by automated tests.
