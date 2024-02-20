package com.teststeps.thekla4j.browser.core.helper;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Log4j2(topic = "Screenshot Operations")
public class ScreenshotFunctions {

  protected static String getScreenshotPath() {

    return Option.of(Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SCREENSHOT_RELATIVE_PATH.property()))
        .map(s -> s.isEmpty() ?
            Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SCREENSHOT_ABSOLUTE_PATH.property()) :
            System.getProperty("user.dir") + File.separator + s)
        .map(s -> s.isEmpty() ? System.getProperty("user.dir") : s)
        .peek(s -> log.info("Screenshot directory: {}", s))
        .peek(s -> new File(s).mkdirs())
        .getOrElse(System.getProperty("java.io.tmpdir"));

  }


  public static Either<ActivityError, File> takeScreenshot(Actor actor) {

    // new formatted data
    String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

    return BrowseTheWeb.as(actor)
        .flatMap(Browser::takeScreenShot)
        .mapTry(file -> Files.copy(file.toPath(), (new File(getScreenshotPath() + "/screenshot_" + date + "_" + UUID.randomUUID() + ".png")).toPath()))
        .map(path -> new File(path.toUri()))
        .peek(file -> log.info("Screenshot taken: {}", file.getAbsolutePath()))
        .onFailure(log::error)
        .transform(ActivityError.toEither("Failed to take screenshot"));
  }
}
