package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;

import java.security.SecureRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PredefinedGeneratorFunctions {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]}|;:,.<>?";
  private static final SecureRandom RANDOM = new SecureRandom();

  protected static final String randomStringDescription =
    """
      randomString{length: int, prefix: string, specialChars: boolean}
      randomString{256} -> length: 256

          Generate a random string with a given prefix and length.
          length:        the generated string will have this length,             restriction: length > prefix.length,     default 8,
          prefix:        the generated string will be prefixed by this string,                                            default: empty string.""
          specialChars:  the generated string will include special characters    ( !@#$%^&*()-_=+[]}|;:,.<>? )            default: false.
      """;

  private static final Function<Map<String, String>, String> readableMap =
    map -> map.toList()
        .collect(
    Collectors.mapping(t -> t._1 + ": " + t._2,
      Collectors.joining(", ", "{", "}")));

  protected static final DataGenerator randomString = data -> {

    if(data.isEmpty())
      return Try.failure(new IllegalArgumentException("Generate random string failed: no default string length is given: e.g. randomString{length: 16} or randomString{16} => actual: randomString" + readableMap.apply(data)));

    Try<Integer> tryLength;

    if (data.keySet().contains("default")) {
      tryLength = data.get("default")
        .toTry()
        .mapTry(i -> Try.of(() -> Integer.parseInt(i))
          .getOrElseThrow(x -> new IllegalArgumentException("Generate random string failed: default value must be an integer, actual: " + readableMap.apply(data), x)));
    } else {
      tryLength = data.get("length")
        .toTry()
        .mapTry(i -> Try.of(() -> Integer.parseInt(i))
          .getOrElseThrow(() -> new IllegalArgumentException("Generate random string failed: length must be an integer. => e.g. length: 16 => actual: " +
            readableMap.apply(data))));;
    }

    if (tryLength.isFailure()) {
      return Try.failure(tryLength.getCause());
    }

    int length = tryLength.get();

    String prefix = data.get("prefix").getOrElse("");
    String CHARS = data.get("specialChars").getOrElse("").equals("true") ? SPECIAL_CHARACTERS : CHARACTERS;

    if (prefix.length() > length) {
      return Try.failure(new IllegalArgumentException("Generate random string failed: total length must be greater than prefix length. Prefix: " +
        prefix + "( Length: " + prefix.length() + "), Length: " + length));
    }

    int len = CHARS.length();
    List<Character> randomChars = List.range(0, length - prefix.length())
      .map(i -> CHARS.charAt(RANDOM.nextInt(len)));

    return Try.success(randomChars.foldLeft(prefix, (acc, c) -> acc + c));
  };

  private PredefinedGeneratorFunctions() {
    //utility class
  }
}
