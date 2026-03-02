/**
 * Playwright-based implementation of the thekla4j browser screenplay pattern.
 *
 * <p>This package provides a Playwright implementation of the {@link com.teststeps.thekla4j.browser.core.Browser}
 * interface, following the same architectural pattern as the Selenium implementation.</p>
 *
 * <h2>Key Classes</h2>
 * <ul>
 * <li>{@link com.teststeps.thekla4j.browser.playwright.PlaywrightBrowserFactory} - Entry point for creating browser
 * instances</li>
 * <li>{@link com.teststeps.thekla4j.browser.playwright.PlaywrightBrowser} - Browser interface implementation</li>
 * <li>{@link com.teststeps.thekla4j.browser.playwright.PlaywrightLoader} - Manages Playwright lifecycle</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Quick start with local Chromium
 * Browser browser = PlaywrightBrowserFactory.localChromium();
 *
 * // Builder pattern with configuration
 * Browser browser = PlaywrightBrowserFactory.browser()
 *     .updateBrowserConfig(config -> config.withHeadless(true))
 *     .build();
 * }</pre>
 */
package com.teststeps.thekla4j.browser.playwright;
