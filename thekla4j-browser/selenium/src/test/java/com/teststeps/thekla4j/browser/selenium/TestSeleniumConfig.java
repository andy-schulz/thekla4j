package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Selenium.loadConfigs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestSeleniumConfig {
  @Test
  public void loadSeleniumConfigSetByPassedVariable() throws Throwable {

    Option<String> configToLoad = Option.of("passedByCode");

    SeleniumConfig config = loadConfigs.apply(configToLoad)
      .getOrElseThrow(Function.identity())
      ._1.getOrElseThrow(() -> new RuntimeException("cant get SeleniumConfig"));


    assertThat("retrieving seleniumConfig is success",
      config.remoteUrl(),
      equalTo("http://passedByCode:1234"));

  }

  @Test
  public void loadSeleniumConfigOfNone() throws Throwable {

    Option<String> configToLoad = Option.of("NONE");

    Option<SeleniumConfig> config = loadConfigs.apply(configToLoad)
      .getOrElseThrow(Function.identity())
      ._1;


    assertThat("retrieving empty seleniumConfig is success",
      config.isEmpty(),
      equalTo(true));

  }
}
