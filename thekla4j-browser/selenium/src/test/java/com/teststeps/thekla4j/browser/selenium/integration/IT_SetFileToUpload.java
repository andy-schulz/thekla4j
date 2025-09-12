package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.DriverLoader;
import com.teststeps.thekla4j.browser.selenium.SeleniumBrowser;
import com.teststeps.thekla4j.browser.selenium.SeleniumLoader;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Property;
import com.teststeps.thekla4j.browser.spp.activities.SetUpload;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Option;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_SetFileToUpload {

  Actor actor;

  BrowserConfig browserConfig;
  DriverLoader loader;
  Browser browser;

  @BeforeEach
  public void setUp() {
    browserConfig = BrowserConfig.of(BrowserName.CHROME);
    loader = SeleniumLoader.of(browserConfig, Option.none(), Option.none());
    browser = SeleniumBrowser.load(loader, browserConfig);
  }

  @AfterEach
  public void tearDown() {
    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void uploadFile() throws ActivityError, URISyntaxException {

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(browser));

    Element uploadInput = Element.found(By.css("#upload"));

    URL url1 = IT_SetFileToUpload.class.getClassLoader().getResource("files/file_1.txt");
    Path path1 = Paths.get(url1.toURI());

    actor.attemptsTo(
      Navigate.to("https://www.selenium.dev/selenium/web/upload.html"),

      See.ifThe(Property.named("files").of(uploadInput))
          .is(Expected.to.pass(s -> s.equals("[]"),
            "Files shall not be set before uploading.")),

      SetUpload.file(path1).to(uploadInput),

      See.ifThe(Property.named("files").of(uploadInput))
          .is(Expected.to.pass(s -> s.contains("name=file_1.txt"))))

        .getOrElseThrow(Function.identity());

  }

  @Test
  public void uploadMultipleFile() throws ActivityError, URISyntaxException {

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(browser));

    Element uploadInput = Element.found(By.css("#upload"));

    // find the absolute file paths relative to the project root print the code here

    URL url1 = IT_SetFileToUpload.class.getClassLoader().getResource("files/file_1.txt");
    URL url2 = IT_SetFileToUpload.class.getClassLoader().getResource("files/file_2.txt");
    Path path1 = Paths.get(url1.toURI());
    Path path2 = Paths.get(url2.toURI());

    actor.attemptsTo(
      Navigate.to("https://www.selenium.dev/selenium/web/upload.html"),

      See.ifThe(Property.named("files").of(uploadInput))
          .is(Expected.to.pass(s -> s.equals("[]"),
            "Files shall not be set before uploading.")),

      SetUpload.files(path1, path2).to(uploadInput),


      See.ifThe(Property.named("files").of(uploadInput))
          .is(Expected.to.pass(s -> s.contains("name=file_1.txt") && s.contains("name=file_2.txt"))))

        .getOrElseThrow(Function.identity());

  }
}
