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
 * AppiumOptions behaves like ChromeOptions but for Appium.
 * You can set capabilities and options for Appium sessions.
 */
public class AppiumOptions extends MutableCapabilities {

  BrowserName browserName;

  public AppiumOptions() {
    super();
  }

  public void setCapability(String key, Object value) {
    super.setCapability(key, value);
  }

  public AppiumOptions merge(Capabilities extraCapabilities) {
    if (extraCapabilities != null) {
      extraCapabilities.asMap().forEach(this::setCapability);
    }
    return this;
  }

  public Object getAppiumOption(String key) {
    return getCapability(key);
  }

  public Map<String, Object> getAppiumOptions() {
    return HashMap.ofAll(asMap());
  }

  public AppiumOptions setBrowserName(BrowserName browserName) {
    this.browserName = browserName;
    setCapability(CapabilityType.BROWSER_NAME, browserName);
    return this;
  }

  public BrowserName getAppiumBrowserName() {
    return this.browserName;
  }

  public AppiumOptions setBrowserVersion(String browserVersion) {
    setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
    return this;
  }

  public AppiumOptions setPlatformName(String platformName) {
    setCapability(CapabilityType.PLATFORM_NAME, platformName);
    return this;
  }

  public AppiumOptions setDeviceName(String deviceName) {
    setCapability(K_DEVICE_NAME, deviceName);
    return this;
  }

  public AppiumOptions setPlatformVersion(String platformVersion) {
    setCapability(K_PLATFORM_VERSION, platformVersion);
    return this;
  }

  public AppiumOptions setChromedriverExecutable(String chromedriverExecutable) {
    setCapability(AppiumConstants.K_CHROME_DRIVER_EXECUTABLE, chromedriverExecutable);
    return this;
  }


}
