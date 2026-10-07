package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.Constants.DOWNLOAD;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.BrowserSetup;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.DownloadFile;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_Download {

  private Actor actor;

  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
  }

  @BeforeEach
  public void initActor() {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(browser()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  private Browser browser() {
    return BrowserSetup.browser(config -> config.withEnableFileDownload(true));
  }

  @Test
  public void downloadFileTest() throws ActivityError {

    Element downloadButton = Element.found(By.css("#downloadFile"))
        .withName("Download Button");

    String url = DOWNLOAD;

    String file = actor.attemptsTo(

      Navigate.to(url),

      DownloadFile.by(Click.on(downloadButton))
          .named("DownloadFile.txt"))
        .map(readStringFromFile)

        .getOrElseThrow(Function.identity());

    assertThat("content is correct", file, equalTo("Hello World!\nI am a downloaded file."));
  }
}