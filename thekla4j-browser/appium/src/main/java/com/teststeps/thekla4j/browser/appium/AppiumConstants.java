package com.teststeps.thekla4j.browser.appium;

/**
 * Constants for Appium configuration and capabilities.
 */
public class AppiumConstants {
  /** Default Appium server URL */
  static final String LOCAL_APPIUM_SERVICE = "http://localhost:4723";

  /** Default appium capability prefix */
  public final static String APPIUM_PREFIX = "appium";
  /** appium capability key for automation name with prefix */
  public final static String K_AUTOMATION_NAME = APPIUM_PREFIX + ":automationName";
  /** appium capability key for automation name without prefix */
  public final static String AUTOMATION_NAME = "automationName";
  /** appium capability key for device name with prefix */
  public final static String K_DEVICE_NAME = APPIUM_PREFIX + ":deviceName";
  /** appium capability key for platform version with prefix */
  public final static String K_PLATFORM_VERSION = APPIUM_PREFIX + ":platformVersion";
  /** appium capability key for chrome driver with prefix */
  public final static String K_CHROME_DRIVER_EXECUTABLE = APPIUM_PREFIX + ":chromedriverExecutable";

  /** appium capability value for UiAutomator2 */
  public final static String V_UI_AUTOMATOR_2 = "uiautomator2";
  /** appium capability value for xciu test */
  public final static String V_XCUI_TEST = "xcuitest";

}
