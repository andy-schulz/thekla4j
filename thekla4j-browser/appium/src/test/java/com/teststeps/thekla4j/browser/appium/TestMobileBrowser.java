package com.teststeps.thekla4j.browser.appium;

import static io.vavr.control.Option.none;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.appium.mock.RemoteTargetLocatorMock;
import com.teststeps.thekla4j.browser.appium.mock.RemoteTestNavigationMock;
import com.teststeps.thekla4j.browser.appium.mock.RemoteWebDriverOptionsMock;
import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Move;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.*;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.DoKey;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.appium.java_client.android.AndroidDriver;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;

public class TestMobileBrowser {


  private Performer performer;
  private Actor actor;

  @Mock
  BrowserConfig browserConfigMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  AndroidDriver driverMock;

  MobileBrowser mobileBrowser;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  WebElement webElementMock;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  Point pointMock;

  @Mock
  RemoteWebDriverOptionsMock webDriverOptionsMock;

  @Mock
  RemoteTestNavigationMock webDriverNavigationMock;

  @Mock
  RemoteTargetLocatorMock webDriverTargetLocatorMock;

  @Mock
  AppiumLoader loaderMock;

  @Captor
  ArgumentCaptor<String> urlCaptor;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  Actions actions;

  Element element = Element.found(By.css("button#accept-cookies"));

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);

    when(loaderMock.driver()).thenReturn(Try.success(driverMock));
    when(loaderMock.isLocalExecution()).thenReturn(true);
    when(loaderMock.downloadPath()).thenReturn(Option.none());
    when(loaderMock.actions()).thenReturn(Try.success(actions));
    when(loaderMock.stopVideoRecording()).thenReturn(Try.success(null));

    when(driverMock.findElements(any(org.openqa.selenium.By.class))).thenReturn(List.of(webElementMock));
    when(driverMock.findElement(any(org.openqa.selenium.By.class))).thenReturn(webElementMock);

    when(driverMock.manage()).thenReturn(webDriverOptionsMock);
    when(driverMock.navigate()).thenReturn(webDriverNavigationMock);
    when(driverMock.switchTo()).thenReturn(webDriverTargetLocatorMock);

    when(pointMock.getX()).thenReturn(1);
    when(pointMock.getY()).thenReturn(1);
    when(webElementMock.getLocation()).thenReturn(pointMock);

    // MobileBrowser explizit mit Factory-Methode und Mocks initialisieren
    mobileBrowser = new MobileBrowser(loaderMock, browserConfigMock, none());

    actor = Actor.named("Test Actor").whoCan(BrowseTheWeb.with(mobileBrowser));
    performer = Performer.of(actor);

  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void testNavigateTo() throws ActivityError {

    String url = "theUrl";

    Navigate.to(url).runAs(performer);

    verify(driverMock, times(1)).navigate();
    verify(webDriverNavigationMock, times(1)).to(urlCaptor.capture());
    assertThat(urlCaptor.getValue(), equalTo(url));

  }

  @Test
  public void testClickOnElement() throws ActivityError {

    Click.on(element).runAs(performer);

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

    Enter.text("text").into(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).sendKeys("text");
  }

  @Test
  public void testEnterTextIntoClearedElement() throws ActivityError {

    Enter.text("text").intoCleared(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).sendKeys("text");
  }

  @Test
  public void testTextOfElement() throws ActivityError {

    Text.of(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getText();
  }

  @Test
  public void testValueOf() throws ActivityError {

    Value.of(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getDomProperty("value");
  }

  @Test
  public void testAttributeOf() throws ActivityError {

    Attribute.named("test").of(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(1)).getDomAttribute("test");
  }

  @Test
  public void testElementState() throws ActivityError {

    ElementState.of(element).runAs(performer);

    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(webElementMock, times(2)).isDisplayed();
    verify(webElementMock, times(1)).isEnabled();
  }

  @Test
  public void testTitle() throws ActivityError {

    Title.ofPage().runAs(performer);

    verify(driverMock).getTitle();
  }

  @Test
  public void testUrl() throws ActivityError {

    Url.ofPage().runAs(performer);

    verify(driverMock).getCurrentUrl();
  }

  @Test
  public void testGetCookie() throws ActivityError {

    org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie("testCookie", "testCookieValue");
    when(webDriverOptionsMock.getCookieNamed("testCookie")).thenReturn(cookie);

    GetCookie.named("testCookie").runAs(performer);

    verify(webDriverOptionsMock).getCookieNamed("testCookie");
  }

  @Test
  public void testGetAllCookies() throws ActivityError {

    org.openqa.selenium.Cookie cookie1 = new org.openqa.selenium.Cookie("testCookie1", "testCookieValue");
    org.openqa.selenium.Cookie cookie2 = new org.openqa.selenium.Cookie("testCookie2", "testCookieValue");
    org.openqa.selenium.Cookie cookie3 = new org.openqa.selenium.Cookie("testCookie3", "testCookieValue");
    when(webDriverOptionsMock.getCookies()).thenReturn(Set.of(cookie1, cookie2, cookie3));

    Either<ActivityError, io.vavr.collection.List<Cookie>> cookies = GetAllCookies.fromBrowser()
        .runAs(actor);

    verify(webDriverOptionsMock).getCookies();

    assertThat("cookies are returned", cookies.isRight(), equalTo(true));
    assertThat("get list of 3 cookies", cookies.get().length(), equalTo(3));
  }

  @Test
  public void testAddCookie() throws ActivityError {
    org.openqa.selenium.Cookie webCookie = new org.openqa.selenium.Cookie("cookieName", "CookieValue");

    Cookie cookie = Cookie.of("cookieName", "CookieValue");

    AddCookie.toBrowser(cookie).runAs(performer);

    verify(webDriverOptionsMock).addCookie(webCookie);
  }

  @Test
  public void testDeleteCookie() throws ActivityError {
    DeleteCookie.named("cookieName").runAs(performer);

    verify(webDriverOptionsMock).deleteCookieNamed("cookieName");
  }

  @Test
  public void testDeleteAllCookies() throws ActivityError {
    DeleteAllCookies.fromBrowser().runAs(performer);

    verify(webDriverOptionsMock).deleteAllCookies();
  }

  @Test
  public void testTakePageScreenshot() throws ActivityError {

    File screenShotDummy = new File("screenShotDummyPath");
    when(driverMock.getScreenshotAs(any())).thenReturn(screenShotDummy);

    Either<ActivityError, File> screenshot = TakeScreenshot.ofPage()
        .runAs(actor);

    assertThat("screenshot is taken", screenshot.isRight(), equalTo(true));
    verify(driverMock).getScreenshotAs(any());
    assertThat("screenshots are equal", screenshot.get(), equalTo(screenShotDummy));
  }

  @Test
  public void testTakeElementScreenshot() throws ActivityError {

    File screenShotDummy = new File("elementScreenshotDummy");
    when(webElementMock.getScreenshotAs(any())).thenReturn(screenShotDummy);

    Either<ActivityError, File> screenshot = TakeScreenshot.ofElement(element)
        .runAs(actor);

    assertThat("screenshot is taken", screenshot.isRight(), equalTo(true));
    verify(webElementMock).getScreenshotAs(any());
    assertThat("screenshots are equal", screenshot.get(), equalTo(screenShotDummy));
  }

  @Test
  public void testDrawShape() throws ActivityError {

    when(pointMock.moveBy(1, 1)).thenReturn(new Point(1001,1001));


    Shape shape = Shape.startingAt(StartPoint.on(1, 1))
        .moveTo(Move.right(10));

    Draw.shape(shape).on(element).runAs(performer);

    verify(webElementMock, times(1)).getLocation();

    verify(actions, times(1)).moveToLocation(1001,1001);
    verify(actions, times(1)).clickAndHold();
    verify(actions, times(1)).moveByOffset(10,0);
    verify(actions, times(1)).release();

  }

  @Test
  public void testDrawShapes() throws ActivityError {

    when(pointMock.moveBy(1, 1)).thenReturn(new Point(101,101));

    Shape right = Shape.startingAt(StartPoint.on(1, 1))
        .moveTo(Move.right(10));

    Shape down = Shape.startingAt(StartPoint.on(1, 1))
        .moveTo(Move.down(10));

    Draw.shapes(right, down).on(element).runAs(performer);

    verify(webElementMock, times(2)).getLocation();

    verify(actions, times(2)).moveToLocation(101,101);
    verify(actions, times(2)).clickAndHold();
    verify(actions, times(1)).moveByOffset(10,0);
    verify(actions, times(1)).moveByOffset(0,10);
    verify(actions, times(2)).release();


  }

  @Test
  public void testUploadFile() throws ActivityError, IOException {
    Path file = Paths.get("").resolve("newFile.txt").toAbsolutePath();

    SetUpload.file(file).to(element).runAs(performer);


    verify(driverMock, times(1)).findElements(any(org.openqa.selenium.By.class));
    verify(driverMock, times(1)).pushFile(any(String.class), any(File.class));
    verify(webElementMock, times(1)).sendKeys("/sdcard/Download/newFile.txt");

  }

  @Test
  public void testPageRefresh() throws ActivityError {
    RefreshCurrentBrowser.page().runAs(performer);
    verify(webDriverNavigationMock, times(1)).refresh();
  }

  @Test
  public void testNavigateBack() throws ActivityError {
    Navigate.back().runAs(performer);
    verify(webDriverNavigationMock, times(1)).back();

  }

  @Test
  public void testNavigateForward() throws ActivityError {
    Navigate.forward().runAs(performer);
    verify(webDriverNavigationMock, times(1)).forward();
  }

  @Test
  public void testSwitchToNewBrowserTab() throws ActivityError {
    SwitchToNewBrowser.tab().runAs(performer);

    verify(driverMock, times(1)).switchTo();
    verify(webDriverTargetLocatorMock, times(1)).newWindow(WindowType.TAB);

  }

  @Test
  public void testSwitchToNewBrowserWindow() throws ActivityError {
    SwitchToNewBrowser.window().runAs(performer);

    verify(driverMock, times(1)).switchTo();
    verify(webDriverTargetLocatorMock, times(1)).newWindow(WindowType.WINDOW);

  }

  @Test
  public void testSwitchToBrowserByTitle() throws ActivityError {
    SwitchToBrowser.havingTitle("new Window").runAs(performer);

    verify(driverMock, times(1)).getWindowHandles();
    verify(driverMock, times(1)).switchTo();

  }

  @Test
  public void testSwitchToBrowserByIndex() throws ActivityError {

    when(driverMock.getWindowHandles()).thenReturn(Set.of("1", "2", "3"));

    SwitchToBrowser.byIndex(1).runAs(performer);

    verify(driverMock, times(1)).getWindowHandles();

  }

  @Test
  public void testNumberOfOpenTabs() throws ActivityError {

    when(driverMock.getWindowHandles()).thenReturn(Set.of("1", "2", "3"));

    NumberOfBrowser.tabsAndWindows().runAs(performer);

    verify(driverMock, times(1)).getWindowHandles();

  }

  @Test
  public void testKeyPress() throws ActivityError {
    DoKey.press(Key.ENTER).runAs(performer);

    verify(actions, times(1)).sendKeys(eq(org.openqa.selenium.Keys.ENTER));
    verify(actions, times(1)).perform();

//    verify(mobileBrowser).executeKeyActions(any());

  }

  @Test
  public void testExecuteJavaScriptInBrowser() throws ActivityError {

    String jsCode = "alert('Hello World')";
    ExecuteJavaScript.onBrowser(jsCode).runAs(performer);

    ArgumentCaptor<String> jsCaptor = ArgumentCaptor.forClass(String.class);
    verify(driverMock, times(1)).executeScript(jsCaptor.capture(), any());
    assertThat(jsCaptor.getValue(), equalTo(jsCode));
  }

  @Test
  public void testExecuteJavaScriptOnElement() throws ActivityError {
    String jsCode = "alert('Hello World')";

    ExecuteJavaScript.onElement(jsCode, element).runAs(performer);

    ArgumentCaptor<String> jsCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Object> elementCaptor = ArgumentCaptor.forClass(Object.class);

    verify(driverMock, times(1)).executeScript(jsCaptor.capture(), elementCaptor.capture());

    assertThat(jsCaptor.getValue(), equalTo(jsCode));
    assertThat(elementCaptor.getValue(), equalTo(webElementMock));
  }

  @Test
  public void testActorStageCleaning() {

    actor.cleansStage();

    verify(driverMock, times(1)).quit();
  }

}
