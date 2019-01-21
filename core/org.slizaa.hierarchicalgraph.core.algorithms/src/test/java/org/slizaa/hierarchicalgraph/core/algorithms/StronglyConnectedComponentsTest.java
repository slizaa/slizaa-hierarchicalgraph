package org.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class StronglyConnectedComponentsTest {

    @Test
    public void detectCycle() {

        //
        List<HGNode> nodes = null;

        //
        List<List<HGNode>> stronglyConnectedComponents = GraphUtils.detectStronglyConnectedComponents(nodes);

        //
        assertThat(stronglyConnectedComponents).hasSize(3);

        //
        for (List<HGNode> scc : stronglyConnectedComponents) {
            if (scc.size() == 2) {
                assertThat(scc).contains(nodes.get(2)).contains(nodes.get(3));
            }
        }
    }
}
