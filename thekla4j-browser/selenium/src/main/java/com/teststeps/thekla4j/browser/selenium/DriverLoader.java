package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.selenium.logListener.LogManager;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.nio.file.Path;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Interface for loading and managing WebDriver instances.
 */
public interface DriverLoader {

  /**
   * provide the RemoteWebDriver
   *
   * @return a Try of RemoteWebDriver
   */
  Try<RemoteWebDriver> driver();

  /**
   * checks if the driver is executed locally or remotely
   *
   * @return true if the driver is executed locally, false if executed remotely
   */
  boolean isLocalExecution();

  /**
   * gets the download path if supported by the driver
   *
   * @return an optional Path where files are downloaded when using this driver
   */
  Option<Path> downloadPath();

  /**
   * gets the Actions object for advanced user interactions
   *
   * @return a Try of Actions for advanced user interactions
   */
  Try<Actions> actions();

  /**
   * Initializes the log listener for the driver, if supported.
   *
   * @return A Try indicating success or failure.
   */
  Try<Void> activateBrowserLog();

  /**
   * Initializes the log listener for the driver, if supported.
   *
   * @return A Try indicating success or failure.
   */

  Try<LogManager> logManager();

  /**
   * Checks if video recording is active for the driver, if supported.
   *
   * @return A Try indicating whether video recording is active or not.
   */
  boolean isVideoRecordingActive();
}
