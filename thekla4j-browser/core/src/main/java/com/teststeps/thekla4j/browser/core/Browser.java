package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.spp.activities.State;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.File;
import java.time.Duration;

public interface Browser {

  Try<Void> navigateTo(String url);

  Try<Void> clickOn(Element element);

  Try<Void> doubleClickOn(Element element);

  Try<Void> enterTextInto(String text, Element element, Boolean clearField);

  Try<String> textOf(Element element);

  Try<String> valueOf(Element element);

  Try<String> attributeValueOf(String attribute, Element element);

  Try<State> getState(Element element);

  Try<String> title();

  Try<String> url();

  Try<Cookie> getCookie(String name);

  Try<List<Cookie>> getAllCookies();

  Try<Void> addCookie(Cookie cookie);

  Try<Void> deleteCookie(String name);

  Try<Void> deleteAllCookies();

  Try<File> takeScreenShot();

  Try<Void> drawShapes(List<Shape> shape, Element element, Boolean releaseAndHold, Option<Duration> pause);

  Try<Void> switchToNewBrowserTab();

  Try<Void> switchToNewBrowserWindow();

  Try<Void> switchToBrowserByTitle(String browserTitle);

  Try<Void> switchToBrowserByIndex(int index);

  Try<Integer> numberOfOpenTabsAndWindows();

  Try<Void> quit();

  Try<String> getSessionId();

  Try<KeyActions> executeKeyActions();

  Try<Void> executeJavaScript(String script, Element element);

  Try<Void> executeJavaScript(String script);


}
