package com.teststeps.thekla4j.browser;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SCREENSHOT_ABSOLUTE_PATH;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SCREENSHOT_RELATIVE_PATH;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

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
    System.out.println(helpText);
    String expectedHelpText =
        """
            thekla4j.browser.highlightElements: Possible values: true, false (default: true)
            thekla4j.browser.slowDownExecution: Possible values: true, false (default: false)
            thekla4j.browser.slowDownTimeInSeconds: Time in seconds to slow down the execution (default: 1)
            thekla4j.browser.autoScroll.enabled: Possible values: true, false (default: false)
            thekla4j.browser.autoScroll.vertical: Possible values: top, center, bottom (default: center)
            thekla4j.browser.screenshot.relativePath: Relative project path to store the screenshots (default: )
            thekla4j.browser.screenshot.absolutePath: Absolute path to store the screenshots (default:
            """.strip();

    String endText =
        """
            thekla4j.browser.config: The browser configuration to use (default: None)
            """.strip();

    assertThat("help text is correct", helpText, startsWith(expectedHelpText));
    assertThat("help text is correct", helpText, endsWith(endText));
  }
}
