package com.teststeps.thekla4j.core.properties;

import com.teststeps.thekla4j.commons.properties.PropertyElement;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.collection.List;

import java.util.function.Function;

public class DefaultThekla4jFunctions {

  public static Function<DefaultThekla4jProperties[], String> helpText = (properties) ->
    List.of(properties)
      .map(DefaultThekla4jProperties::property)
      .map(property -> property.name() + ": " + property.helpText() + " (default: " + property.defaultValue().getOrElse("None") + ")")
    .mkString("\n");

  public static Function<PropertyElement, String> value = (property) ->
    Thekla4jProperty.of(property)
    .getOrElseThrow(() -> new RuntimeException("Property not found: " + property.name() + ". Its a framework problem."));;
}
