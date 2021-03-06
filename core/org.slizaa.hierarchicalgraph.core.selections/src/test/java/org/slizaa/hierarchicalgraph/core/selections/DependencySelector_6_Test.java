package org.slizaa.hierarchicalgraph.core.selections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.slizaa.hierarchicalgraph.core.selections.fwk.DependencySelectorProbeRule;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import org.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;

public class DependencySelector_6_Test  {

  public static XmiBasedTestGraphProviderRule       _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

  public static DependencySelectorProbeRule _probe         = new DependencySelectorProbeRule(_graphProvider);

  @ClassRule
  public static RuleChain                   _chain         = RuleChain.outerRule(_graphProvider).around(_probe);
  
  @Test
  public void testWithTargetSelection() {

    //
    assertThat(_probe.dependencySelector().getUnfilteredCoreDependencies()).hasSize(50);
    assertThat(_probe.dependencySelector().getUnfilteredSourceNodes()).hasSize(22);
    assertThat(_probe.dependencySelector().getUnfilteredTargetNodes()).hasSize(6);
    
    //
    _probe.dependencySelector().setSelectedTargetNodes(_graphProvider.node(1383));
    
    //
    assertThat(_probe.dependencySelector().getFilteredCoreDependencies()).hasSize(7);
    assertThat(_probe.dependencySelector().getFilteredSourceNodes()).hasSize(7);
    assertThat(_probe.dependencySelector().getFilteredTargetNodes()).hasSize(6);

    //
    _probe.dependencySelector().setUnfilteredCoreDependencies(_probe.dependencySelector().getFilteredCoreDependencies());
    
    // the core dependencies and the source nodes have the same size as above
    assertThat(_probe.dependencySelector().getUnfilteredCoreDependencies()).hasSize(7);
    assertThat(_probe.dependencySelector().getUnfilteredSourceNodes()).hasSize(7);

    // here the 'crop' has removed the target node
    assertThat(_probe.dependencySelector().getUnfilteredTargetNodes()).hasSize(1);
  }
}
