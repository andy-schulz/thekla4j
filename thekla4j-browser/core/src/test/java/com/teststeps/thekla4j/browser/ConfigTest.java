package com.teststeps.thekla4j.browser;

import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.utils.yaml.YAML;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConfigTest {

  @Test
  public void toStringBrowserName() {

    System.out.println(YAML.jStringify(BrowserName.CHROME));

    assertThat("Chrome", YAML.jStringify(BrowserName.CHROME), equalTo("\"Chrome\"\n"));
    assertThat("Chromium", YAML.jStringify(BrowserName.CHROMIUM), equalTo("\"Chromium\"\n"));
    assertThat("Firefox", YAML.jStringify(BrowserName.FIREFOX), equalTo("\"Firefox\"\n"));
    assertThat("Edge", YAML.jStringify(BrowserName.EDGE), equalTo("\"Edge\"\n"));
    assertThat("Safari", YAML.jStringify(BrowserName.SAFARI), equalTo("\"Safari\"\n"));
  }
}
