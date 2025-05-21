package com.teststeps.thekla4j.browser.selenium.integration;

import static com.teststeps.thekla4j.browser.selenium.properties.DefaultThekla4jSeleniumProperties.SELENIUM_CONFIG;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.TakeScreenshot;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_ScreenshotTest {

  private Actor actor;

  @BeforeAll
  public static void cleanupOldTests() {
    Thekla4jProperty.resetPropertyCache();
    System.clearProperty(SELENIUM_CONFIG.property().name());
  }

  @AfterEach
  public void tearDown() {

    if (actor != null) {
      actor.cleansStage();
    }
  }

  @Test
  public void createScreenshot() throws IOException {

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));


    Either<ActivityError, File> file = actor.attemptsTo$(
      Navigate.to("https://www.google.com"),
      TakeScreenshot.ofPage(),

      "Step", "Click on element");

    assertThat("taking screenshot is a success", file.isRight());
    assertThat("file exists", file.get().exists());
    assertThat("file is a file and not a directory", file.get().isFile());
    assertThat("size of file is bigger than 1024 byte", file.get().length() > 1024);

    System.out.println(file.get().length());

    Path p = Paths.get("").toAbsolutePath();
    Files.move(file.get().toPath(), Paths.get(p.toString(), "page.png"), REPLACE_EXISTING);

  }

  @Test
  public void createScreenshotOfElement() throws IOException {

    Element button = Element.found(By.xpath("//*[text()='Alle akzeptieren']"));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(Selenium.browser()));


    Either<ActivityError, File> file = actor.attemptsTo$(
      Navigate.to("https://www.google.com"),
      TakeScreenshot.ofElement(button),

      "Step", "Click on element");

    assertThat("taking screenshot is a success", file.isRight());
    assertThat("file exists", file.get().exists());
    assertThat("file is a file and not a directory", file.get().isFile());
    assertThat("size of file is bigger than 1024 byte", file.get().length() > 1024);
    assertThat("size of file is smaller than 5000 byte", file.get().length() < 5000);

    Path p = Paths.get("").toAbsolutePath();
    Files.move(file.get().toPath(), Paths.get(p.toString(), "button.png"), REPLACE_EXISTING);


  }
}
