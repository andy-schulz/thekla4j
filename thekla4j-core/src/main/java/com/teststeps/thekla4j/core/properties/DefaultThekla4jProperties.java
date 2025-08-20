package com.teststeps.thekla4j.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import io.vavr.control.Option;

public interface DefaultThekla4jProperties {

  /**
   * Get the property
   *
   * @return the property
   */
  PropertyElement property();

  /**
   * Get the value of the property
   *
   * @return the value of the property
   */
  String value();

  /**
   * Get the value of the property as an Integer
   *
   * @return the value of the property as an Integer
   */
  Integer asInteger();


  /**
   * Get the value of the property as an option
   *
   * @return the value of the property as an option
   */
  Option<String> optionValue();

}
