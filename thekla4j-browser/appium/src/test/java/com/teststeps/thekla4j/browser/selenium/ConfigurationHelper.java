package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfigList;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfigList;
import com.teststeps.thekla4j.utils.yaml.YAML;
import io.vavr.Function2;

public class ConfigurationHelper {

  static final Function2<String, String, BrowserConfig> getDefaultBrowserConfig = (config, defaultName) -> {
    BrowserConfigList browserConfigList = YAML.jParse(BrowserConfigList.class)
        .apply(config)
        .getOrElseThrow(x -> new RuntimeException("Error loading BrowserConfig", x));

    assertThat("check browser config exists", browserConfigList.getBrowserConfigs().size(), equalTo(1));
    assertThat("check browser config '" + defaultName + "' exists", browserConfigList.browserConfigs().get(defaultName).isDefined(),
      equalTo(true));

    return browserConfigList.browserConfigs().get(browserConfigList.defaultConfig()).get();
  };

  static final Function2<String, String, SeleniumConfig> getDefaultSeleniumConfig = (config, defaultName) -> {
    SeleniumConfigList seleniumConfigList = YAML.jParse(SeleniumConfigList.class)
        .apply(config)
        .getOrElseThrow(x -> new RuntimeException("Error loading SeleniumConfig", x));

    assertThat("check selenium config exists", seleniumConfigList.getSeleniumConfigs().size(), equalTo(1));
    assertThat("check browser config '" + defaultName + "' exists", seleniumConfigList.seleniumConfigs().get(defaultName).isDefined(),
      equalTo(true));

    return seleniumConfigList.seleniumConfigs().get(seleniumConfigList.defaultConfig()).get();
  };
}
