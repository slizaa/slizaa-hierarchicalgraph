package org.slizaa.hierarchicalgraph.core.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class TestModelCreator {

    /**
     * 
     * @return
     */
    public static List<HGNode> createDummyModel() {

        HGNode p1 = new Node("p1");
        HGNode p2 = new Node("p2");
        HGNode p3 = new Node("p3");
        HGNode p4 = new Node("p4");

        new Dependency(p1, p2, 13);
        new Dependency(p2, p3, 57);
        new Dependency(p3, p4, 45);
        new Dependency(p4, p3, 3);

        return new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
    }
}
