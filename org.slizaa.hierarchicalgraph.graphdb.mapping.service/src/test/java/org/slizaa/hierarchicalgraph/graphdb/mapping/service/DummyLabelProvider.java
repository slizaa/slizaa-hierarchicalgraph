package org.slizaa.hierarchicalgraph.graphdb.mapping.service;


import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

public class DummyLabelProvider implements ILabelDefinitionProvider {

  @Override
  public ILabelDefinition getLabelDefinition(HGNode node) {
    return null;
  }
}
