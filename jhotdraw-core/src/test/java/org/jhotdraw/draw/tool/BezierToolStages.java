package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.geom.BezierPath;

import java.awt.geom.Point2D;

import static org.assertj.core.api.Assertions.*;

/**
 * JGiven stages used by {@link BezierToolBddTest}.
 */
public class BezierToolStages {

    public static class GivenBezierPath extends Stage<GivenBezierPath> {
        @ProvidedScenarioState
        BezierPath source;

        public GivenBezierPath a_bezier_path_with_n_nodes(int n) {
            source = new BezierPath();
            for (int i = 0; i < n; i++) {
                source.add(new BezierPath.Node(new Point2D.Double(i, i)));
            }
            return self();
        }
    }

    public static class WhenExtractingTail extends Stage<WhenExtractingTail> {
        @ScenarioState
        BezierPath source;

        @ProvidedScenarioState
        BezierPath tail;

        @ProvidedScenarioState
        Throwable thrown;

        public WhenExtractingTail the_tail_is_extracted_from_start_index(int startIndex) {
            try {
                tail = BezierTool.extractTailPath(source, startIndex);
            } catch (Throwable t) {
                thrown = t;
            }
            return self();
        }
    }

    public static class ThenTailExtractionResult extends Stage<ThenTailExtractionResult> {
        @ScenarioState
        BezierPath source;
        @ScenarioState
        BezierPath tail;
        @ScenarioState
        Throwable thrown;

        public ThenTailExtractionResult source_keeps_n_nodes(int n) {
            assertThat(thrown).as("no exception expected").isNull();
            assertThat(source).hasSize(n);
            return self();
        }

        public ThenTailExtractionResult tail_contains_n_nodes(int n) {
            assertThat(thrown).as("no exception expected").isNull();
            assertThat(tail).hasSize(n);
            return self();
        }

        public ThenTailExtractionResult tail_is_empty() {
            assertThat(thrown).as("no exception expected").isNull();
            assertThat(tail).isNotNull();
            assertThat(tail).isEmpty();
            return self();
        }

        public ThenTailExtractionResult tail_starts_with_x_coordinate(double x) {
            assertThat(thrown).as("no exception expected").isNull();
            assertThat(tail).isNotNull();
            assertThat(tail).isNotEmpty();
            assertThat(tail.get(0).x[0]).isEqualTo(x);
            return self();
        }

        public ThenTailExtractionResult an_illegal_argument_exception_is_thrown() {
            assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            return self();
        }
    }
}
