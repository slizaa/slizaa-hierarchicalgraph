package org.slizaa.hierarchicalgraph.graphdb.mapping.service;

import org.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.DefaultMappingService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MappingFactory {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IMappingService createMappingServiceForStandaloneSetup() {
    return new DefaultMappingService();
  }
}
