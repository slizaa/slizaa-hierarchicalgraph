package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.OverlayPosition;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.DefaultLabelDefinition;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelMappingDslTest extends AbstractLabelMappingDslTest {

  @Test
  public void testSetText() {
    DefaultLabelDefinition labelDefinition = process(setLabelText("test"), createHGNode());
    assertThat(labelDefinition.getText()).isNotNull().isEqualTo("test");
  }

  @Test
  public void testSetText_Null_1() {
    DefaultLabelDefinition labelDefinition = process(setLabelText((String) null), createHGNode());
    assertThat(labelDefinition.getText()).isNull();
  }

  @Test
  public void testSetText_Null_2() {
    DefaultLabelDefinition labelDefinition = process(setLabelText((Function<HGNode, String>) null), createHGNode());
    assertThat(labelDefinition.getText()).isNotNull().isEqualTo("<<LABEL_MAPPING_IS_NULL: (666) >>");
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testSetBaseImage() {

    //
    DefaultLabelDefinition labelDefinition = process(setBaseImage(fromClasspath("icons/final_co.png")), createHGNode());

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
        setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_RIGHT), createHGNode());
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.TOP_LEFT),
        createHGNode());
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.TOP_LEFT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_RIGHT),
        createHGNode());
    assertThat(labelDefinition.getOverlayImage(OverlayPosition.BOTTOM_RIGHT)).isNotNull()
        .isEqualTo(fromClasspath("icons/final_co.png"));

    //
    labelDefinition = process(setOverlayImage(fromClasspath("icons/final_co.png"), OverlayPosition.BOTTOM_LEFT),
        createHGNode());
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
        , createHGNode());
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
        , createHGNode());
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
        when(n -> true).then(setBaseImage(fromClasspath("icons/final_co.png"))), createHGNode());

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
        when(n -> false).then(setBaseImage(fromClasspath("icons/final_co.png"))), createHGNode());

    //
    assertThat(labelDefinition.getBaseImage()).isNull();
  }
}
