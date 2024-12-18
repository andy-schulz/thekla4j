package com.teststeps.thekla4j.commons.properties;

public class PropertyElement {

  private final String name;
  private final String defaultValue;
  private final String helpText;

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
   * get the help text of the property
   * @return the help text of the property
   */
  public String helpText() {
    return helpText;
  }

  /**
   * create a new PropertyElement with the given name and default value
   * @param name the name of the property
   * @param defaultValue the default value of the property
   * @return the new PropertyElement
   */
  public static PropertyElement of(String name, String defaultValue, String helpText) {
    return new PropertyElement(name, defaultValue, helpText);
  }

  private PropertyElement(String name, String value, String helpText) {
    this.name = name;
    this.defaultValue = value;
    this.helpText = helpText;
  }
}
