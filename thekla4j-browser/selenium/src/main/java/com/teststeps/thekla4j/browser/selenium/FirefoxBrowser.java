package com.teststeps.thekla4j.browser.selenium;

import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxBrowser extends SeleniumBrowser {


  public static FirefoxBrowser with() {
    return new FirefoxBrowser();
  }


  private FirefoxBrowser() {
    super(new FirefoxDriver());
  }
}
