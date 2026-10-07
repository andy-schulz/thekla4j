package com.teststeps.thekla4j.browser.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import com.teststeps.thekla4j.browser.config.BrowserConfig;
import com.teststeps.thekla4j.browser.config.BrowserName;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumGridConfig;
import io.vavr.collection.HashMap;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;

public class TestBrowserPreferences {

  private static final Map<String, Object> PREFS = LinkedHashMap.of(
    "intl.accept_languages", "de-DE,de",
    "media.autoplay.default", 0,
    "dom.webnotifications.enabled", false);

  private static final SeleniumGridConfig GRID = SeleniumGridConfig.of("http://localhost:4444/wd/hub");

  private static Try<MutableCapabilities> localOptions(BrowserConfig config) {
    return SeleniumLoader.of(config, Option.none(), Option.none()).loadOptions();
  }

  private static Try<MutableCapabilities> remoteOptions(BrowserConfig config) {
    return SeleniumLoader.of(config, Option.of(GRID), Option.none()).loadOptions();
  }

  /**
   * reads the preferences out of the browser specific option of the given capabilities
   */
  @SuppressWarnings("unchecked")
  private static Map<String, Object> preferencesOf(MutableCapabilities capabilities, String browserOption) {
    java.util.Map<String, Object> options = (java.util.Map<String, Object>) capabilities.asMap().get(browserOption);

    return HashMap.ofAll((java.util.Map<String, Object>) options.get("prefs"));
  }

  @Test
  @DisplayName("configured preferences are passed to Firefox as firefox options")
  public void firefoxPreferences() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.FIREFOX).withPrefs(PREFS)).get(),
      "moz:firefoxOptions");

    assertThat("a string preference is passed on", prefs.get("intl.accept_languages").get(), equalTo("de-DE,de"));
    assertThat("an integer preference keeps its type", prefs.get("media.autoplay.default").get(), equalTo(0));
    assertThat("a boolean preference keeps its type", prefs.get("dom.webnotifications.enabled").get(), equalTo(false));
  }

  @Test
  @DisplayName("configured preferences are passed to Chrome as experimental option")
  public void chromePreferences() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.CHROME).withPrefs(PREFS)).get(),
      "goog:chromeOptions");

    assertThat("all preferences are passed on", prefs.filterKeys(PREFS.keySet()::contains).size(), equalTo(3));
    assertThat("an integer preference keeps its type", prefs.get("media.autoplay.default").get(), equalTo(0));
  }

  @Test
  @DisplayName("configured preferences are passed to Edge as experimental option")
  public void edgePreferences() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.EDGE).withPrefs(PREFS)).get(),
      "ms:edgeOptions");

    assertThat("all preferences are passed on", prefs.filterKeys(PREFS.keySet()::contains).size(), equalTo(3));
    assertThat("a boolean preference keeps its type", prefs.get("dom.webnotifications.enabled").get(), equalTo(false));
  }

  @Test
  @DisplayName("preferences configured for Safari fail with a clear message")
  public void safariPreferencesAreRejected() {

    Try<MutableCapabilities> options = localOptions(BrowserConfig.of(BrowserName.SAFARI).withPrefs(PREFS));

    assertThat("setting preferences for safari fails", options.isFailure(), equalTo(true));
    assertThat("the error message names the reason",
      options.getCause().getMessage(),
      containsString("Safari does not support browser preferences"));
  }

  @Test
  @DisplayName("Safari starts when no preferences are configured")
  public void safariWithoutPreferences() {

    assertThat("safari without preferences is loaded",
      localOptions(BrowserConfig.of(BrowserName.SAFARI)).isSuccess(),
      equalTo(true));
  }

  @Test
  @DisplayName("the download directory and the configured preferences are both applied for Chrome")
  public void chromeDownloadDirectoryAndConfiguredPreferences() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.CHROME).withEnableFileDownload(true).withPrefs(PREFS)).get(),
      "goog:chromeOptions");

    assertThat("the configured preferences survive the download setup",
      prefs.get("intl.accept_languages").get(), equalTo("de-DE,de"));
    assertThat("the download directory survives the configured preferences",
      prefs.get("download.default_directory").isDefined(), equalTo(true));
    assertThat("the download prompt is switched off",
      prefs.get("download.prompt_for_download").get(), equalTo(false));
  }

  @Test
  @DisplayName("the download preferences reach the Firefox options")
  public void firefoxDownloadPreferences() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.FIREFOX).withEnableFileDownload(true)).get(),
      "moz:firefoxOptions");

    assertThat("the download directory is set", prefs.get("browser.download.dir").isDefined(), equalTo(true));
    assertThat("the download folder list is set", prefs.get("browser.download.folderList").get(), equalTo(2));
    assertThat("the download manager is switched off",
      prefs.get("browser.download.manager.showWhenStarting").get(), equalTo(false));
  }

  @Test
  @DisplayName("a preference managed by thekla4j cannot be overwritten by the browser configuration")
  public void managedPreferenceWinsOverConfiguredPreference() {

    Map<String, Object> prefs = preferencesOf(
      localOptions(BrowserConfig.of(BrowserName.CHROME)
          .withEnableFileDownload(true)
          .withPrefs(LinkedHashMap.of("download.default_directory", "/a/path/of/my/own")))
          .get(),
      "goog:chromeOptions");

    assertThat("the download directory of thekla4j is used",
      prefs.get("download.default_directory").get(), not(equalTo("/a/path/of/my/own")));
  }

  @Test
  @DisplayName("file download configured for a local Safari fails with a clear message")
  public void safariDownloadIsRejected() {

    Try<MutableCapabilities> options = localOptions(BrowserConfig.of(BrowserName.SAFARI).withEnableFileDownload(true));

    assertThat("enabling file download for a local safari fails", options.isFailure(), equalTo(true));
    assertThat("the error message names the reason",
      options.getCause().getMessage(),
      containsString("Safari does not support setting a download directory"));
  }

  @Test
  @DisplayName("file upload is supported by all browsers, locally and on the grid")
  public void fileUploadIsSupportedByAllBrowsers() {

    List.of(BrowserName.CHROME, BrowserName.EDGE, BrowserName.FIREFOX, BrowserName.SAFARI)
        .forEach(browserName -> {
          BrowserConfig config = BrowserConfig.of(browserName).withEnableFileUpload(true);

          assertThat("file upload is supported for a local " + browserName, localOptions(config).isSuccess(), equalTo(true));
          assertThat("file upload is supported for a remote " + browserName, remoteOptions(config).isSuccess(), equalTo(true));
        });
  }

  @Test
  @DisplayName("enabling file upload does not enable file download")
  public void fileUploadDoesNotEnableDownloads() {

    assertThat("no download capability is set when only file upload is enabled",
      remoteOptions(BrowserConfig.of(BrowserName.CHROME).withEnableFileUpload(true)).get()
          .asMap()
          .containsKey("se:downloadsEnabled"),
      equalTo(false));
  }
}
