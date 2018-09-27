package org.slizaa.hierarchicalgraph.graphdb.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

import com.google.common.collect.Lists;

/**
 * <p>
 * https://github.com/lukas-krecan/JsonUnit
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@RunWith(value = Parameterized.class)
public class ExtendedNeo4JRemoteRepository_GetNodeLabels_Test {

  {
    CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private Function<IBoltClient, Long>       _nodeIdProvider;

  /** - */
  private List<String>                      _expectedLabels;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedNeo4JRemoteRepository_GetNodeLabels_Test}.
   * </p>
   *
   * @param nodeIdProvider
   * @param expectedLabels
   */
  public ExtendedNeo4JRemoteRepository_GetNodeLabels_Test(Function<IBoltClient, Long> nodeIdProvider,
      List<String> expectedLabels) {
    this._nodeIdProvider = checkNotNull(nodeIdProvider);
    this._expectedLabels = checkNotNull(expectedLabels);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void getNodeProperties() {

    IBoltClient boltClient = _boltClientConnection.getBoltClient();

    List<String> labels = Lists.newArrayList(boltClient.getNode(_nodeIdProvider.apply(boltClient)).labels());
    assertThat(labels).containsExactlyElementsOf(_expectedLabels);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Parameters(name = "{index}: getNodeLabels({0}) = {1}")
  public static Collection<Object[]> data() {

    Function<IBoltClient, Long> f1 = c -> NodeIdFinder.getAssignmentClassFile(c);
    Function<IBoltClient, Long> f2 = c -> NodeIdFinder.getDoGetMapperMethod(c);
    Function<IBoltClient, Long> f3 = c -> NodeIdFinder.getSetterWrapperForCollectionsAndMapsWithNullCheckType(c);

    return Arrays.asList(new Object[][] { { f1, Arrays.asList("Resource", "Binary", "ClassFile") },
        { f2, Arrays.asList("Method") }, { f3, Arrays.asList("Type", "Class") } });
  }
}
