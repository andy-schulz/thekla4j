package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import static com.teststeps.thekla4j.cucumber.dynamic_test_data.ParameterParsingFunctions.parseParameterStringToMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vavr.collection.Map;
import org.junit.jupiter.api.Test;

public class ParameterParsingFunctionTest {

  @Test
  public void testDefault() {

    Map<String, String> map = parseParameterStringToMap.apply("defaultValue");

    assertThat("default value is set", !map.get("default").isEmpty());
    assertThat("default value is set", map.get("default").get().equals("defaultValue"));

  }

  @Test
  public void testEmpty() {

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> parseParameterStringToMap.apply(""));

    assertThat("passing empty parameter String shall throw an error", thrown.getMessage(),
      equalTo("Parameter string of generator function is null or empty => genFunction{}"));
  }

  @Test
  public void testingOneNamedParameter() {

    Map<String, String> map = parseParameterStringToMap.apply("name: John");

    assertThat("name is set", !map.get("name").isEmpty());
    assertThat("name is set", map.get("name").get().equals("John"));
  }

  @Test
  public void testingOneNamedParameterWithTrailingSeparator() {

    Map<String, String> map = parseParameterStringToMap.apply("name: John,");

    assertThat("name is set", !map.get("name").isEmpty());
    assertThat("name is set", map.get("name").get().equals("John"));
  }

  @Test
  public void testingOneNamedParameterWithLeadingSeparator() {

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> parseParameterStringToMap.apply(",name:John"));


    assertThat("exception is thrown", thrown.getMessage(),
      equalTo(
        "Invalid key-value pair:  in parameter string ',name:John'. A parameter list is expected to be in the format: key1: value1, key2: value2, ...\n"));
  }

  @Test
  public void testingOneNamedParameterWithSpaces() {

    Map<String, String> map = parseParameterStringToMap.apply("name:John Doe");

    assertThat("name is set", !map.get("name").isEmpty());
    assertThat("name is set", map.get("name").get().equals("John Doe"));
  }

  @Test
  public void testingOneNamedParameterWithSpacesAfterEqualSign() {

    Map<String, String> map = parseParameterStringToMap.apply("name: John");

    assertThat("name is set", !map.get("name").isEmpty());
    assertThat("name is set", map.get("name").get().equals("John"));
  }

  @Test
  public void testingTwoNamedParameter() {

    Map<String, String> map = parseParameterStringToMap.apply("name: John, age: 25");

    assertThat("name is set", !map.get("name").isEmpty());
    assertThat("name is set", map.get("name").get().equals("John"));

    assertThat("age is set", !map.get("age").isEmpty());
    assertThat("age is set", map.get("age").get().equals("25"));
  }

  @Test
  public void testingTwoNamedParameterWithMissingValue() {

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> parseParameterStringToMap.apply("name: John, age: "));

    assertThat("passing invalid parameter string with throw an error", thrown.getMessage(),
      equalTo(
        "Invalid key-value pair: age in parameter string 'name: John, age: '. A parameter list is expected to be in the format: key1: value1, key2: value2, ...\n"));
  }

  @Test
  public void testingTwoNamedParametersWithEmptyKey() {

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> parseParameterStringToMap.apply("name: John,: Test"));

    assertThat("passing invalid parameter string throws an error", thrown.getMessage(),
      equalTo("Key cannot be empty in string: name: John,: Test"));
  }

  @Test
  public void testingQuotedValueWithCommas() {
    Map<String, String> map = parseParameterStringToMap.apply("date: 2025-01-01, format: \"EEEE, d. MMMM yyyy\"");

    assertThat("date is set", map.get("date").get(), equalTo("2025-01-01"));
    assertThat("format is set with commas preserved", map.get("format").get(), equalTo("EEEE, d. MMMM yyyy"));
  }

  @Test
  public void testingQuotedValueWithColons() {
    Map<String, String> map = parseParameterStringToMap.apply("pattern: \"HH:mm:ss\"");

    assertThat("pattern is set with colons preserved", map.get("pattern").get(), equalTo("HH:mm:ss"));
  }

  @Test
  public void testingMixedQuotedAndUnquotedParams() {
    Map<String, String> map = parseParameterStringToMap.apply("name: John, format: \"yyyy-MM-dd, HH:mm\", age: 25");

    assertThat("name is set", map.get("name").get(), equalTo("John"));
    assertThat("format is set", map.get("format").get(), equalTo("yyyy-MM-dd, HH:mm"));
    assertThat("age is set", map.get("age").get(), equalTo("25"));
  }

  @Test
  public void testingQuotedDefaultValue() {
    Map<String, String> map = parseParameterStringToMap.apply("\"hello, world\"");

    assertThat("default value preserves commas", map.get("default").get(), equalTo("hello, world"));
  }
}
