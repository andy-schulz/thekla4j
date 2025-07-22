package com.teststeps.thekla4j.browser.selenium.integration;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.selenium.examples.SeleniumBidiTest;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Property;
import com.teststeps.thekla4j.browser.spp.activities.SetUpload;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class IT_SetFileToUpload {

  Actor actor;

  @AfterEach
  public void tearDown() {
    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void uploadFile() throws ActivityError, URISyntaxException {

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element uploadInput = Element.found(By.css("#upload"));


    URL url1 = SeleniumBidiTest.class.getClassLoader().getResource("files/file_1.txt");
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

    actor = Actor.named("Tester").whoCan(BrowseTheWeb.with(Selenium.browser()));

    Element uploadInput = Element.found(By.css("#upload"));

    // find the absolute file paths relative to the project root print the code here

    URL url1 = SeleniumBidiTest.class.getClassLoader().getResource("files/file_1.txt");
    URL url2 = SeleniumBidiTest.class.getClassLoader().getResource("files/file_2.txt");
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
