package com.teststeps.thekla4j.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.collection.List;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DefaultThekla4jFunctions {


  /**
   * Get the help text for all properties
   */
  public static Function<DefaultThekla4jProperties[], String> helpText = (properties) -> List.of(properties)
      .map(DefaultThekla4jProperties::property)
      .sortBy(PropertyElement::name)
      .map(property -> String.format("%-50s", property.name() + ": ") + property.helpText() + " (default: " + property.defaultValue()
          .getOrElse("None") + ")")
      .mkString("\n");

  /**
   * Get the value of a property
   */
  public static Function<PropertyElement, String> value = (property) -> Thekla4jProperty.of(property)
      .getOrElseThrow(() -> new RuntimeException("Property not found: " + property.name() + ". Its a framework problem."));


}