package org.slizaa.hierarchicalgraph.graphdb.testfwk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestTest {

  @Rule
  public PredefinedDatabaseDirectoryRule rule = new PredefinedDatabaseDirectoryRule(TestTest.class.getResourceAsStream("/mapstruct_1-2-0-Final-db.zip"), new File("D:\\hurz"));

  @Rule
//  public Neo4jRule neo4jRule = new Neo4jRule().withConfig("dbms.directories.data", rule.getDatabaseDirectoy());
  public Neo4jRule neo4jRule = new Neo4jRule().withConfig("dbms.directories.data", "D:\\hurz");
  // C:\temp\hurz\databases\graph.db

  @Test
  public void testTest() {
  
    //
    GraphDatabaseService databaseService = neo4jRule.getGraphDatabaseService();
    
    //
    try (Transaction transaction = databaseService.beginTx()) {
      databaseService.getAllLabels().forEach(label -> System.out.println(label));
      transaction.success();
    } 
    
    System.out.println("END");
  }


}
