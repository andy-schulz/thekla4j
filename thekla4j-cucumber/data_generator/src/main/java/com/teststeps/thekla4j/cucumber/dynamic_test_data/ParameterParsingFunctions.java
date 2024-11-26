package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class ParameterParsingFunctions {

  public static String ERROR_MESSAGE =
      """
          Invalid key-value pair: {KEYVALUE} in parameter string '{INPUT}'. A parameter list is expected to be in the format: key1: value1, key2: value2, ...
          """;

  public static Function<String, Map<String, String>> parseParameterStringToMap = input -> {

    return Option.of(input)
        .map(String::trim)
        .flatMap(ParameterParsingFunctions.checkEmptyString)
        .map(ParameterParsingFunctions.splitParameterString)
        .map(ParameterParsingFunctions.splitKeyValuePair)
        .map(ParameterParsingFunctions.checkAndSetDefault)
        .map(ParameterParsingFunctions.checkKeyValuePair.apply(input))
        .map(ParameterParsingFunctions.convertToMap)
        .getOrElseThrow(() -> new IllegalArgumentException("Parameter string of generator function is null or empty => genFunction{}"));
  };

  private static final Function<String, Option<String>> checkEmptyString =
      str -> str.isEmpty() ? Option.none() : Option.of(str);

  private static final Function<String, List<String>> splitParameterString =
      str -> List.of(str.split(","));

  private static final Function<List<String>, List<List<String>>> splitKeyValuePair =
      list -> list.map(pair -> List.of(pair.split(":")).map(String::trim));

  private static final Function<List<List<String>>, List<List<String>>> checkAndSetDefault = list -> {
    if (list.size() == 1 && list.get(0).size() == 1) {
      return List.of(List.of("default", list.get(0).get(0)));
    }
    return list;
  };

  private static final Function2<String, List<List<String>>, List<List<String>>> checkKeyValuePair =
      (inputString, list) -> list.filter(keyValue -> {

        if (keyValue.size() != 2) {
          throw new IllegalArgumentException(ERROR_MESSAGE
              .replace("{INPUT}", inputString)
              .replace("{KEYVALUE}", keyValue.collect(Collectors.joining("="))));
        }

        if (keyValue.size() == 2 && keyValue.get(0).isEmpty()) {
          throw new IllegalArgumentException("Key cannot be empty in string: " + inputString);
        }
        return true;
      });

  private static final Function<List<List<String>>, Map<String, String>> convertToMap =
      list -> list.toMap(keyValue -> keyValue.get(0), keyValue -> keyValue.get(1));


  private ParameterParsingFunctions() {
    // utility class
  }

}
