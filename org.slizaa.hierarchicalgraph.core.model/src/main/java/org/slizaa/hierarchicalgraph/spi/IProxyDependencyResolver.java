package org.slizaa.hierarchicalgraph.spi;

import org.slizaa.hierarchicalgraph.HGProxyDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IProxyDependencyResolver {

  /**
   * <p>
   * </p>
   *
   * @param dependencyToResolve
   */
  IProxyDependencyResolverJob resolveProxyDependency(HGProxyDependency dependencyToResolve);

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   *
   */
  public static interface IProxyDependencyResolverJob {

    /**
     * <p>
     * </p>
     *
     */
    void waitForCompletion();
  }
}
