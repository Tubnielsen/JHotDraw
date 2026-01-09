# Refactoring Notes (Clean Code / Code Smells)

## Observed smells
- **Long / complex method:** `BezierTool.mouseReleased(...)` mixed UI concerns (event handling) with non-trivial path manipulation for curve fitting.
- **Fragile state in event-driven code:** mouse event handlers assumed `createdFigure` is always non-null, which is not guaranteed if the tool is switched/cancelled mid-interaction.
- **Low testability:** the path-extraction logic was embedded inside the event handler, making it hard to test without driving the UI.

## Refactoring strategy (Prefactoring → Actualization → Postfactoring)
1. **Stabilize first (prefactoring):** add guard clauses to avoid `NullPointerException` for unexpected event sequences.
2. **Extract logic (prefactoring):** move the tail-extraction algorithm into a dedicated helper method.
3. **Add preconditions (postfactoring):** validate the helper method inputs to make behavior explicit.
4. **Verify (verification):** add unit tests and BDD scenarios.

## Applied refactoring patterns
- **Guard Clauses:** early returns in `mouseMoved`, `mouseDragged`, and `mouseReleased` when there is no active `createdFigure`.
- **Extract Method:** `extractTailPath(BezierPath source, int startIndex)` isolates the logic-heavy part.
- **Introduce Preconditions:** invalid indices throw `IllegalArgumentException`, preventing silent corruption.

## Clean Code alignment
- **Clear names:** extracted method name documents intent.
- **Reduced nesting:** guard clauses flatten the control flow.
- **Separation of concerns:** event handler coordinates; helper method performs pure data manipulation.
