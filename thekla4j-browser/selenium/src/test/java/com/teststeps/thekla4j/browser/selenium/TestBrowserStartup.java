package com.teststeps.thekla4j.browser.selenium;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

@DisplayName("Browser Startup Tests")
public class TestBrowserStartup {

  @Mock
  private BrowserConfig mockBrowserConfig;

  @Mock
  private RemoteWebDriver mockRemoteWebDriver;

  @Mock
  private Options mockOptions;

  @Mock
  private Window mockWindow;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup mock chain: driver -> manage() -> window()
    when(mockRemoteWebDriver.manage()).thenReturn(mockOptions);
    when(mockOptions.window()).thenReturn(mockWindow);
    when(mockBrowserConfig.browserName()).thenReturn(BrowserName.CHROME);
  }

  @Test
  @DisplayName("Should maximize window when maximizeWindow is true in startup config")
  void shouldMaximizeWindowWhenMaximizeWindowIsTrue() {
    BrowserStartupConfig startupConfig = BrowserStartupConfig.startMaximized();

    assertTrue(startupConfig.maximizeWindow(), "maximizeWindow should be true");
    assertEquals("", startupConfig.testName(), "testName should be empty for startMaximized");
  }

  @Test
  @DisplayName("Should not maximize window when maximizeWindow is false in startup config")
  void shouldNotMaximizeWindowWhenMaximizeWindowIsFalse() {
    BrowserStartupConfig startupConfig = BrowserStartupConfig.testName("test").withMaximizeWindow(false);

    assertFalse(startupConfig.maximizeWindow(), "maximizeWindow should be false");
  }

  @Test
  @DisplayName("Should create startup config with maximize window enabled")
  void shouldCreateStartupConfigWithMaximizeWindowEnabled() {
    BrowserStartupConfig startupConfig = BrowserStartupConfig.startMaximized();

    assertTrue(startupConfig.maximizeWindow(), "maximizeWindow should be true for startMaximized config");
    assertEquals("", startupConfig.testName(), "testName should be empty string for startMaximized config");
  }

  @Test
  @DisplayName("Should create startup config with test name and no maximize by default")
  void shouldCreateStartupConfigWithTestNameAndNoMaximizeByDefault() {
    String testName = "MyBrowserTest";

    BrowserStartupConfig startupConfig = BrowserStartupConfig.testName(testName);

    assertEquals(testName, startupConfig.testName(), "testName should match the provided name");
    assertFalse(startupConfig.maximizeWindow(), "maximizeWindow should be false by default");
  }

  @Test
  @DisplayName("Should be able to modify maximize window setting using with method")
  void shouldBeAbleToModifyMaximizeWindowSettingUsingWithMethod() {
    BrowserStartupConfig originalConfig = BrowserStartupConfig.testName("test");

    BrowserStartupConfig modifiedConfig = originalConfig.withMaximizeWindow(true);

    assertFalse(originalConfig.maximizeWindow(), "Original config should have maximizeWindow as false");
    assertTrue(modifiedConfig.maximizeWindow(), "Modified config should have maximizeWindow as true");
    assertEquals(originalConfig.testName(), modifiedConfig.testName(), "testName should remain the same");
  }

  @Test
  @DisplayName("Should create SeleniumLoader with startup config and verify maximize behavior")
  void shouldCreateSeleniumLoaderWithStartupConfigAndVerifyMaximizeBehavior() {
    BrowserStartupConfig startupConfigWithMaximize = BrowserStartupConfig.startMaximized();
    BrowserStartupConfig startupConfigWithoutMaximize = BrowserStartupConfig.testName("test");

    SeleniumLoader loaderWithMaximize = SeleniumLoader.of(
      mockBrowserConfig,
      Option.none(),
      Option.of(startupConfigWithMaximize));

    SeleniumLoader loaderWithoutMaximize = SeleniumLoader.of(
      mockBrowserConfig,
      Option.none(),
      Option.of(startupConfigWithoutMaximize));

    assertNotNull(loaderWithMaximize, "SeleniumLoader with maximize should be created");
    assertNotNull(loaderWithoutMaximize, "SeleniumLoader without maximize should be created");
    assertTrue(loaderWithMaximize.isLocalExecution(), "Should be local execution when no grid config provided");
    assertTrue(loaderWithoutMaximize.isLocalExecution(), "Should be local execution when no grid config provided");
  }

  @Test
  @DisplayName("Should handle empty startup config gracefully")
  void shouldHandleEmptyStartupConfigGracefully() {
    Option<BrowserStartupConfig> emptyStartupConfig = Option.none();

    assertDoesNotThrow(() -> {
      SeleniumLoader seleniumLoader = SeleniumLoader.of(mockBrowserConfig, Option.none(), emptyStartupConfig);
      assertNotNull(seleniumLoader, "SeleniumLoader should be created even with empty startup config");
    }, "Should not throw exception when startup config is empty");
  }

  @Test
  @DisplayName("Should verify startup config properties are immutable")
  void shouldVerifyStartupConfigPropertiesAreImmutable() {
    String testName = "ImmutableTest";
    Boolean maximizeWindow = true;

    BrowserStartupConfig config = new BrowserStartupConfig(testName, maximizeWindow);

    assertEquals(testName, config.testName(), "testName should be accessible");
    assertEquals(maximizeWindow, config.maximizeWindow(), "maximizeWindow should be accessible");

    BrowserStartupConfig newConfig = config.withTestName("NewTestName").withMaximizeWindow(false);

    assertEquals(testName, config.testName(), "Original testName should remain unchanged");
    assertEquals(maximizeWindow, config.maximizeWindow(), "Original maximizeWindow should remain unchanged");

    assertEquals("NewTestName", newConfig.testName(), "New config should have updated testName");
    assertFalse(newConfig.maximizeWindow(), "New config should have updated maximizeWindow");
  }

  @Test
  @DisplayName("Should test different maximize window configurations")
  void shouldTestDifferentMaximizeWindowConfigurations() {
    BrowserStartupConfig config1 = BrowserStartupConfig.testName("test1");
    assertFalse(config1.maximizeWindow(), "Default testName config should not maximize");

    BrowserStartupConfig config2 = BrowserStartupConfig.startMaximized();
    assertTrue(config2.maximizeWindow(), "startMaximized config should maximize");

    BrowserStartupConfig config3 = new BrowserStartupConfig("ManualTest", true);
    assertTrue(config3.maximizeWindow(), "Manual config with true should maximize");
    assertEquals("ManualTest", config3.testName(), "Manual config should preserve testName");

    BrowserStartupConfig config4 = config1.withMaximizeWindow(true);
    assertTrue(config4.maximizeWindow(), "Modified config should maximize");
    assertEquals("test1", config4.testName(), "Modified config should preserve original testName");
  }

  @Test
  @DisplayName("Should verify SeleniumLoader factory methods work correctly")
  void shouldVerifySeleniumLoaderFactoryMethodsWorkCorrectly() {
    BrowserStartupConfig startupConfig = BrowserStartupConfig.startMaximized();

    SeleniumLoader loader1 = SeleniumLoader.of(mockBrowserConfig, Option.none(), Option.of(startupConfig));
    SeleniumLoader loader2 = SeleniumLoader.of(mockBrowserConfig, Option.none(), Option.none());

    assertNotNull(loader1, "Loader with startup config should be created");
    assertNotNull(loader2, "Loader without startup config should be created");
    assertTrue(loader1.isLocalExecution(), "Should default to local execution");
    assertTrue(loader2.isLocalExecution(), "Should default to local execution");
  }

  @Test
  @DisplayName("should verify maximize is executed after driver creation")
  void maximizeIsExecutedAfterCreation() throws Exception {
    // Arrange
    BrowserStartupConfig startupConfig = BrowserStartupConfig.startMaximized();

    // Create a mock grid config to force remote execution (which uses createDriver)
    SeleniumGridConfig mockGridConfig = mock(SeleniumGridConfig.class);
    when(mockGridConfig.remoteUrl()).thenReturn("http://localhost:4444/wd/hub");

    SeleniumLoader seleniumLoader = spy(SeleniumLoader.of(
      mockBrowserConfig,
      Option.of(mockGridConfig),
      Option.of(startupConfig)));

    // Mock the createDriver function using reflection to access the private field
    Function2<String, Capabilities, Try<RemoteWebDriver>> mockCreateDriver =
        (url, capabilities) -> Try.success(mockRemoteWebDriver);

    // Use reflection to set the createDriver field
    Field createDriverField = SeleniumLoader.class.getDeclaredField("createDriver");
    createDriverField.setAccessible(true);
    createDriverField.set(seleniumLoader, mockCreateDriver);

    // Mock loadOptions to return some dummy capabilities
    doReturn(Try.success(new ChromeOptions())).when(seleniumLoader).loadOptions();

    // Act - Call the driver() method which should trigger the maximize functionality
    Try<RemoteWebDriver> driverResult = seleniumLoader.driver();

    // Assert
    assertTrue(driverResult.isSuccess(), "Driver creation should be successful");
    assertEquals(mockRemoteWebDriver, driverResult.get(), "Should return the mocked driver");
    assertTrue(startupConfig.maximizeWindow(), "Startup config should have maximize window enabled");
    assertFalse(seleniumLoader.isLocalExecution(), "Should be remote execution with grid config");

    // Verify that the maximize chain was called on the mocked driver
    // The real implementation applies driverUpdates which includes the maximize call
    verify(mockRemoteWebDriver, times(1)).manage();
    verify(mockOptions, times(1)).window();
    verify(mockWindow, times(1)).maximize();
  }
}
