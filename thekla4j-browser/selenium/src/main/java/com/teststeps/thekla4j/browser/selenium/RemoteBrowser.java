package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;

@Log4j2(topic = "Selenium Remote Browser")
class RemoteBrowser {
  static Try<Browser> with(Option<String> testName, SeleniumConfig seleniumConfig, BrowserConfig browserConfig) {

    return createCapabilities.apply(browserConfig)
      .map(caps -> {
        caps.setCapability("bstack:options", createBrowserStackCapabilities.apply(testName, seleniumConfig, browserConfig));
        return caps;
      })
      .mapTry(caps -> new RemoteWebDriver(new URL(seleniumConfig.remoteUrl()), caps, false))
      .peek(driver -> System.out.println("SessionID: " + driver.getSessionId()))
      .onFailure(System.err::println)
      .map(SeleniumBrowser::new);
  }

  static Try<Browser> defaultChromeBrowser(Option<String> testName, SeleniumConfig seleniumConfig) {

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setBrowserName("chrome");

    return Try.of(() -> capabilities)
      .map(caps -> {
        caps.setCapability("bstack:options", createBrowserStackCapabilities.apply(testName, seleniumConfig, null));
        return caps;
      })
      .mapTry(url -> new SeleniumBrowser(new RemoteWebDriver(new URL(seleniumConfig.remoteUrl()), capabilities, false)));
  }

  private static final Function3<Option<String>, SeleniumConfig, BrowserConfig, HashMap<String, String>> createBrowserStackCapabilities =
    (testName, seleniumConf, browserConf) -> {

      HashMap<String, String> capabilities = new HashMap<>();

      Option.of(browserConf)
        .flatMap(bc -> Option.of(bc.osVersion()))
        .map(osVers -> capabilities.put("os_version", osVers));

      Option.of(seleniumConf.bStack())
        .flatMap(bst -> Option.of(bst.projectName()))
        .map(projName -> capabilities.put("projectName", projName));

      Option.of(seleniumConf.bStack())
        .flatMap(bst -> Option.of(bst.buildName()))
        .map(buildName -> capabilities.put("buildName", buildName));

      if(testName.isEmpty()) {
        Option.of(seleniumConf.bStack())
          .flatMap(bst -> Option.of(bst.sessionName()))
          .map(sessName -> capabilities.put("sessionName", sessName));
      } else {
        testName.map(tn -> capabilities.put("sessionName", tn));
      }

      Option.of(seleniumConf.bStack())
        .flatMap(bst -> Option.of(bst.geoLocation()))
        .map(sessName -> capabilities.put("geoLocation", sessName));


      log.debug("BrowserStack Capabilities created: " + capabilities);
      return capabilities;
    };


  private static final Function1<BrowserConfig, Try<DesiredCapabilities>> createCapabilities =
    browserConfig ->
      Try.of(DesiredCapabilities::new)
        .map(capabilities -> {
          Option.of(browserConfig.browserName()).forEach(bn -> capabilities.setBrowserName(bn.getName().toLowerCase()));
          Option.of(browserConfig.browserVersion()).forEach(capabilities::setVersion);
          Option.of(browserConfig.os()).forEach(os -> capabilities.setPlatform(Platform.fromString(os.getName())));
          return capabilities;
        })
        .onSuccess(capa -> log.debug("Capabilities created: " + capa));
}
