package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.Selenium.loadConfigs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

public class TestSeleniumConfig {
  @Test
  public void loadSeleniumConfigSetByPassedVariable() throws Throwable {

    Option<String> configToLoad = Option.of("passedByCode");

    SeleniumConfig config = loadConfigs.apply(configToLoad, Option.none(), List.empty(), List.empty())
        .getOrElseThrow(Function.identity())._1.getOrElseThrow(() -> new RuntimeException("cant get SeleniumConfig"));


    assertThat("retrieving seleniumConfig is success",
      config.remoteUrl(),
      equalTo("http://passedByCode:1234"));

  }

  @Test
  public void updateSeleniumConfig() throws Throwable {

    Option<String> configToLoad = Option.of("passedByCode");

    List<Function1<SeleniumConfig, SeleniumConfig>> seleniumConfigUpdate =
        List.of(c -> c.withRemoteUrl("http://updatedUrl:3333"));

    SeleniumConfig config = loadConfigs.apply(configToLoad, Option.none(), seleniumConfigUpdate, List.empty())
        .getOrElseThrow(Function.identity())._1.getOrElseThrow(() -> new RuntimeException("cant get SeleniumConfig"));


    assertThat("retrieving seleniumConfig is success",
      config.remoteUrl(),
      equalTo("http://updatedUrl:3333"));

  }

  @Test
  public void loadBrowserConfigSetByPassedVariable() throws Throwable {

    Option<String> browserConfigToLoad = Option.of("passedByCode");

    BrowserConfig config = loadConfigs.apply(Option.none(), browserConfigToLoad, List.empty(), List.empty())
        .getOrElseThrow(Function.identity())._2.getOrElseThrow(() -> new RuntimeException("cant get BrowserConfig"));


    assertThat("retrieving browser config is success",
      config.chromeOptions().debug().debuggerAddress(),
      equalTo("passedByCode"));

  }

  @Test
  public void updateBrowserConfig() throws Throwable {

    Option<String> browserConfigToLoad = Option.of("passedByCode");

    List<Function1<BrowserConfig, BrowserConfig>> browserConfigUpdate =
        List.of(c -> c.withBrowserVersion("newBrowserVersion"));

    BrowserConfig config = loadConfigs.apply(Option.none(), browserConfigToLoad, List.empty(), browserConfigUpdate)
        .getOrElseThrow(Function.identity())._2.getOrElseThrow(() -> new RuntimeException("cant get BrowserConfig"));


    assertThat("browser version is set",
      config.browserVersion(),
      equalTo("newBrowserVersion"));

  }

  @Test
  public void loadBrowserAndSeleniumConfigSetByPassedVariable() throws Throwable {

    Option<String> browserConfigToLoad = Option.of("passedByCode");
    Option<String> seleniumConfigToLoad = Option.of("passedByCode");

    Tuple2<Option<SeleniumConfig>, Option<BrowserConfig>> config = loadConfigs.apply(seleniumConfigToLoad, browserConfigToLoad,
      List.empty(), List.empty())
        .getOrElseThrow(Function.identity());


    assertThat("selenium config is set",
      config._1.isDefined(),
      equalTo(true));

    assertThat("retrieving seleniumConfig is success",
      config._1.get().remoteUrl(),
      equalTo("http://passedByCode:1234"));

    assertThat("browser config is set",
      config._2.isDefined(),
      equalTo(true));

    assertThat("retrieving selenium config is success",
      config._2.get().chromeOptions().debug().debuggerAddress(),
      equalTo("passedByCode"));

  }

  @Test
  public void loadSeleniumConfigOfNone() throws Throwable {

    Option<String> configToLoad = Option.of("NONE");

    Option<SeleniumConfig> config = loadConfigs.apply(configToLoad, Option.none(), List.empty(), List.empty())
        .getOrElseThrow(Function.identity())._1;


    assertThat("retrieving empty seleniumConfig is success",
      config.isEmpty(),
      equalTo(true));

  }
}
