package com.teststeps.thekla4j.browser.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeBrowser extends SeleniumBrowser {


  public static ChromeBrowser with() {
    return new ChromeBrowser();
  }


  private ChromeBrowser() {
    super(new ChromeDriver());
  }
}
