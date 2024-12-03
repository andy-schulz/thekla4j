package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Frame;
import com.teststeps.thekla4j.browser.selenium.error.ElementNotFoundError;
import com.teststeps.thekla4j.browser.selenium.status.SeleniumElementStatus;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function5;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.time.Instant;

import static com.teststeps.thekla4j.browser.selenium.element.ElementHelperFunctions.scrollIntoView;

@Log4j2
public class FrameFunctions {

  static Try<WebElement> findFrame(RemoteWebDriver driver, Frame frame) {

    return retryUntil.apply(
        FrameFunctions.locateFrame.apply(driver),
        locateFrame.apply(driver).apply(frame),
        frame,
        Instant.now(),
        Duration.ofMillis(0))
      .flatMap(scrollIntoView.apply(driver));
  }

  private static final Function1<RemoteWebDriver, Function1<Frame, Try<WebElement>>> locateFrame =
    drvr -> frame ->
      Try.of(() -> ElementFunctions.getElements.apply(drvr, frame.locators()))
        .mapTry(l -> l.getOrElseThrow(() -> ElementNotFoundError.of("Could not find " + frame)));

  private static final Function5<Function1<Frame, Try<WebElement>>, Try<WebElement>, Frame, Instant, Duration, Try<WebElement>> retryUntil =
    (frameFinder, webElement, frame, start, waitFor) -> {

      Try.run(() -> Thread.sleep(waitFor.toMillis()))
        .onFailure(log::error);

      Duration waitForNextIteration = Duration.ofMillis(500);

      Duration timeout = frame.timeout();

      log.debug("Retrying to find frame: {}", frame);

      if (Instant.now().isAfter(start.plus(timeout))) {
        return webElement;
      }

      if (webElement.isFailure()) {
        return FrameFunctions.retryUntil.apply(frameFinder, frameFinder.apply(frame), frame, start, waitForNextIteration);
      }

      return webElement
        .flatMap(SeleniumElementStatus.IS_NOT_STALE::check)
        .onFailure(status -> log.error("Error on {} ElementStatusCheck status: {}", SeleniumElementStatus.IS_NOT_STALE, status))
        .onSuccess(status -> log.debug("Element {} status: {}", SeleniumElementStatus.IS_NOT_STALE, status))
        .flatMap(elemStatus ->
          elemStatus ?
            webElement :
            FrameFunctions.retryUntil.apply(frameFinder, frameFinder.apply(frame), frame, start, waitForNextIteration))
        .transform(tr -> tr.isSuccess() ? tr :
          FrameFunctions.retryUntil.apply(frameFinder, frameFinder.apply(frame), frame, start, waitForNextIteration));
    };

  protected final static Function2<RemoteWebDriver, Frame, Try<Void>> switchToFrame =
    (driver, frame) -> {

      driver.switchTo().defaultContent();

      return findFrame(driver, frame)
        .onFailure(e -> log.error("Could not find frame: {}", frame, e))
        .map(webElement -> {
          driver.switchTo().frame(webElement);
          return null;
        });
    };
}
