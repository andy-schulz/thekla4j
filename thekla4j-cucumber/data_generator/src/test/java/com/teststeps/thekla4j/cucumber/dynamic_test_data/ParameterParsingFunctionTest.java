package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static com.teststeps.thekla4j.cucumber.dynamic_test_data.ParameterParsingFunctions.parseParameterStringToMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  public void jsonTest() {

    String newJson = """
        {
          "userId":"123",
          "userName":"John Doe",
          "details": {
            "name": "John",
            "age": 25
          }
        }
        """;

    String newJson2 = "value1";

    HashMap<String, String> map = HashMap.of("TEST", newJson, "TEST2", newJson2);

    String props = "TEST.details";
//    String props = "TEST2";

    List<String> test = List.of(props.split("\\."));

    String key = test.head();
    List<String> attributes = test.tail();

    Object elem = attributes.foldLeft((Object) new JSONObject(map.get(key).get()), (json, k) -> {
      if (json instanceof JSONObject j) {
        Object e = j.get(k);
        return e;
      }

      return json;
    });

    System.out.println(elem);

  }
}
