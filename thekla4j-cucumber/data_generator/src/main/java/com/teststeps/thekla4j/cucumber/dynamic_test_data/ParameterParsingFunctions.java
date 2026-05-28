package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

/**
 * Utility class providing functions for parsing parameter strings, matching generator patterns,
 * and storing/retrieving named values for dynamic test data generation.
 */
@Log4j2
public class ParameterParsingFunctions {

  /**
   * Error message template for invalid key-value pair format.
   */
  public static String ERROR_MESSAGE =
      """
          Invalid key-value pair: {KEYVALUE} in parameter string '{INPUT}'. A parameter list is expected to be in the format: key1: value1, key2: value2, ...
          """;

  /**
   * Parses a parameter string in the format {@code key1: value1, key2: value2, ...} into a map.
   * A single value without a key is treated as the {@code default} key.
   */
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
      str -> {
        java.util.List<String> parts = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < str.length(); i++) {
          char c = str.charAt(i);
          if (c == '"') {
            inQuotes = !inQuotes;
            current.append(c);
          } else if (c == ',' && !inQuotes) {
            parts.add(current.toString());
            current = new StringBuilder();
          } else {
            current.append(c);
          }
        }
        // Match Java's split behavior: discard trailing empty segments
        String last = current.toString();
        if (!last.trim().isEmpty()) {
          parts.add(last);
        }
        return List.ofAll(parts);
      };

  private static final Function<List<String>, List<List<String>>> splitKeyValuePair =
      list -> list.map(pair -> {
        String trimmed = pair.trim();
        if (trimmed.isEmpty()) {
          return List.of(trimmed);
        }
        // Split on the first colon that is not inside quotes
        boolean inQuotes = false;
        for (int i = 0; i < trimmed.length(); i++) {
          char c = trimmed.charAt(i);
          if (c == '"') {
            inQuotes = !inQuotes;
          } else if (c == ':' && !inQuotes) {
            String key = trimmed.substring(0, i).trim();
            String rawValue = trimmed.substring(i + 1).trim();
            if (rawValue.isEmpty()) {
              return List.of(key);
            }
            return List.of(key, stripQuotes(rawValue));
          }
        }
        // No colon found — treat as single value
        return List.of(stripQuotes(trimmed));
      });

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


  private static String stripQuotes(String value) {
    if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
      return value.substring(1, value.length() - 1);
    }
    return value;
  }

  private ParameterParsingFunctions() {
    // utility class
  }

}
