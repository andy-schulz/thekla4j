package com.teststeps.thekla4j.browser.selenium.element;

import com.teststeps.thekla4j.browser.core.properties.DefaultThekla4jBrowserProperties;
import com.teststeps.thekla4j.commons.properties.Thekla4jProperty;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

@Log4j2(topic = "Element Helper Operations")
public class ElementHelperFunctions {

  private static final String HIGHLIGHT_STYLE = ";border: 2px solid red;";

  private static final String SCRIPT =
    "var style = arguments[0].getAttribute('style')\n" +
      "arguments[0].setAttribute('style', style ? style + '%s' : '%s')";

  private static final Function3<RemoteWebDriver, WebElement, String, Object> setBorder =
    (driver, element, style) ->
      ((JavascriptExecutor) driver).executeScript(
        String.format(SCRIPT, style, style),
//              String.format("arguments[0].setAttribute('style', '%s');", style),
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

  public static Function1<RemoteWebDriver, Function1<WebElement, Try<WebElement>>> scrollIntoView =
    driver -> element ->
      Try.of(() -> (JavascriptExecutor) driver)
        .map(d -> d.executeScript("arguments[0].scrollIntoView({block: 'center',inline: 'center', behavior: 'smooth'});", element))
        .map(__ -> element);


  public static Function2<RemoteWebDriver, HighlightContext, Function1<WebElement, WebElement>> highlightElement =
    (driver, hlx) -> element -> {

      Boolean highlightElements = Boolean.parseBoolean(
        Thekla4jProperty.of(DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS.property()));

      if (!highlightElements) {
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
