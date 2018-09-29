package org.slizaa.hierarchicalgraph.graphdb.testfwk;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTest {

  @ClassRule
  public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

  @Test
  public void testTest() {
  
    //
    StatementResult result =  graphDatabaseSetup.getBoltClient().syncExecCypherQuery("MATCH (node) RETURN count(node)");

    assertThat(result.single().get("count(node)").asInt()).isEqualTo(40549);
  }
}
