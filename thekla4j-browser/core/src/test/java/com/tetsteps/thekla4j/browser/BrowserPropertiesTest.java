package com.tetsteps.thekla4j.browser;

import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import org.junit.jupiter.api.Test;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.*;
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
}
