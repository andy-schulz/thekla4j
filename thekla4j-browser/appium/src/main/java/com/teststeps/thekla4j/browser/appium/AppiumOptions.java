package com.teststeps.thekla4j.browser.appium;

import static com.teststeps.thekla4j.browser.appium.AppiumConstants.K_DEVICE_NAME;
import static com.teststeps.thekla4j.browser.appium.AppiumConstants.K_PLATFORM_VERSION;

import com.teststeps.thekla4j.browser.config.BrowserName;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;


/**
 * AppiumOptions behaves like SeleniumOptions but for Appium.
 * You can set capabilities and options for Appium sessions.
 */
public class AppiumOptions extends MutableCapabilities {

  /** The browser name for the Appium session. */
  private BrowserName browserName;

  /**
   * Constructs a new AppiumOptions instance with default settings.
   */
  public AppiumOptions() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCapability(String key, Object value) {
    super.setCapability(key, value);
  }

  /**
   * Merges the given capabilities into this AppiumOptions instance.
   *
   * @param extraCapabilities the capabilities to merge
   * @return this AppiumOptions instance
   */
  public AppiumOptions merge(Capabilities extraCapabilities) {
    if (extraCapabilities != null) {
      extraCapabilities.asMap().forEach(this::setCapability);
    }
    return this;
  }

  /**
   * Retrieves the value of the specified Appium option.
   *
   * @param key the name of the option
   * @return the value of the option, or null if not set
   */
  public Object getAppiumOption(String key) {
    return getCapability(key);
  }

  /**
   * Retrieves all Appium options as a map.
   *
   * @return a map of all Appium options
   */
  public Map<String, Object> getAppiumOptions() {
    return HashMap.ofAll(asMap());
  }

  /**
   * Sets the browser name for the Appium session.
   *
   * @param browserName the browser name to set
   * @return this AppiumOptions instance
   */
  public AppiumOptions setBrowserName(BrowserName browserName) {
    this.browserName = browserName;
    setCapability(CapabilityType.BROWSER_NAME, browserName);
    return this;
  }

  /**
   * Retrieves the browser name for the Appium session.
   *
   * @return the browser name
   */
  public BrowserName getAppiumBrowserName() {
    return this.browserName;
  }

  /**
   * Sets the browser version for the Appium session.
   *
   * @param browserVersion the browser version to set
   * @return this AppiumOptions instance
   */
  public AppiumOptions setBrowserVersion(String browserVersion) {
    setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
    return this;
  }

  /**
   * Sets the platform name for the Appium session.
   *
   * @param platformName the platform name to set (e.g., "iOS", "Android")
   * @return this AppiumOptions instance
   */
  public AppiumOptions setPlatformName(String platformName) {
    setCapability(CapabilityType.PLATFORM_NAME, platformName);
    return this;
  }

  /**
   * Sets the device name for the Appium session.
   *
   * @param deviceName the device name to set
   * @return this AppiumOptions instance
   */
  public AppiumOptions setDeviceName(String deviceName) {
    setCapability(K_DEVICE_NAME, deviceName);
    return this;
  }

  /**
   * Sets the platform version for the Appium session.
   *
   * @param platformVersion the platform version to set (e.g., "14.4", "11.0")
   * @return this AppiumOptions instance
   */
  public AppiumOptions setPlatformVersion(String platformVersion) {
    setCapability(K_PLATFORM_VERSION, platformVersion);
    return this;
  }

  /**
   * Sets the path to the ChromeDriver executable for the Appium session.
   *
   * @param chromedriverExecutable the path to the ChromeDriver executable
   * @return this AppiumOptions instance
   */
  public AppiumOptions setChromedriverExecutable(String chromedriverExecutable) {
    setCapability(AppiumConstants.K_CHROME_DRIVER_EXECUTABLE, chromedriverExecutable);
    return this;
  }
}
