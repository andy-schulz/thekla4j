package com.teststeps.thekla4j.browser;

import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import org.junit.jupiter.api.Test;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SCREENSHOT_ABSOLUTE_PATH;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SCREENSHOT_RELATIVE_PATH;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_EXECUTION;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.SLOW_DOWN_TIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class BrowserPropertiesTest {

  @Test
  public void checkDefaultBrowserProperties() {
    String highlightElements = Thekla4jProperty.of(HIGHLIGHT_ELEMENTS.property());
    String slowDownExecution = Thekla4jProperty.of(SLOW_DOWN_EXECUTION.property());
    String slowDownTime = Thekla4jProperty.of(SLOW_DOWN_TIME.property());

    assertThat("default property 'highlight element' is true", highlightElements, equalTo("true"));
    assertThat("default property 'slow down execution' is false", slowDownExecution, equalTo("false"));
    assertThat("default property 'slow down time' is 1 second", slowDownTime, equalTo("1"));
  }

  @Test
  public void checkAutoScrollProperties() {
    String autoScrollEnabled = Thekla4jProperty.of(AUTO_SCROLL_ENABLED.property());
    String autoScrollVertical = Thekla4jProperty.of(AUTO_SCROLL_VERTICAL.property());

    assertThat("default property 'auto scroll enabled' is false", autoScrollEnabled, equalTo("false"));
    assertThat("default property 'auto scroll vertical' is center", autoScrollVertical, equalTo("center"));
  }

  @Test
  public void checkScreenshotProperties() {
    String screenshotRelativePath = Thekla4jProperty.of(SCREENSHOT_RELATIVE_PATH.property());
    String screenshotAbsolutePath = Thekla4jProperty.of(SCREENSHOT_ABSOLUTE_PATH.property());

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
thekla4j.browser.screenshot.absolutePath: Absolute path to store the screenshots (default: C:\\Projekte\\priv\\thekla4j\\thekla4j-browser\\core)
""".strip();

    assertThat("help text is correct", helpText, equalTo(expectedHelpText));
  }
}
