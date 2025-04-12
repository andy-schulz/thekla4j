package com.teststeps.thekla4j.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Option;

import static com.teststeps.thekla4j.core.properties.DefaultThekla4jFunctions.helpText;
import static com.teststeps.thekla4j.core.properties.TempFolderUtil.tempDir;

public enum DefaultThekla4jCoreProperties implements DefaultThekla4jProperties {

  /**
   * absolute path to the used temp directory
   */
  TEMP_DIR_PATH(PropertyElement.of("thekla4j.tempDir.path", tempDir.get(), "temp directory path"));

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
