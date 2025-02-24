package com.teststeps.thekla4j.browser.selenium.element;

import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.AUTO_SCROLL_VERTICAL;
import static com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS;

/**
 * Helper functions for element operations
 */
@Log4j2(topic = "Element Helper Operations")
public class ElementHelperFunctions {

  private static final String HIGHLIGHT_STYLE = ";border: 2px solid red;";

  private static final String SCRIPT =
    "var style = arguments[0].getAttribute('style')\n" +
      "arguments[0].setAttribute('style', style ? style + '%s' : '%s')";

  private static final String SCROLL_INTO_VIEW_SCRIPT =
    """
      arguments[0].scrollIntoView({block: '%s',inline: 'center', behavior: 'instant'});
    """;

  private final String SCROLL_INTO_VIEW_SCRIPT2 =
    """
      var element = arguments[0];
      var rect = element.getBoundingClientRect();
      if(!(rect.top >= 0 && rect.left >= 0 &&
      rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
      rect.right <= (window.innerWidth || document.documentElement.clientWidth))) {
      element.scrollIntoView({block: 'center',inline: 'center', behavior: 'instant'});
      }""";

  private static final Function3<RemoteWebDriver, WebElement, String, Object> setBorder =
    (driver, element, style) ->
      ((JavascriptExecutor) driver).executeScript(
        String.format(SCRIPT, style, style),
        element);


  private static final Function3<RemoteWebDriver, WebElement, String, Object> setStyle =
    (driver, element, style) -> {
      if (Objects.isNull(style) || style.isEmpty()) {
        ((JavascriptExecutor) driver).executeScript(
          "arguments[0].removeAttribute('style');",
          element);
      } else {
        ((JavascriptExecutor) driver).executeScript(
          String.format("arguments[0].setAttribute('style', '%s');", style),
          element);
      }
      return null;
    };

  private static final Function0<String> getVerticalScrolling =
    () -> switch (AUTO_SCROLL_VERTICAL.value()) {
      case "top" -> "start";
      case "bottom" -> "end";
      default -> "center";
    };

  /**
   * Scroll the element into view, used before interacting with the element
   */
  public static Function1<RemoteWebDriver, Function1<WebElement, Try<WebElement>>> scrollIntoView =
    driver -> element ->
      DefaultThekla4jBrowserProperties.AUTO_SCROLL_ENABLED.value().equals("true") ?

        Try.of(() -> (JavascriptExecutor) driver)
          .mapTry(d -> d.executeScript("arguments[0].scrollIntoView({block: '%s',inline: 'center', behavior: 'instant'});"
            .formatted(getVerticalScrolling.apply()), element))
          .map(__ -> element) :

        Try.success(element);


  /**
   * Highlight the element, the highlight is applied before interacting with the element
   * and removed before the next element is highlighted
   */
  public static Function3<RemoteWebDriver, HighlightContext, Boolean, Function1<WebElement, WebElement>> highlightElement =
    (driver, hlx, elementHighlight) -> element -> {

      if (!elementHighlight || Objects.isNull(hlx)) {
        return element;
      }

      if (!Boolean.parseBoolean(HIGHLIGHT_ELEMENTS.value())) {
        return element;
      }

      if (!Objects.isNull(hlx.lastChangedWebElement.get())) {
        Try.of(() ->
          hlx.lastChangedWebElement.get().isDisplayed() ?
            setStyle.apply(driver, hlx.lastChangedWebElement.get(), hlx.lastChangedElementStyle.get()) :
            null);
      }

      hlx.lastChangedElementStyle.remove();
      hlx.lastChangedWebElement.remove();

      Try.of(() -> {
        if (element.isDisplayed()) {

          String style = element.getAttribute("style");
          hlx.lastChangedElementStyle.set(style);

          setBorder.apply(driver, element, HIGHLIGHT_STYLE);

          hlx.lastChangedWebElement.set(element);
        }
        return null;
      });


      return element;
    };
}
