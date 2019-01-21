package org.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class DependencyStructureMatrixTest {

    @Test
    public void detectCycle() {

        //
        List<HGNode> nodes = TestModelCreator.createDummyModel();

        //
        IDependencyStructureMatrix dsm = GraphUtils.createDependencyStructureMatrix(nodes);

        // assert ordered nodes
        assertThat(dsm.getOrderedNodes()).hasSize(4).containsExactly(
                nodes.get(0), nodes.get(1), nodes.get(2),
                nodes.get(3)
        );
        
        // assert upward dependencies
        assertThat(dsm.getUpwardDependencies()).hasSize(1)
                .containsExactly(nodes.get(3).getOutgoingDependenciesTo(nodes.get(2)));

        // assert cycles
        assertThat(dsm.getCycles()).hasSize(1);
        assertThat(dsm.getCycles().get(0)).containsExactly(nodes.get(3), nodes.get(2));
    }
}
