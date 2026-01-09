# Concept Location (Bezier Tool)

## Goal
Locate the implementation concepts involved in creating and completing Bezier curves.

## Approach (static + dynamic)
- **Static analysis:** search for `BezierTool`, `BezierPath`, and event-handler methods (`mousePressed/Dragged/Released`).
- **Dynamic analysis:** set breakpoints in `BezierTool.mouseDragged()` and `BezierTool.mouseReleased()`, then draw a curve and inspect the call stack and current objects (`createdFigure`, `BezierPath`).

## Primary locations
- `jhotdraw-core/src/main/java/org/jhotdraw/draw/tool/BezierTool.java`
  - `mouseDragged(...)` – adds/updates nodes as the user draws.
  - `mouseReleased(...)` – completes the curve; performs curve fitting and finalizes the path.
  - `mouseMoved(...)` – updates the last point during drawing.
- `jhotdraw-utils/src/main/java/org/jhotdraw/geom/BezierPath.java`
  - Path representation as a list of `BezierPath.Node`.

## Domain class responsibility table

| Domain class | Responsibility in the feature |
|---|---|
| `BezierTool` | Translates mouse events to edits on a Bezier figure/path; triggers completion/curve fitting on release |
| `BezierPath` | Data structure for Bezier nodes/segments (points + control points) |
| `BezierFigure` (created by tool) | Figure that stores and renders a `BezierPath` on the drawing |
| `DrawingView` / `DrawingEditor` | Provides the active view/editor context and dispatches events to the active tool |

## Key observations
- The tool is **event-driven** and therefore sensitive to **state validity** (`createdFigure` may be `null` if the tool is switched or cancelled).
- The curve-completion logic is the most logic-heavy part, making it the best candidate for extraction and unit testing.
