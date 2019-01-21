package org.slizaa.hierarchicalgraph.core.algorithms;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class CyclesTest {

    @ClassRule
    public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

    @Test
    public void detectCycle() {

        //
        List<HGNode> nodes = _graphProvider.node(577L).getChildren();

        //
        List<List<HGNode>> cycles = GraphUtils.detectCycles(nodes);

        //
        assertThat(cycles).hasSize(1);

        //
        assertThat(cycles.get(0)).hasSize(10);
        assertThat(cycles.get(0)).containsExactlyInAnyOrder(node(14903), node(14576), node(7676), node(14485), node(16667),
                node(20483), node(19397), node(19647), node(7155), node(19564));
    }

    private HGNode node(long id) {
        return _graphProvider.node(id);
    }
}
