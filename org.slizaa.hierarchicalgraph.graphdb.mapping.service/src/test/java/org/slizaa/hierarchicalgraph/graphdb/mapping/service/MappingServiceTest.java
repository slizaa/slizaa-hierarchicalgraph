package org.slizaa.hierarchicalgraph.graphdb.mapping.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import org.slizaa.hierarchicalgraph.core.algorithms.AdjacencyMatrix;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.hierarchicalgraph.graphdb.testfwk.TestDB;
import org.slizaa.hierarchicalgraph.graphdb.testfwk.mapping.SimpleJTypeMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MappingServiceTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /**
   * <p>
   * </p>
   * 
   * @throws ClassNotFoundException
   */
  @Test
  public void testMappingService() throws ClassNotFoundException {

    // TODO: WORK-AROUND FOR SECURITY-EXCEPTION-BUG
    Class<?> clazz = this.getClass().getClassLoader().loadClass("org.eclipse.core.runtime.SubMonitor");

    IMappingService mappingService = MappingFactory.createMappingServiceForStandaloneSetup();

    HGRootNode rootNode = mappingService.convert(new SimpleJTypeMappingProvider(),
        _boltClientConnection.getBoltClient(), null);

    assertThat(rootNode.getChildren()).hasSize(2);

    assertThat(rootNode.getChildren().get(0).getChildren()).hasSize(5);
    assertThat(rootNode.getChildren().get(1).getChildren()).hasSize(36);

    int[][] matrix = AdjacencyMatrix.computeAdjacencyMatrix(rootNode.getChildren().get(1).getChildren());

    AdjacencyMatrix.printMatrix(matrix);
  }
}
