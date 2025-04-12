package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.DownloadFile;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.teststeps.thekla4j.browser.selenium.Constants.DOWNLOAD;
import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static com.teststeps.thekla4j.utils.file.FileUtils.readStringFromFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IT_Download {

  private Actor actor;
  Element header = Element.found(By.css(".headerElement"));


  @BeforeAll
  public static void init() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @BeforeEach
  public void initActor() {

    BrowserStartupConfig conf = BrowserStartupConfig.startMaximized();

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(Selenium.browser(conf)));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void downloadFileTest() throws ActivityError {

    Element downloadButton = Element.found(By.css("#downloadFile"))
      .withName("Download Button");

    String url = DOWNLOAD;


    String file =  actor.attemptsTo(

        Navigate.to(url),

        DownloadFile.by(Click.on(downloadButton))
          .named("DownloadFile.txt"))
      .map(readStringFromFile)

      .getOrElseThrow(Function.identity());

      assertThat("content is correct", file, equalTo("Hello World!\nI am a downloaded file."));
  }
}