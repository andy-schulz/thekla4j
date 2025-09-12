package com.teststeps.thekla4j.browser.appium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.appium.config.AppiumConfig;
import com.teststeps.thekla4j.browser.appium.config.AppiumConfigList;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserConfigList;
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

  static final Function2<String, String, AppiumConfig> getDefaultAppiumConfig = (config, defaultName) -> {
    AppiumConfigList appiumConfigList = YAML.jParse(AppiumConfigList.class)
        .apply(config)
        .getOrElseThrow(x -> new RuntimeException("Error loading AppiumConfig", x));

    assertThat("check appium config exists", appiumConfigList.appiumConfigs().size(), equalTo(1));
    assertThat("check appium config '" + defaultName + "' exists", appiumConfigList.appiumConfigs().get(defaultName).isDefined(),
      equalTo(true));

    return appiumConfigList.appiumConfigs().get(appiumConfigList.defaultConfig()).get();
  };
}
