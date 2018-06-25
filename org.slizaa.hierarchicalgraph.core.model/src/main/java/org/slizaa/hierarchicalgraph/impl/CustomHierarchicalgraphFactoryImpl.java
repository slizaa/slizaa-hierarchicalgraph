/**
 */
package org.slizaa.hierarchicalgraph.impl;

import org.slizaa.hierarchicalgraph.DefaultDependencySource;
import org.slizaa.hierarchicalgraph.DefaultNodeSource;
import org.slizaa.hierarchicalgraph.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.HGCoreDependency;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.HierarchicalgraphFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CustomHierarchicalgraphFactoryImpl extends HierarchicalgraphFactoryImpl
    implements HierarchicalgraphFactory {

  /**
   * <p>
   * Creates a new instance of type {@link CustomHierarchicalgraphFactoryImpl}.
   * </p>
   */
  public CustomHierarchicalgraphFactoryImpl() {
    super();
  }

  @Override
  public HGNode createHGNode() {
    HGNodeImpl hgNode = new ExtendedHGNodeImpl();
    return hgNode;
  }

  @Override
  public HGRootNode createHGRootNode() {
    HGRootNodeImpl hgRootNode = new ExtendedHGRootNodeImpl();
    return hgRootNode;
  }

  @Override
  public DefaultNodeSource createDefaultNodeSource() {
    DefaultNodeSourceImpl defaultNodeSource = new ExtendedDefaultNodeSourceImpl();
    return defaultNodeSource;
  }

  @Override
  public DefaultDependencySource createDefaultDependencySource() {
    DefaultDependencySourceImpl defaultDependencySource = new DefaultDependencySourceImpl();
    return defaultDependencySource;
  }

  @Override
  public HGAggregatedDependency createHGAggregatedDependency() {
    HGAggregatedDependencyImpl hgAggregatedDependency = new ExtendedHGAggregatedDependencyImpl();
    return hgAggregatedDependency;
  }

  @Override
  public HGCoreDependency createHGCoreDependency() {
    HGCoreDependencyImpl hgCoreDependency = new ExtendedHGCoreDependencyImpl();
    return hgCoreDependency;
  }

  @Override
  public HGProxyDependency createHGProxyDependency() {
    HGProxyDependencyImpl hgProxyDependency = new ExtendedHGProxyDependencyImpl();
    return hgProxyDependency;
  }
}