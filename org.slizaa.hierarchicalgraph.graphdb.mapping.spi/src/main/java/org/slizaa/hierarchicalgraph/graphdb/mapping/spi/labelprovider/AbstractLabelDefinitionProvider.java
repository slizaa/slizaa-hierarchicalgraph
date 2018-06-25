package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractLabelDefinitionProvider extends LabelMappingDsl implements ILabelDefinitionProvider {

  /** - */
  private LabelDefinitionProcessor _processor;

  /**
   * {@inheritDoc}
   */
  @Override
  public final ILabelDefinition getLabelDefinition(HGNode node) {

    //
    DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();

    //
    processor().processLabelDefinition(node, defaultLabelDefinition);

    //
    return defaultLabelDefinition;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract LabelDefinitionProcessor createLabelDefinitionProcessor();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private LabelDefinitionProcessor processor() {

    //
    if (this._processor == null) {
      this._processor = createLabelDefinitionProcessor();
      checkNotNull(this._processor);
    }

    //
    return this._processor;
  }
}
