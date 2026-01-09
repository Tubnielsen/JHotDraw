# BDD Scenarios (JGiven)

## User Story
As a user, I want to draw and complete Bezier curves on the canvas without errors, so that I can create smooth and correct curves as part of my drawing.

## Scenario 1: Completing a curve extracts the tail for curve fitting
**Given** a Bezier path with 5 nodes
**When** the tail is extracted from start index 2
**Then** the source keeps 2 nodes
**And** the tail contains 3 nodes
**And** the tail starts with x-coordinate 2.0

## Scenario 2: Completing at the end yields no tail
**Given** a Bezier path with 3 nodes
**When** the tail is extracted from start index 3
**Then** the tail is empty
**And** the source keeps 3 nodes

## Scenario 3: Invalid start index is rejected
**Given** a Bezier path with 1 node
**When** the tail is extracted from start index -1
**Then** an IllegalArgumentException is thrown
