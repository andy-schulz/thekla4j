package com.teststeps.thekla4j.browser.playwright.helper;

import io.vavr.Function3;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestFunctions {
  /**
   * Returns a Predicate&lt;String&gt; that matches a string against a template containing
   * {@code {X}} and {@code {Y}} placeholders, allowing ±1 pixel tolerance on both coordinates.
   *
   * <p>Example:
   * <pre>
   * coordinatesWithinOnePx.apply(50, 75, "Start Drawing on Canvas with Mouse. X: {X} Y: {Y}")
   * // accepts "...X: 50 Y: 75", "...X: 51 Y: 76", "...X: 49 Y: 74", etc.
   * </pre>
   */
  public static final Function3<Integer, Integer, String, Predicate<String>> coordinatesWithinOnePx =
      (expectedX, expectedY, template) -> {
        // Split the original template on {X} first, then split the second part on {Y}.
        // Each literal fragment is individually escaped, then joined with digit capture groups.
        int xIdx = template.indexOf("{X}");
        int yIdx = template.indexOf("{Y}");
        String beforeX = template.substring(0, xIdx);
        String betweenXY = template.substring(xIdx + 3, yIdx);
        String afterY = template.substring(yIdx + 3);

        String regex = Pattern.quote(beforeX) + "(\\d+)" + Pattern.quote(betweenXY) + "(\\d+)" + Pattern.quote(afterY);

        Pattern pattern = Pattern.compile(regex);

        return text -> {
          Matcher m = pattern.matcher(text);
          if (!m.matches()) return false;
          int actualX = Integer.parseInt(m.group(1));
          int actualY = Integer.parseInt(m.group(2));
          return Math.abs(actualX - expectedX) <= 1 && Math.abs(actualY - expectedY) <= 1;
        };
      };
}
