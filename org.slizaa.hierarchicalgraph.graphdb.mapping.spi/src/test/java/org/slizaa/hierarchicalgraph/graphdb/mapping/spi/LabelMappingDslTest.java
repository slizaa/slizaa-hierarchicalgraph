package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.OverlayPosition;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.DefaultLabelDefinition;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.LabelMappingDsl;

public class LabelMappingDslTest extends LabelMappingDsl {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testSetBaseImage() {

    //
    DefaultLabelDefinition labelDefinition = process(setBaseImage(fromClasspath("icons/final_co.png")));

    //
    assertThat(labelDefinition.getBaseImage()).isNotNull().isEqualTo(fromClasspath("icons/final_co.png"));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testSetOverlayImage() {

    //
    DefaultLabelDefinition labelDefinition = process(
        setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_RIGHT));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_LEFT));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_LEFT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_RIGHT));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_LEFT));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_LEFT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testExclusiceChoice() {

    //@formatter:off
    DefaultLabelDefinition labelDefinition = process(

      exclusiveChoice().
        when(n -> false).then(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_RIGHT)).
        when(n -> false).then(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_LEFT)).
        otherwise(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_RIGHT))
    );
    //@formatter:on

    //
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_RIGHT)).isNull();
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_LEFT)).isNull();
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_LEFT)).isNull();
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_RIGHT)).isNotNull();
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testExecuteAll() {

    //@formatter:off
    DefaultLabelDefinition labelDefinition = process(
        executeAll(
            setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_RIGHT),
            setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_LEFT),
            setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_RIGHT),
            setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_LEFT)
        )
    );
    //@formatter:on

    //
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_LEFT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_LEFT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testWhen_True() {

    //
    DefaultLabelDefinition labelDefinition = process(
        when(n -> true).then(setBaseImage(fromClasspath("icons/final_co.png"))));

    //
    assertThat(labelDefinition.getBaseImage()).isNotNull().isEqualTo(fromClasspath("icons/final_co.png"));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testWhen_False() {

    //
    DefaultLabelDefinition labelDefinition = process(
        when(n -> false).then(setBaseImage(fromClasspath("icons/final_co.png"))));

    //
    assertThat(labelDefinition.getBaseImage()).isNull();
  }

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  protected DefaultLabelDefinition process(LabelDefinitionProcessor processor) {
    DefaultLabelDefinition labelDefinition = new DefaultLabelDefinition();
    processor.processLabelDefinition(HierarchicalgraphFactory.eINSTANCE.createHGNode(), labelDefinition);
    return labelDefinition;
  }
}
