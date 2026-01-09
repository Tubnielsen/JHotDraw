package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.integration.junit.ScenarioTest;
import org.jhotdraw.geom.BezierPath;
import org.junit.Test;

/**
 * BDD-style scenarios for the Bezier tool maintenance task.
 * <p>
 * The user story is verified at the logic level by focusing on the extracted helper
 * method used during curve completion.
 */
public class BezierToolBddTest extends ScenarioTest<BezierToolStages.GivenBezierPath,
        BezierToolStages.WhenExtractingTail,
        BezierToolStages.ThenTailExtractionResult> {

    @Test
    public void complete_curve_extracts_tail_nodes_without_errors() {
        given().a_bezier_path_with_n_nodes(5);
        when().the_tail_is_extracted_from_start_index(2);
        then().source_keeps_n_nodes(2)
                .and().tail_contains_n_nodes(3)
                .and().tail_starts_with_x_coordinate(2.0);
    }

    @Test
    public void completing_curve_at_end_produces_empty_tail() {
        given().a_bezier_path_with_n_nodes(3);
        when().the_tail_is_extracted_from_start_index(3);
        then().tail_is_empty()
                .and().source_keeps_n_nodes(3);
    }

    @Test
    public void invalid_start_index_is_rejected() {
        given().a_bezier_path_with_n_nodes(1);
        when().the_tail_is_extracted_from_start_index(-1);
        then().an_illegal_argument_exception_is_thrown();
    }
}
