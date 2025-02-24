package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Move;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.mock.RemoteTargetLocatorMock;
import com.teststeps.thekla4j.browser.selenium.mock.RemoteTestNavigationMock;
import com.teststeps.thekla4j.browser.selenium.mock.RemoteWebDriverOptionsMock;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.*;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestMobileBrowser {


  private Actor actor;

  @InjectMocks
  @Spy
  MobileBrowser mobileBrowserMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RemoteWebDriver driverMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  WebElement webElementMock;

  @Mock
  RemoteWebDriverOptionsMock webDriverOptionsMock;

  @Mock
  RemoteTestNavigationMock webDriverNavigationMock;

  @Mock
  RemoteTargetLocatorMock webDriverTargetLocatorMock;


  @Captor
  ArgumentCaptor<String> urlCaptor;

  Element element = Element.found(By.css("button#accept-cookies"));

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);

    when(driverMock.findElements(any(org.openqa.selenium.By.class))).thenReturn(List.of(webElementMock));
    when(driverMock.manage()).thenReturn(webDriverOptionsMock);
    when(driverMock.navigate()).thenReturn(webDriverNavigationMock);
    when(driverMock.switchTo()).thenReturn(webDriverTargetLocatorMock);

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(mobileBrowserMock));

  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void testNavigateTo() throws ActivityError {

    String url = "theUrl";

    Navigate.to(url).runAs(actor);

    verify(mobileBrowserMock).navigateTo(urlCaptor.capture());
    verify(driverMock, times(1)).navigate();
  }

  @Test
  public void testClickOnElement() throws ActivityError {

    Click.on(element).runAs(actor);

    verify(mobileBrowserMock).clickOn(any(Element.class));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).click();
  }

  @Test
  public void testDoubleClickOnElement() {

    Either<ActivityError, Void> result = actor.attemptsTo(DoubleClick.on(element));

    assertThat("Double click is not supported on mobile devices", result.isLeft());
    assertThat("Double click is not supported on mobile devices", result.getLeft().getMessage(),
      containsString("Double Click is not supported to work on mobile devices"));
  }

  @Test
  public void testEnterTextIntoElement() throws ActivityError {

    Enter.text("text").into(element).runAs(actor);

    verify(mobileBrowserMock).enterTextInto(anyString(), any(Element.class), eq(false));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).sendKeys("text");
  }

  @Test
  public void testEnterTextIntoClearedElement() throws ActivityError {

    Enter.text("text").intoCleared(element).runAs(actor);

    verify(mobileBrowserMock).enterTextInto(anyString(), any(Element.class), eq(true));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).sendKeys("text");
  }

  @Test
  public void testTextOfElement() throws ActivityError {

    Text.of(element).runAs(actor);

    verify(mobileBrowserMock).textOf(any(Element.class));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getText();
  }

  @Test
  public void testValueOf() throws ActivityError {

    Value.of(element).runAs(actor);

    verify(mobileBrowserMock).valueOf(any(Element.class));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getAttribute("value");
  }

  @Test
  public void testAttributeOf() throws ActivityError {

    Attribute.named("test").of(element).runAs(actor);

    verify(mobileBrowserMock).attributeValueOf(eq("test"), any(Element.class));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getAttribute("test");
  }

  @Test
  public void testElementState() throws ActivityError {

    ElementState.of(element).runAs(actor);

    verify(mobileBrowserMock).getState(any(Element.class));
    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(2)).isDisplayed();
    verify(webElementMock, times(2)).isEnabled();
  }

  @Test
  public void testTitle() throws ActivityError {

    Title.ofPage().runAs(actor);

    verify(mobileBrowserMock).title();
    verify(driverMock).getTitle();
  }

  @Test
  public void testUrl() throws ActivityError {

    Url.ofPage().runAs(actor);

    verify(mobileBrowserMock).url();
    verify(driverMock).getCurrentUrl();
  }

  @Test
  public void testGetCookie() throws ActivityError {

    org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie("testCookie", "testCookieValue");
    when(webDriverOptionsMock.getCookieNamed("testCookie")).thenReturn(cookie);

    GetCookie.named("testCookie").runAs(actor);

    verify(mobileBrowserMock).getCookie("testCookie");
    verify(webDriverOptionsMock).getCookieNamed("testCookie");
  }

  @Test
  public void testGetAllCookies() throws ActivityError {

    org.openqa.selenium.Cookie cookie1 = new org.openqa.selenium.Cookie("testCookie1", "testCookieValue");
    org.openqa.selenium.Cookie cookie2 = new org.openqa.selenium.Cookie("testCookie2", "testCookieValue");
    org.openqa.selenium.Cookie cookie3 = new org.openqa.selenium.Cookie("testCookie3", "testCookieValue");
    when(webDriverOptionsMock.getCookies()).thenReturn( Set.of(cookie1, cookie2, cookie3));

    io.vavr.collection.List<Cookie> cookies =  GetAllCookies.fromBrowser().runAs(actor);

    verify(mobileBrowserMock).getAllCookies();
    verify(webDriverOptionsMock).getCookies();

    assertThat("get list of 3 cookies", cookies.length(), equalTo(3));
  }

  @Test
  public void testAddCookie() throws ActivityError {
    org.openqa.selenium.Cookie webCookie = new org.openqa.selenium.Cookie("cookieName", "CookieValue");

    Cookie cookie = Cookie.of("cookieName", "CookieValue");

    AddCookie.toBrowser(cookie).runAs(actor);

    verify(mobileBrowserMock).addCookie(cookie);
    verify(webDriverOptionsMock).addCookie(webCookie);
  }

  @Test
  public void testDeleteCookie() throws ActivityError {
    DeleteCookie.named("cookieName").runAs(actor);

    verify(mobileBrowserMock).deleteCookie("cookieName");
    verify(webDriverOptionsMock).deleteCookieNamed("cookieName");
  }

  @Test
  public void testDeleteAllCookies() throws ActivityError {
    DeleteAllCookies.fromBrowser().runAs(actor);

    verify(mobileBrowserMock).deleteAllCookies();
    verify(webDriverOptionsMock).deleteAllCookies();
  }

  @Test
  public void testTakePageScreenshot() throws ActivityError {

    File screenShotDummy = new File("screenShotDummyPath");
    when(driverMock.getScreenshotAs(any())).thenReturn(screenShotDummy);

    File screenshot = TakeScreenshot.ofPage().runAs(actor);

    verify(mobileBrowserMock).takeScreenShot();
    verify(driverMock).getScreenshotAs(any());

    assertThat("screenshots are equal", screenshot, equalTo(screenShotDummy));
  }

  @Test
  public void testTakeElementScreenshot() throws ActivityError {

    File screenShotDummy = new File("elementScreenshotDummy");
    when(webElementMock.getScreenshotAs(any())).thenReturn(screenShotDummy);

    File screenshot = TakeScreenshot.ofElement(element).runAs(actor);

    verify(mobileBrowserMock).takeScreenShotOfElement(element);
    verify(webElementMock).getScreenshotAs(any());

    assertThat("screenshots are equal", screenshot, equalTo(screenShotDummy));
  }

  @Test
  public void testDrawShape() throws ActivityError {

    Shape shape = Shape.startingAt(StartPoint.on(1,1))
        .moveTo(Move.right(10));

    Draw.shape(shape).on(element).runAs(actor);

    verify(mobileBrowserMock).drawShapes(io.vavr.collection.List.of(shape), element, false, Option.none());
    verify(webElementMock, times(2)).getLocation();

  }

  @Test
  public void testDrawShapes() throws ActivityError {

    Shape shape = Shape.startingAt(StartPoint.on(1,1))
      .moveTo(Move.right(10));

    Draw.shapes(shape, shape).on(element).runAs(actor);

    verify(mobileBrowserMock).drawShapes(io.vavr.collection.List.of(shape, shape), element, false, Option.none());
    verify(webElementMock, times(4)).getLocation();

  }

  @Test
  public void testUploadFile() throws ActivityError {
    Path uploadFilePath = Paths.get("").toAbsolutePath();

    SetUpload.file(uploadFilePath).to(element).runAs(actor);

    verify(mobileBrowserMock).setUploadFiles(io.vavr.collection.List.of(uploadFilePath), element);
    verify(webElementMock, times(1)).sendKeys(uploadFilePath.toString());

  }

  @Test
  public void testPageRefresh() throws ActivityError {
    RefreshCurrentBrowser.page().runAs(actor);

    verify(mobileBrowserMock).refresh();
    verify(webDriverNavigationMock, times(1)).refresh();

  }

  @Test
  public void testNavigateBack() throws ActivityError {
    Navigate.back().runAs(actor);

    verify(mobileBrowserMock).navigateBack();
    verify(webDriverNavigationMock, times(1)).back();

  }

  @Test
  public void testNavigateForward() throws ActivityError {
    Navigate.forward().runAs(actor);

    verify(mobileBrowserMock).navigateForward();
    verify(webDriverNavigationMock, times(1)).forward();

  }

  @Test
  public void testSwitchToNewBrowserTab() throws ActivityError {
    SwitchToNewBrowser.tab().runAs(actor);

    verify(mobileBrowserMock).switchToNewBrowserTab();
    verify(webDriverTargetLocatorMock, times(1)).newWindow(WindowType.TAB);

  }

  @Test
  public void testSwitchToNewBrowserWindow() throws ActivityError {
    SwitchToNewBrowser.window().runAs(actor);

    verify(mobileBrowserMock).switchToNewBrowserWindow();
    verify(webDriverTargetLocatorMock, times(1)).newWindow(WindowType.WINDOW);

  }

  @Test
  public void testSwitchToBrowserByTitle() throws ActivityError {
    SwitchToBrowser.havingTitle("new Window").runAs(actor);

    verify(mobileBrowserMock).switchToBrowserByTitle("new Window");
    verify(driverMock, times(1)).getWindowHandles();

  }

  @Test
  public void testSwitchToBrowserByIndex() throws ActivityError {

    when(driverMock.getWindowHandles()).thenReturn(Set.of("1", "2", "3"));

    SwitchToBrowser.byIndex(1).runAs(actor);

    verify(mobileBrowserMock).switchToBrowserByIndex(1);
    verify(driverMock, times(1)).getWindowHandles();

  }

  @Test
  public void testNumberOfOpenTabs() throws ActivityError {

    when(driverMock.getWindowHandles()).thenReturn(Set.of("1", "2", "3"));

    NumberOfBrowser.tabsAndWindows().runAs(actor);

    verify(mobileBrowserMock).numberOfOpenTabsAndWindows();
    verify(driverMock, times(1)).getWindowHandles();

  }

  @Test
  public void testKeyPress() throws ActivityError {

    DoKey.press(Key.ENTER).runAs(actor);

    verify(mobileBrowserMock).executeKeyActions();
    verify(driverMock, times(1)).perform(any());

  }

  @Test
  public void testExecuteJavaScriptInBrowser() throws ActivityError {

    ExecuteJavaScript.onBrowser("alert('Hello World')").runAs(actor);

    verify(mobileBrowserMock).executeJavaScript("alert('Hello World')");
    verify(driverMock, times(1)).executeScript(anyString(), any());
  }

  @Test
  public void testExecuteJavaScriptOnElement() throws ActivityError {

    ExecuteJavaScript.onElement("alert('Hello World')", element).runAs(actor);

    verify(mobileBrowserMock).executeJavaScript("alert('Hello World')", element);
    verify(driverMock, times(1)).executeScript(anyString(), any());
  }

  @Test
  public void testActorStageCleaning() {

    actor.cleansStage();

    verify(mobileBrowserMock, times(1)).quit();
    verify(driverMock, times(1)).quit();
  }

}
