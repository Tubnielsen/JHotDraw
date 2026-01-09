# User Story (Change Request)

**As a user, I want to draw and complete Bezier curves on the canvas without errors, so that I can create smooth and correct curves as part of my drawing.**

## Acceptance criteria
1. Finishing a curve (mouse release) must not crash the application, even if the tool is switched mid-interaction.
2. Completing a curve must produce a valid Bezier path (no missing nodes, no inconsistent state).
3. Regression protection: core curve-completion logic must be covered by automated tests.
