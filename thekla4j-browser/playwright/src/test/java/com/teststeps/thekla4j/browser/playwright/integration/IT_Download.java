package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.DOWNLOAD;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.PlaywrightBrowser;
import com.teststeps.thekla4j.browser.playwright.PlaywrightLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.DownloadFile;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_Download {

  private Actor actor;

  @BeforeEach
  public void initActor() {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromiumWithDownload()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  private Browser chromiumWithDownload() {
    BrowserConfig browserConfig = BrowserConfig.of(BrowserName.CHROMIUM)
        .withHeadless(false)
        .withEnableFileDownload(true);
    PlaywrightLoader loader = PlaywrightLoader.of(browserConfig);
    return PlaywrightBrowser.load(loader, browserConfig);
  }

  @Test
  public void downloadFileTest() throws ActivityError {

    Element downloadButton = Element.found(By.css("#downloadFile"))
        .withName("Download Button");

    String file = actor.attemptsTo(

      Navigate.to(DOWNLOAD),

      DownloadFile.by(Click.on(downloadButton))
          .named("DownloadFile.txt"))
        .map(readStringFromFile)

        .getOrElseThrow(Function.identity());

    assertThat("content is correct", file, equalTo("Hello World!\nI am a downloaded file."));
  }
}
