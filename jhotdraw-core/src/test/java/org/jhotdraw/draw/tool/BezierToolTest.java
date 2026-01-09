package org.jhotdraw.draw.tool;

import org.jhotdraw.geom.BezierPath;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

/**
 * Unit tests for small, logic-heavy parts of {@link BezierTool}.
 * The UI behaviour of the tool is tested indirectly by exercising the extracted helper methods.
 */
public class BezierToolTest {

    private static BezierPath pathWithNNodes(int n) {
        BezierPath p = new BezierPath();
        for (int i = 0; i < n; i++) {
            p.add(new BezierPath.Node(new Point2D.Double(i, i)));
        }
        return p;
    }

    @Test
    public void extractTailPath_movesNodesFromStartIndexInclusive() {
        BezierPath source = pathWithNNodes(5);

        BezierPath tail = BezierTool.extractTailPath(source, 2);

        assertEquals(2, source.size());
        assertEquals(3, tail.size());

        assertEquals(2.0, tail.get(0).x[0], 0.000001);
        assertEquals(3.0, tail.get(1).x[0], 0.000001);
        assertEquals(4.0, tail.get(2).x[0], 0.000001);
    }

    @Test
    public void extractTailPath_withStartIndexAtEnd_returnsEmptyTail() {
        BezierPath source = pathWithNNodes(3);

        BezierPath tail = BezierTool.extractTailPath(source, 3);

        assertEquals(3, source.size());
        assertTrue(tail.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractTailPath_rejectsNegativeStartIndex() {
        BezierTool.extractTailPath(pathWithNNodes(1), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractTailPath_rejectsStartIndexGreaterThanSize() {
        BezierTool.extractTailPath(pathWithNNodes(1), 2);
    }
}
