package com.teststeps.thekla4j.browser.playwright.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Pure Playwright test to verify the download mechanism works correctly
 * without any thekla4j abstraction layer.
 */
public class IT_Download_Raw {

  @Test
  public void downloadFileWithPlaywrightDirectly() throws Exception {

    try (Playwright playwright = Playwright.create()) {

      BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
          .setHeadless(false);

      try (Browser browser = playwright.chromium().launch(launchOptions)) {

        BrowserContext context = browser.newContext(
          new Browser.NewContextOptions().setAcceptDownloads(true));

        Page page = context.newPage();

        page.navigate("http://localhost:3000/download/");

        System.out.println("Page title: " + page.title());
        System.out.println("Page URL:   " + page.url());

        // Use waitForDownload to intercept the download synchronously
        Download download = page.waitForDownload(() -> {
          page.locator("#downloadFile").click();
        });

        System.out.println("Suggested filename: " + download.suggestedFilename());
        System.out.println("Download URL:       " + download.url());

        // Save to a temp file
        Path targetPath = Files.createTempFile("playwright-download-test-", "-" + download.suggestedFilename());
        download.saveAs(targetPath);

        System.out.println("Saved to: " + targetPath);
        System.out.println("File size: " + Files.size(targetPath) + " bytes");
        System.out.println("File content: " + Files.readString(targetPath));

        assertTrue(Files.exists(targetPath), "Downloaded file should exist");
        assertTrue(Files.size(targetPath) > 0, "Downloaded file should not be empty");

        context.close();
      }
    }
  }
}
