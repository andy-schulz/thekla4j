package com.teststeps.thekla4j.browser.appium.mock;

import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;

public class RemoteWebDriverOptionsMock implements WebDriver.Options {

  @Override
  public void addCookie(Cookie cookie) {

  }

  @Override
  public void deleteCookieNamed(String name) {

  }

  @Override
  public void deleteCookie(Cookie cookie) {

  }

  @Override
  public void deleteAllCookies() {

  }

  @Override
  public Set<Cookie> getCookies() {
    return Set.of();
  }

  @Override
  public @Nullable Cookie getCookieNamed(String name) {
    return null;
  }

  @Override
  public WebDriver.Timeouts timeouts() {
    return null;
  }

  @Override
  public WebDriver.Window window() {
    return null;
  }

  @Override
  public Logs logs() {
    return null;
  }
}
