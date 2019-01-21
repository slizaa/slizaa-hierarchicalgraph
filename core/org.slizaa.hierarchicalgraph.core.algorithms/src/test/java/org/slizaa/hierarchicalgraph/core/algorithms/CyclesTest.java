package org.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class CyclesTest {

    @Test
    public void detectCycle() {

        //
        List<HGNode> nodes = TestModelCreator.createDummyModel();

        //
        List<List<HGNode>> cycles = GraphUtils.detectCycles(nodes);

        //
        assertThat(cycles).hasSize(1);

        //
        assertThat(cycles.get(0)).contains(nodes.get(2)).contains(nodes.get(3));
    }
}
