package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.functions.ConfigFunctions;
import com.teststeps.thekla4j.utils.vavr.TransformOption;
import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Browser")
class BrowserFunctions {

  static Function1<BrowserName, Try<Browser>> loadRemoteBrowser = browserName -> ConfigFunctions.loadSeleniumConfig.apply()
    .onFailure(e -> log.error(() -> "Error loading Selenium config: " + e))
    .flatMap(opt -> opt.transform(TransformOption.toTry("No Selenium config found")))
    .flatMap(sc -> RemoteBrowser.with(Option.none(), sc, BrowserConfig.of(browserName)));
}
