package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Option;

/**
 * Parses generator call syntax using brace-counting instead of regex character whitelists.
 * Handles quoted strings and nested braces.
 *
 * <p>A generator call has the form {@code name{params}} where {@code name} is alphanumeric
 * and {@code params} is everything between the balanced braces. The call must start at the
 * beginning of the input string.
 */
public class GeneratorCallParser {

  /**
   * Represents a parsed generator call with the generator name and parameter string.
   */
  public record GeneratorCall(String name, String parameterString) {
  }

  /**
   * Parse a generator call from the beginning of the input string.
   *
   * @param input the input string to parse
   * @return the parsed generator call, or {@code Option.none()} if the input doesn't start with a generator call
   */
  public static Option<GeneratorCall> parse(String input) {
    if (input == null || input.isEmpty()) {
      return Option.none();
    }

    int nameEnd = 0;
    while (nameEnd < input.length() && isAlphanumeric(input.charAt(nameEnd))) {
      nameEnd++;
    }

    if (nameEnd == 0 || nameEnd >= input.length() || input.charAt(nameEnd) != '{') {
      return Option.none();
    }

    String name = input.substring(0, nameEnd);

    // Find matching } using brace depth counting and quote tracking
    int depth = 1;
    boolean inQuotes = false;
    int i = nameEnd + 1;

    while (i < input.length() && depth > 0) {
      char c = input.charAt(i);
      if (c == '"') {
        inQuotes = !inQuotes;
      } else if (!inQuotes) {
        if (c == '{') depth++;
        else if (c == '}') depth--;
      }
      i++;
    }

    if (depth != 0) {
      return Option.none();
    }

    String parameterString = input.substring(nameEnd + 1, i - 1);
    return Option.of(new GeneratorCall(name, parameterString));
  }

  private static boolean isAlphanumeric(char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
  }

  private GeneratorCallParser() {
    // utility class
  }
}
