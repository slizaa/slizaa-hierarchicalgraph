/**
 *
 */
package org.slizaa.hierarchicalgraph.graphdb.mapping.cypher;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.core.boltclient.IBoltClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public interface IBoltClientAware {

  void initialize(IBoltClient boltClient, IProgressMonitor progressMonitor) throws Exception;
}
