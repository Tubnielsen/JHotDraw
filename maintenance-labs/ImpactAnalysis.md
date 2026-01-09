# Impact Analysis (Bezier Tool)

## Change scope
The maintenance change is **localized** to the Bezier tool implementation and its tests:
- Production code: `BezierTool.java`
- Tests: `BezierToolTest.java`, `BezierToolBddTest.java` + stages
- CI: GitHub Actions workflow

## Likely impacts / risks
1. **Event-ordering risks (null-state):** `mouseDragged/mouseMoved/mouseReleased` can be invoked when `createdFigure` is `null` (e.g., tool switch or cancel). This can crash the app.
2. **Curve completion correctness:** extracting and fitting the tail of the path must preserve node order and indices.
3. **Regression surface:** changes affect only Bezier curve creation; other tools and figures are unaffected.

## Package impact table (visited during analysis)

| Package | # classes visited | Comments |
|---|---:|---|
| `org.jhotdraw.draw.tool` | 1–3 | `BezierTool` and nearby tool base classes for event flow |
| `org.jhotdraw.draw.figure` | 1–3 | Bezier figure/path storage and figure invalidation |
| `org.jhotdraw.geom` | 1–3 | `BezierPath` node structure used for extraction |
| `org.jhotdraw.draw` | 1–3 | `DrawingView` / editor context used by tools |

## Verification plan (mitigation)
- Unit tests validate tail extraction (normal + boundary + invalid input).
- BDD scenarios (Given–When–Then) verify the user story at the logic level.
- CI runs `mvn test` to prevent regressions.