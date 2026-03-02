package com.teststeps.thekla4j.browser.playwright;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Browser;
import io.vavr.control.Option;

public class BrowserSetup {

  public static Browser chromiumBrowser() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROMIUM)
        .withHeadless(false);

    BrowserStartupConfig conf = BrowserStartupConfig.startMaximized();
    PlaywrightLoader loader = PlaywrightLoader.of(browserConfig, Option.of(conf));
    return PlaywrightBrowser.load(loader, browserConfig);
  }
}
