package com.teststeps.thekla4j.commons.properties;

public class PropertyElement {

  private final String name;
  private final String defaultValue;

  public String name() {
    return name;
  }

  public String defaultValue() {
    return defaultValue;
  }

  public static PropertyElement of(String name, String defaultValue) {
    return new PropertyElement(name, defaultValue);
  }

  private PropertyElement(String name, String value) {
    this.name = name;
    this.defaultValue = value;
  }
}
