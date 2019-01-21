package org.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class DependencyStructureMatrixTest {

    @ClassRule
    public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

    @Test
    public void detectCycle() {

        //
        List<HGNode> nodes = _graphProvider.node(577L).getChildren();

        //
        IDependencyStructureMatrix dsm = GraphUtils.createDependencyStructureMatrix(nodes);

        // assert ordered nodes
        assertThat(dsm.getOrderedNodes()).hasSize(34);

        for (AbstractHGDependency upwardDependency : dsm.getUpwardDependencies()) {
            System.out.println(String.format("%s -[%s]-> %s", upwardDependency.getFrom().getIdentifier(), ((HGAggregatedDependency)upwardDependency).getAggregatedWeight(), upwardDependency.getTo().getIdentifier()));
        }
        
//        // assert upward dependencies
//        assertThat(dsm.getUpwardDependencies()).hasSize(14)
//                .containsExactly(nodes.get(3).getOutgoingDependenciesTo(nodes.get(2)));

        // assert cycles
        assertThat(dsm.getCycles()).hasSize(1);
        assertThat(dsm.getCycles().get(0)).containsExactly(nodes.get(3), nodes.get(2));

        assertThat(dsm.getCycles().get(0)).hasSize(10);
        assertThat(dsm.getCycles().get(0)).containsExactlyInAnyOrder(node(14903), node(14576), node(7676), node(14485), node(16667),
                node(20483), node(19397), node(19647), node(7155), node(19564));
    }

    private HGNode node(long id) {
        return _graphProvider.node(id);
    }
}
