package com.teststeps.thekla4j.browser.selenium;

import io.vavr.control.Try;
import org.openqa.selenium.WebDriver;

public interface SeleniumDriver {

  Try<WebDriver> getDriver();
}
