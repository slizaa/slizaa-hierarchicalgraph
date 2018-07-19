package org.slizaa.hierarchicalgraph.graphdb.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;

import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.slizaa.core.boltclient.IBoltClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NodeIdFinder {

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getDoGetMapperMethod(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (m:Method {fqn: 'java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)'}) Return id(m)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getAssignmentClassFile(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (r:Resource {fqn: 'org/mapstruct/ap/internal/model/common/Assignment.class'}) Return id(r)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getSetterWrapperForCollectionsAndMapsWithNullCheckType(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (t:Type {fqn: 'org.mapstruct.ap.internal.model.assignment.SetterWrapperForCollectionsAndMapsWithNullCheck'}) Return id(t)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param cypherQuery
   * @return
   */
  private static long requestId(IBoltClient boltClient, String cypherQuery) {

    try {
      return checkNotNull(boltClient).syncExecCypherQuery(checkNotNull(cypherQuery)).single().get(0).asLong();
    } catch (NoSuchRecordException e) {
      throw new RuntimeException(e);
    }
  }
}
