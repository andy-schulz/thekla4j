package com.teststeps.thekla4j.commons;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PropertyTest {


  @Test
  public void loadPropertyFromFiles() {

    PropertyElement propertyElement = PropertyElement.of("thekla4j.testPropertyFile", "defaultValue");

    String property = Thekla4jProperty.of(propertyElement);

    assertThat("property is load from file", property, equalTo("propertyValueSetInFile"));

  }

  @Test
  public void loadPropertyFromSystem() {

    System.setProperty("thekla4j.testPropertySystem", "propertyValueSetInSystem");

    PropertyElement propertyElement = PropertyElement.of("thekla4j.testPropertySystem", "defaultValue");

    String property = Thekla4jProperty.of(propertyElement);

    assertThat("property is load from system", property, equalTo("propertyValueSetInSystem"));

  }

  @Test
  public void loadPropertyDefault() {

    PropertyElement propertyElement = PropertyElement.of("thekla4j.testPropertyDefault", "defaultValue");

    String property = Thekla4jProperty.of(propertyElement);

    assertThat("property is load from default", property, equalTo("defaultValue"));

  }
}
