package com.teststeps.thekla4j.commons.properties;

public class PropertyElement {

  private final String name;
  private final String defaultValue;

  /**
   * get the name of the property
   * @return the name of the property
   */
  public String name() {
    return name;
  }

  /**
   * get the default value of the property
   * @return the default value of the property
   */
  public String defaultValue() {
    return defaultValue;
  }

  /**
   * create a new PropertyElement with the given name and default value
   * @param name the name of the property
   * @param defaultValue the default value of the property
   * @return the new PropertyElement
   */
  public static PropertyElement of(String name, String defaultValue) {
    return new PropertyElement(name, defaultValue);
  }

  private PropertyElement(String name, String value) {
    this.name = name;
    this.defaultValue = value;
  }
}
