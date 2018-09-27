package org.slizaa.hierarchicalgraph.graphdb.testfwk;

import com.google.common.io.Files;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class PredefinedDatabaseDirectoryRule implements TestRule {

  //
  private File parent;

  private InputStream inputStream;

  /**
   *
   * @param inputStream
   */
  public PredefinedDatabaseDirectoryRule(InputStream inputStream, File parent) {
    this.inputStream = checkNotNull(inputStream);
    unzipDatabase(parent, inputStream);
  }

  /**
   *
   * @param base
   * @param description
   * @return
   */
  @Override
  public Statement apply(final Statement base, final Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {



        base.evaluate();
        // TODO
        // databaseDirectory.delete();
      }
    };
  }

//  /**
//   *
//   * @param zippedDatabaseStream
//   * @return
//   */
//  private static File unzipDatabase(InputStream zippedDatabaseStream) {
//
//    //
//    checkNotNull(zippedDatabaseStream);
//
//    File databaseDirectory = Files.createTempDir();
//
//    //
//    if (!databaseDirectory.exists()) {
//      try {
//        unzip(zippedDatabaseStream, new File(databaseDirectory, "databases/graph.db"));
//      } catch (Exception e) {
//        throw new RuntimeException(e);
//      }
//    }
//
//    //
//    return databaseDirectory;
//  }

  /**
   *
   * @param parentDirectory
   * @param zippedDatabaseStream
   * @return
   */
  private static String unzipDatabase(File parentDirectory, InputStream zippedDatabaseStream) {

    //
    checkNotNull(parentDirectory);
    checkNotNull(zippedDatabaseStream);

    //
//    if (!parentDirectory.exists()) {
      try {
        unzip(zippedDatabaseStream, new File(parentDirectory, "databases/graph.db"));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
//    }

    //
    return parentDirectory.getAbsolutePath();
  }

  /**
   * <p>
   * </p>
   *
   * @param inputStream
   * @param folder
   */
  private static void unzip(InputStream inputStream, File folder) {

    checkNotNull(inputStream);
    checkNotNull(folder);

    byte[] buffer = new byte[1024];

    try {

      // create output directory is not exists
      if (!folder.exists()) {
        folder.mkdir();
      }

      // get the zip file content
      try (ZipInputStream zis = new ZipInputStream(inputStream)) {
        // get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

          if (!ze.isDirectory()) {

            String fileName = ze.getName();
            File newFile = new File(folder + File.separator + fileName);

            // create all non exists folders
            // else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
              int len;
              while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
              }
            }
          }
          ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}