package com.teststeps.thekla4j.browser.playwright;

/**
 * Test constants for Playwright integration tests.
 */
public class Constants {

  /**
   * Base URL for the framework tester application.
   */
  public static String FRAMEWORKTESTER = "http://localhost:3000";

  /**
   * URL for the element states test page.
   */
  public static String ELEMENT_STATES = FRAMEWORKTESTER + "/elementStates/";

  /**
   * URL for the frames test page.
   */
  public static String FRAMES = FRAMEWORKTESTER + "/frames/";

  /**
   * URL for the canvas test page.
   */
  public static String CANVAS = FRAMEWORKTESTER + "/canvas/";

  /**
   * URL for the table test page.
   */
  public static String TABLE = FRAMEWORKTESTER + "/table/";

  /**
   * URL for the download test page.
   */
  public static String DOWNLOAD = FRAMEWORKTESTER + "/download/";

  /**
   * URL for the drag and drop test page.
   */
  public static String DRAG_AND_DROP = FRAMEWORKTESTER + "/dragndrop/";
}
