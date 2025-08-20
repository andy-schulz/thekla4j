package com.teststeps.thekla4j.core.properties;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jFunctions.helpText;
import static com.teststeps.thekla4j.core.properties.TempFolderUtil.baseTempDir;
import static com.teststeps.thekla4j.core.properties.TempFolderUtil.tempDir;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Option;

public enum DefaultThekla4jCoreProperties implements DefaultThekla4jProperties {

  /**
   * s
   * absolute path to the used temp directory
   */
  TEMP_DIR_PATH(PropertyElement.of("thekla4j.tempDir.path", tempDir.get(), "temp directory path")),
  TEMP_DIR_BASE_PATH(PropertyElement.of("thekla4j.tempDir.base.path", baseTempDir.get(), "temp directory path")),
  SEE_WAIT_FACTOR(PropertyElement.of("thekla4j.core.see.wait.factor", Option.of("1"), "wait factor for see activity")),
  RETRY_WAIT_FACTOR(PropertyElement.of("thekla4j.core.retry.wait.factor", Option.of("1"), "wait factor for retry activity"));

  final PropertyElement property;

  /**
   * {@inheritDoc}
   */
  @Override
  public PropertyElement property() {
    return property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String value() {
    return Thekla4jProperty.of(property)
        .getOrElseThrow(() -> new RuntimeException("Property not found: " + property.name() + ". Its a framework problem."));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Option<String> optionValue() {
    return Thekla4jProperty.of(property);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer asInteger() {
    return Thekla4jProperty.asInteger(property)
        .getOrElseThrow(x -> new RuntimeException("Error getting Integer for Property: " + property.name(), x));
  }


  /**
   * Get the help text for all properties
   *
   * @return the help text for all properties
   */
  public static String help() {
    // iterate over all enums and return the help text
    return helpText.apply(DefaultThekla4jCoreProperties.values());
  }

  DefaultThekla4jCoreProperties(PropertyElement property) {
    this.property = property;
  }
}
