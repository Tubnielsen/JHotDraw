package org.jhotdraw.draw.tool;

import java.awt.geom.Point2D;

import org.jhotdraw.geom.BezierPath;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for small, logic-heavy parts of {@link BezierTool}.
 * <p>
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

        assertThat(source).hasSize(2);
        assertThat(tail).hasSize(3);
        // Order check
        assertThat(tail.get(0).x[0]).isEqualTo(2.0);
        assertThat(tail.get(1).x[0]).isEqualTo(3.0);
        assertThat(tail.get(2).x[0]).isEqualTo(4.0);
    }

    @Test
    public void extractTailPath_withStartIndexAtEnd_returnsEmptyTail() {
        BezierPath source = pathWithNNodes(3);
        BezierPath tail = BezierTool.extractTailPath(source, 3);
        assertThat(source).hasSize(3);
        assertThat(tail).isEmpty();
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
