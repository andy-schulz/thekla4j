package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredefinedGeneratorTest {

  @Test
  public void testRandomStringDefaultLength() {
    HashMap<String, String> data = HashMap.of("default", "16");
    Try<String> result = PredefinedGeneratorFunctions.randomString.run(data);
    assertTrue(result.isSuccess());
    assertEquals(16, result.get().length());
  }

  @Test
  public void testRandomStringWithPrefix() {
    HashMap<String, String> data = HashMap.of("length", "16", "prefix", "test");
    Try<String> result = PredefinedGeneratorFunctions.randomString.run(data);
    assertTrue(result.isSuccess());
    assertTrue(result.get().startsWith("test"));
    assertEquals(16, result.get().length());
  }

  @Test
  public void testRandomStringWithSpecialChars() {
    HashMap<String, String> data = HashMap.of("length", "16", "specialChars", "true");
    Try<String> result = PredefinedGeneratorFunctions.randomString.run(data);
    assertTrue(result.isSuccess());
    assertEquals(16, result.get().length());
    assertTrue(result.get().matches(".*[!@#$%^&*()\\-_=+\\[\\]}|;:,.<>?].*"));
  }

  @Test
  public void testRandomStringInvalidLength() {
    HashMap<String, String> data = HashMap.of("length", "abc");
    Try<String> result = PredefinedGeneratorFunctions.randomString.run(data);
    assertTrue(result.isFailure());
    assertEquals("Generate random string failed: length must be an integer. => e.g. length: 16 => actual: {length: abc}", result.getCause().getMessage());
  }

  @Test void testEmptyParameters() {
    HashMap<String, String> data = HashMap.empty();
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      PredefinedGeneratorFunctions.randomString.run(data).get();
    });
    assertEquals("Generate random string failed: no default string length is given: e.g. randomString{length: 16} or randomString{16} => actual: randomString{}", exception.getMessage());
  }

  @Test
  public void testRandomStringPrefixLongerThanLength() {
    HashMap<String, String> data = HashMap.of("length", "4", "prefix", "toolongprefix");
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      PredefinedGeneratorFunctions.randomString.run(data).get();
    });
    assertEquals("Generate random string failed: total length must be greater than prefix length. Prefix: toolongprefix( Length: 13), Length: 4", exception.getMessage());
  }
}
