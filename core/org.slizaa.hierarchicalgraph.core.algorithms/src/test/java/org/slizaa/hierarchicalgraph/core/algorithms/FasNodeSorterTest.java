package org.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

public class FasNodeSorterTest {

    @Test
    public void sortNodes() {

        //
        List<HGNode> nodes = TestModelCreator.createDummyModel();

        //
        INodeSorter nodeSorter = GraphUtils.createFasNodeSorter();

        //
        INodeSorter.SortResult sortResult = nodeSorter.sort(nodes);

        //
        assertThat(sortResult.getUpwardsDependencies()).hasSize(1);
    }
}
