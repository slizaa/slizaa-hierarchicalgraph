package org.slizaa.hierarchicalgraph.spi;

import org.slizaa.hierarchicalgraph.HGNode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IAutoExpandInterceptor {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  boolean preventAutoExpansion(HGNode node);
}
