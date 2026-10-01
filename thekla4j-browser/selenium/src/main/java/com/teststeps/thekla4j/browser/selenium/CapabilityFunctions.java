package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.BrowserDetails;
import io.vavr.Function1;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.openqa.selenium.Capabilities;

/**
 * Functions to convert selenium session capabilities into driver agnostic {@link BrowserDetails}.
 */
public class CapabilityFunctions {

  /**
   * Convert the capabilities of a selenium session into {@link BrowserDetails}.
   *
   * <p>Every value is normalised to a String at this boundary, because the capability map contains
   * selenium types ({@code Platform}, {@code Proxy}) as well as Boolean, Long and nested maps, and
   * {@code thekla4j-browser:core} must not be handed selenium instances.</p>
   *
   * <p>Nested maps are flattened with dotted keys, so that every leaf stays addressable by name:
   * {@code timeouts.pageLoad} instead of an opaque rendering of the whole {@code timeouts} map.</p>
   */
  public static final Function1<Capabilities, BrowserDetails> toBrowserDetails =
      capabilities -> BrowserDetails.of(flatten("", capabilities.asMap()));

  /**
   * Flatten a capability map into dotted keys, normalising every leaf value to a String.
   *
   * @param prefix the key prefix of the enclosing map, empty for the top level
   * @param source the map to flatten
   * @return the flattened entries
   */
  private static Map<String, String> flatten(String prefix, java.util.Map<?, ?> source) {
    return List.ofAll(source.entrySet())
        .foldLeft(LinkedHashMap.empty(),
          (details, entry) -> details.merge(
            flattenEntry(prefix + String.valueOf(entry.getKey()), entry.getValue())));
  }

  /**
   * Flatten a single capability entry, recursing into non empty nested maps.
   *
   * @param key   the already prefixed key of the entry
   * @param value the value of the entry
   * @return the flattened entries of this single entry
   */
  private static Map<String, String> flattenEntry(String key, Object value) {
    if (value instanceof java.util.Map<?, ?> nested && !nested.isEmpty())
      return flatten(key + ".", nested);

    return LinkedHashMap.of(key, String.valueOf(value));
  }

  private CapabilityFunctions() {
  }
}
