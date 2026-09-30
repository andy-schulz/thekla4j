package com.teststeps.thekla4j.browser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.core.network.UrlPatternMatch;
import org.junit.jupiter.api.Test;

public class TestUrlPatternMatch {

  private static final String URL = "https://api.example.com/v1/user?id=7";

  @Test
  public void starMatchesEveryUrl() {
    assertThat("star matches", UrlPatternMatch.matches("*", URL), equalTo(true));
  }

  @Test
  public void blankPatternMatchesEveryUrl() {
    assertThat("blank matches", UrlPatternMatch.matches("", URL), equalTo(true));
    assertThat("whitespace matches", UrlPatternMatch.matches("   ", URL), equalTo(true));
  }

  @Test
  public void exactUrlMatches() {
    assertThat("exact url matches", UrlPatternMatch.matches(URL, URL), equalTo(true));
  }

  @Test
  public void trailingWildcardMatchesEverythingBelowThePath() {
    assertThat("path prefix matches", UrlPatternMatch.matches("https://api.example.com/v1/*", URL), equalTo(true));
    assertThat("other host does not match", UrlPatternMatch.matches("https://api.other.com/v1/*", URL), equalTo(false));
  }

  @Test
  public void wildcardInTheMiddleMatches() {
    assertThat("wildcard in the middle matches",
      UrlPatternMatch.matches("https://*/v1/user?id=7", URL), equalTo(true));
  }

  @Test
  public void severalWildcardsMatch() {
    assertThat("several wildcards match", UrlPatternMatch.matches("https://*/v1/*", URL), equalTo(true));
  }

  @Test
  public void aPatternWithoutWildcardDoesNotMatchAPrefixOnly() {
    assertThat("partial url does not match",
      UrlPatternMatch.matches("https://api.example.com/v1", URL), equalTo(false));
  }

  @Test
  public void regexCharactersInThePatternAreTakenLiterally() {
    assertThat("the question mark is literal, not a regex quantifier",
      UrlPatternMatch.matches("https://api.example.com/v1/user?id=7", URL), equalTo(true));
    assertThat("the dot is literal, not any character",
      UrlPatternMatch.matches("https://api.example.com/v1/user?id=7", "https://api.example.com/v1/userXid=7"),
      equalTo(false));
  }

  @Test
  public void nullIsNeverAMatch() {
    assertThat("null pattern", UrlPatternMatch.matches(null, URL), equalTo(false));
    assertThat("null url", UrlPatternMatch.matches("*", null), equalTo(false));
  }
}
