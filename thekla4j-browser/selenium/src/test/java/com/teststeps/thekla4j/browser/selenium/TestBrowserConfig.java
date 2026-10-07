package com.teststeps.thekla4j.browser.selenium;

import static com.teststeps.thekla4j.browser.selenium.SeleniumBuilderFunctions.loadConfigs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.function.Function;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBrowserConfig {

  private static final String SELENIUM_CONFIG_PROPERTY = "thekla4j.browser.selenium.config";
  private static final String BROWSER_CONFIG_PROPERTY = "thekla4j.browser.config";

  private static String ambientSeleniumConfig;
  private static String ambientBrowserConfig;

  @BeforeAll
  public static void rememberAmbientProperties() {
    ambientSeleniumConfig = System.getProperty(SELENIUM_CONFIG_PROPERTY);
    ambientBrowserConfig = System.getProperty(BROWSER_CONFIG_PROPERTY);
  }

  /**
   * These tests assert which configuration a passed name resolves to, and the configuration selecting properties win
   * over a passed name. A test run that sets them, for example to run against the grid, would resolve a different
   * configuration, so they are taken out of the way here and put back afterwards.
   */
  @BeforeEach
  public void ignoreTheConfigurationOfTheTestRun() {
    System.clearProperty(SELENIUM_CONFIG_PROPERTY);
    System.clearProperty(BROWSER_CONFIG_PROPERTY);
    Thekla4jProperty.resetPropertyCache();
  }

  @AfterAll
  public static void restoreAmbientProperties() {
    restore(SELENIUM_CONFIG_PROPERTY, ambientSeleniumConfig);
    restore(BROWSER_CONFIG_PROPERTY, ambientBrowserConfig);
    Thekla4jProperty.resetPropertyCache();
  }

  private static void restore(final String name, final String value) {
    if (value == null) {
      System.clearProperty(name);
    } else {
      System.setProperty(name, value);
    }
  }

  @Test
  public void loadSeleniumConfigSetByPassedVariable() throws Throwable {

    Option<String> configToLoad = Option.of("passedByCode");

    SeleniumGridConfig config = loadConfigs.apply(configToLoad, Option.none(), List.empty(), List.empty())
        .getOrElseThrow(Function.identity())._1.getOrElseThrow(() -> new RuntimeException("cant get SeleniumConfig"));


    assertThat("retrieving seleniumConfig is success",
      config.remoteUrl(),
      equalTo("http://passedByCode:1234"));

  }

  @Test
  public void updateSeleniumConfig() throws Throwable {

    Option<String> configToLoad = Option.of("passedByCode");

    List<Function1<SeleniumGridConfig, SeleniumGridConfig>> seleniumConfigUpdate =
        List.of(c -> c.withRemoteUrl("http://updatedUrl:3333"));

    SeleniumGridConfig config = loadConfigs.apply(configToLoad, Option.none(), seleniumConfigUpdate, List.empty())
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
      config.debug().debuggerAddress(),
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

    Tuple2<Option<SeleniumGridConfig>, Option<BrowserConfig>> config = loadConfigs.apply(seleniumConfigToLoad, browserConfigToLoad,
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
      config._2.get().debug().debuggerAddress(),
      equalTo("passedByCode"));

  }

  @Test
  public void loadSeleniumConfigOfLOCAL() throws Throwable {

    Option<String> configToLoad = Option.of("LOCAL");

    Option<SeleniumGridConfig> config = loadConfigs.apply(configToLoad, Option.none(), List.empty(), List.empty())
        .getOrElseThrow(Function.identity())._1;


    assertThat("retrieving empty seleniumConfig is success",
      config.isEmpty(),
      equalTo(true));

  }
}
