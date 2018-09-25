package org.slizaa.hierarchicalgraph.graphdb.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewNode;
import static org.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewRootNode;

import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.scanner.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.scanner.testfwk.TestDB;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtendedNeo4JBackedNodeSource_Test {

  {
    CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private HGRootNode                        _rootNode;

  /** - */
  private HGNode                            _node;

  @Before
  public void init() throws Exception {

    //
    _rootNode = createNewRootNode(() -> {

      // create the
      GraphDbRootNodeSource result = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbRootNodeSource();

      // set the repository
      result.setBoldClient(_boltClientConnection.getBoltClient());

      // return the result
      return result;
    });

    //
    _node = createNewNode(_rootNode, _rootNode, () -> {

      // create the
      GraphDbNodeSource nodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbNodeSource();

      try {

        // set the repository
        nodeSource.setIdentifier(NodeIdFinder.getDoGetMapperMethod(_boltClientConnection.getBoltClient()));

      } catch (Exception e) {
        e.printStackTrace();
        Assert.fail();
      }

      // return the result
      return nodeSource;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void testGetProperties() throws InterruptedException, ExecutionException {

    //
    EMap<String, String> properties = ((GraphDbNodeSource) _node.getNodeSource()).getProperties();

    assertThat(properties).isNotNull();
    assertThat(properties).hasSize(5);
    assertThat(properties.get("fqn"))
        .isEqualTo("java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)");
    assertThat(properties.get("visibility")).isEqualTo("private");
    assertThat(properties.get("name")).isEqualTo("doGetMapper");
    assertThat(properties.get("static")).isEqualTo("true");
    assertThat(properties.get("signature"))
        .isEqualTo("<T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;Ljava/lang/ClassLoader;)TT;");
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testGetLabels() {

    //
    EList<String> labels = ((GraphDbNodeSource) _node.getNodeSource()).getLabels();

    assertThat(labels).isNotNull();
    assertThat(labels).hasSize(1);
    assertThat(labels).contains("Method");
  }
}
