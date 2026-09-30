package com.teststeps.thekla4j.browser.core.network;

import java.util.regex.Pattern;

/**
 * Matches a url against a url pattern.
 * <p>
 * The pattern is matched against the whole url. {@code *} matches any sequence of characters, every other character is
 * taken literally, so {@code https://api.example.com/v1/*} matches every call below that path. The pattern {@code *}
 * matches every url.
 * <p>
 * The matching is done here rather than handed to WebDriver BiDi, because the recorder subscribes to all network events
 * and filters them, so recording and filtering the recorded calls use the very same rule.
 */
public class UrlPatternMatch {

  private UrlPatternMatch() {
    // prevent instantiation of utility class
  }

  /**
   * Check whether a url matches a url pattern
   *
   * @param urlPattern - the pattern, where * matches any sequence of characters
   * @param url        - the url to check
   * @return true if the url matches the pattern
   */
  public static boolean matches(String urlPattern, String url) {
    if (urlPattern == null || url == null)
      return false;

    if (urlPattern.isBlank() || "*".equals(urlPattern))
      return true;

    return Pattern.compile(toRegex(urlPattern)).matcher(url).matches();
  }

  private static String toRegex(String urlPattern) {
    StringBuilder regex = new StringBuilder();
    int from = 0;
    int star = urlPattern.indexOf('*');

    while (star >= 0) {
      if (star > from)
        regex.append(Pattern.quote(urlPattern.substring(from, star)));
      regex.append(".*");
      from = star + 1;
      star = urlPattern.indexOf('*', from);
    }

    if (from < urlPattern.length())
      regex.append(Pattern.quote(urlPattern.substring(from)));

    return regex.toString();
  }
}
