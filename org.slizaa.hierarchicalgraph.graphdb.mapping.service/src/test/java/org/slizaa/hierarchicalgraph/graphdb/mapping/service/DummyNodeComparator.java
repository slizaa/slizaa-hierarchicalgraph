package org.slizaa.hierarchicalgraph.graphdb.mapping.service;


import org.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;

public class DummyNodeComparator implements INodeComparator {

  @Override
  public int category(Object element) {
    return 0;
  }

  @Override
  public int compare(Object e1, Object e2) {
    return 0;
  }
}
