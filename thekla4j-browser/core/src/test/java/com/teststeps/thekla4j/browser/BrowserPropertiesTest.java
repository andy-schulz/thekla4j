package com.teststeps.thekla4j.browser;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class BrowserPropertiesTest {

  @BeforeAll
  public static void setup() {
    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void checkDefaultBrowserProperties() {
    String highlightElements = HIGHLIGHT_ELEMENTS.value();
    String slowDownExecution = SLOW_DOWN_EXECUTION.value();
    String slowDownTime = SLOW_DOWN_TIME.value();

    assertThat("default property 'highlight element' is true", highlightElements, equalTo("true"));
    assertThat("default property 'slow down execution' is false", slowDownExecution, equalTo("false"));
    assertThat("default property 'slow down time' is 1 second", slowDownTime, equalTo("1"));
  }

  @Test
  public void checkAutoScrollProperties() {
    String autoScrollEnabled = AUTO_SCROLL_ENABLED.value();
    String autoScrollVertical = AUTO_SCROLL_VERTICAL.value();

    assertThat("default property 'auto scroll enabled' is false", autoScrollEnabled, equalTo("false"));
    assertThat("default property 'auto scroll vertical' is center", autoScrollVertical, equalTo("center"));
  }

  @Test
  public void checkScreenshotProperties() {
    String screenshotRelativePath = SCREENSHOT_RELATIVE_PATH.value();
    String screenshotAbsolutePath = SCREENSHOT_ABSOLUTE_PATH.value();

    assertThat("default property 'screenshot relative path' is empty", screenshotRelativePath, equalTo(""));
    assertThat("default property 'screenshot absolute path' is user directory", screenshotAbsolutePath, equalTo(System.getProperty("user.dir")));
  }

  @Test
  public void checkHelpText() {
    String helpText = DefaultThekla4jBrowserProperties.help();

    // quote the static parts of the expected help text and ignore the absolut screenshot path which is set to user.dir
    String expectedHelpText =
        """
            \\Qthekla4j.browser.autoScroll.enabled:              Possible values: true, false (default: false)
            thekla4j.browser.autoScroll.vertical:             Possible values: top, center, bottom (default: center)
            thekla4j.browser.config:                          The browser configuration to use (default: None)
            thekla4j.browser.element.wait.factor:             multiplier for the wait time for elements. Default is 1.0, which means no scaling. (default: 1)
            thekla4j.browser.highlightElements:               Possible values: true, false (default: true)
            thekla4j.browser.screenshot.absolutePath:         Absolute path to store the screenshots (default: \\E.*\\Q)
            thekla4j.browser.screenshot.relativePath:         Relative project path to store the screenshots (default: )
            thekla4j.browser.slowDownExecution:               Possible values: true, false (default: false)
            thekla4j.browser.slowDownTimeInSeconds:           Time in seconds to slow down the execution (default: 1)\\E
            """
            .strip();

    assertThat("help text is correct", helpText, matchesPattern(expectedHelpText));
  }
}
