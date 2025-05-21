package com.teststeps.thekla4j.browser.core.helper;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Either;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Helper functions for taking screenshots
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Log4j2(topic = "Screenshot Operations")
public class ScreenshotFunctions {

  /**
   * Get the path to save the screenshot
   *
   * @return - the path to save the screenshot
   */
  protected static String getScreenshotPath() {

    return Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SCREENSHOT_RELATIVE_PATH.property())
        .map(s -> s.isEmpty() ?
            Thekla4jProperty.of(DefaultThekla4jBrowserProperties.SCREENSHOT_ABSOLUTE_PATH.property()).get() :
            System.getProperty("user.dir") + File.separator + s)
        .map(s -> s.isEmpty() ? System.getProperty("user.dir") : s)
        .peek(s -> log.info("Screenshot directory: {}", s))
        .peek(s -> new File(s).mkdirs())
        .getOrElse(System.getProperty("java.io.tmpdir"));

  }


  /**
   * Take a screenshot of the browser
   *
   * @param browser - the browser to take the screenshot of
   * @return - a Try containing the file of the screenshot
   */
  public static Either<ActivityError, File> takeScreenshot(Browser browser) {

    // new formatted data
    String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

    return browser.getSessionId()
        .flatMap(sessId -> browser.takeScreenShot()
            .mapTry(file -> Files.move(file.toPath(),
              (new File(getScreenshotPath() + "/screenshot_" + date + "_" + UUID.randomUUID() + "_" + sessId + ".png")).toPath())))
        .map(path -> new File(path.toUri()))
        .peek(file -> log.info("Screenshot taken: {}", file.getAbsolutePath()))
        .onFailure(log::error)
        .transform(ActivityError.toEither("Failed to take screenshot"));
  }
}
