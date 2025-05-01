package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Interface for the SPP browser
 */
public interface Browser {

  /**
   * Navigate to a given URL
   *
   * @param url the URL to navigate to
   * @return a Try containing a Void
   */
  Try<Void> navigateTo(String url);

  /**
   * Click on a given element
   *
   * @param element the element to click on
   * @return a Try containing a Void
   */
  Try<Void> clickOn(Element element);

  /**
   * Click on a given element at a specific position
   *
   * @param element    the element to click on
   * @param position the position to click on
   * @return a Try containing a Void
   */
  Try<Void> clickOnPositionInsideElement(Element element, StartPoint position);

  /**
   * Double click on a given element
   *
   * @param element the element to double click on
   * @return a Try containing a Void
   */
  Try<Void> doubleClickOn(Element element);

  /**
   * Enter text into a given element
   *
   * @param text       the text to enter
   * @param element    the element to enter the text into
   * @param clearField whether to clear the field before entering the text
   * @return a Try containing a Void
   */
  Try<Void> enterTextInto(String text, Element element, Boolean clearField);

  /**
   * Clear existing text from a given element
   *
   * @param element the element to clear
   * @return a Try containing a Void
   */
  Try<Void> clear(Element element);

  /**
   * Get the text of a given element
   *
   * @param element the element to get the text of
   * @return a Try containing the text of the element
   */
  Try<String> textOf(Element element);

  /**
   * Get the text of all elements found by given element locator
   *
   * @param element the element locator to get the text of
   * @return a Try containing a text list of the elements
   */
  Try<List<String>> textOfAll(Element element);

  /**
   * Get the value of a given element
   *
   * @param element the element to get the value of
   * @return a Try containing the value of the element
   */
  Try<String> valueOf(Element element);

  /**
   * Get the value of a given attribute of a given element
   *
   * @param attribute the attribute to get the value of
   * @param element   the element to get the attribute value of
   * @return a Try containing the value of the attribute
   */
  Try<String> attributeValueOf(String attribute, Element element);

  /**
   * Get the state of a given element
   *
   * @param element the element to get the state of
   * @return a Try containing the state of the element
   */
  Try<State> getState(Element element);

  /**
   * Get the title of the browser
   *
   * @return a Try containing the title of the browser
   */
  Try<String> title();

  /**
   * Get the URL of the browser
   *
   * @return a Try containing the URL of the browser
   */
  Try<String> url();

  /**
   * Get a cookie by name
   *
   * @param name the name of the cookie to get
   * @return a Try containing the cookie
   */
  Try<Cookie> getCookie(String name);

  /**
   * Get all cookies
   *
   * @return a Try containing a list of cookies
   */
  Try<List<Cookie>> getAllCookies();

  /**
   * Add a cookie
   *
   * @param cookie the cookie to add
   * @return a Try containing a Void
   */
  Try<Void> addCookie(Cookie cookie);

  /**
   * Delete a cookie by name
   *
   * @param name the name of the cookie to delete
   * @return a Try containing a Void
   */
  Try<Void> deleteCookie(String name);

  /**
   * Delete all cookies
   *
   * @return a Try containing a Void
   */
  Try<Void> deleteAllCookies();

  /**
   * Take a screenshot
   *
   * @return a Try containing the screenshot
   */
  Try<File> takeScreenShot();

  /**
   * Take a screenshot of a given element
   *
   * @param element the element to take a screenshot of
   * @return a Try containing the screenshot
   */
  Try<File> takeScreenShotOfElement(Element element);

  /**
   * Draw shapes on a given element
   *
   * @param shape          the shapes to draw
   * @param element        the element to draw the shapes on
   * @param releaseAndHold whether to release and hold the mouse between the strokes of a shape
   * @param pause          the pause duration between the strokes of a shape
   * @return a Try containing a Void
   */
  Try<Void> drawShapes(List<Shape> shape, Element element, Boolean releaseAndHold, Option<Duration> pause);

  /**
   * Set upload files
   *
   * @param filePaths             the file paths to upload
   * @param targetFileUploadInput the target file upload input
   * @return a Try containing a Void
   */
  Try<Void> setUploadFiles(List<Path> filePaths, Element targetFileUploadInput);

  /**
   * Refresh the browser
   *
   * @return a Try containing a Void
   */
  Try<Void> refresh();

  /**
   * Navigate back
   *
   * @return a Try containing a Void
   */
  Try<Void> navigateBack();

  /**
   * Navigate forward
   *
   * @return a Try containing a Void
   */
  Try<Void> navigateForward();

  /**
   * Switch to a new browser tab
   *
   * @return a Try containing a Void
   */
  Try<Void> switchToNewBrowserTab();

  /**
   * Switch to a new browser window
   *
   * @return a Try containing a Void
   */
  Try<Void> switchToNewBrowserWindow();

  /**
   * Switch to a browser by title
   *
   * @param browserTitle the title of the browser to switch to
   * @return a Try containing a Void
   */
  Try<Void> switchToBrowserByTitle(String browserTitle);

  /**
   * Switch to a browser by index
   *
   * @param index the index of the browser to switch to
   * @return a Try containing a Void
   */
  Try<Void> switchToBrowserByIndex(int index);

  /**
   * Get the number of open tabs and windows
   *
   * @return a Try containing the number of open tabs and windows
   */
  Try<Integer> numberOfOpenTabsAndWindows();

  /**
   * Quit the browser
   *
   * @return a Try containing a Void
   */
  Try<Void> quit();

  /**
   * Get the session ID
   *
   * @return a Try containing the session ID
   */
  Try<String> getSessionId();

  /**
   * Check if video recording is active
   *
   * @return whether video recording is active
   */
  Boolean isVideoRecordingActive();

  /**
   * Execute key actions
   *
   * @return a Try containing the key actions
   */
  Try<KeyActions> executeKeyActions();

  /**
   * Execute JavaScript
   *
   * @param script  the script to execute
   * @param element the element to execute the script on
   * @return a Try containing the result of the script
   */
  Try<Object> executeJavaScript(String script, Element element);

  /**
   * Execute JavaScript
   *
   * @param script the script to execute
   * @return a Try containing the result of the script
   */
  Try<Object> executeJavaScript(String script);

  Try<File> getDownloadedFile(String fileName, Duration timeout, Duration waitBetweenRetries);
}
