package com.teststeps.thekla4j.browser.core.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.control.Try;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScreenshotTests {

  public static void setPropertyContent(String content) {

    Try.of(ScreenshotTests.class::getClassLoader)
        .map(loader -> loader.getResource("thekla4j.properties"))
        .mapTry(URL::toURI)
        .map(File::new)
        .mapTry(PrintWriter::new)
        .peek(pw -> pw.print(content))
        .peek(PrintWriter::close)
        .getOrElseThrow(x -> new RuntimeException("Could not load thekla4j.properties", x));
  }

  @BeforeEach
  public void reset() {
    Thekla4jProperty.resetPropertyCache();
  }

  @Test
  public void absoluteScreenshotPathNotSet() {

    String property = "";
    setPropertyContent(property);

    String path = ScreenshotFunctions.getScreenshotPath();

    assertThat("path", path, equalTo(System.getProperty("user.dir")));

  }

  @Test
  public void absoluteScreenshotPathSetToEmptyString() {

    String property = "thekla4j.browser.screenshot.absolutePath=";
    setPropertyContent(property);

    String path = ScreenshotFunctions.getScreenshotPath();

    assertThat("path", path, equalTo(System.getProperty("user.dir")));
  }

  @Test
  public void absoluteScreenshotPathSetToValue() {

    String property = "thekla4j.browser.screenshot.absolutePath=testFolder";
    setPropertyContent(property);

    String path = ScreenshotFunctions.getScreenshotPath();

    assertThat("path", path, equalTo("testFolder"));
  }

  @Test
  public void relativeScreenshotPathSetToValue() {

    String property = "thekla4j.browser.screenshot.relativePath=testFolder";
    setPropertyContent(property);

    String path = ScreenshotFunctions.getScreenshotPath();

    assertThat("path", path, equalTo(System.getProperty("user.dir") + File.separator + "testFolder"));
  }
}
