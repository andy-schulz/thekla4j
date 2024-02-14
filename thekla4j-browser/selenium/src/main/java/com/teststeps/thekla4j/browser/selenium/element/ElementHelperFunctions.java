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

  private static final String HIGHLIGHT_STYLE = "border: 2px solid red;";

  private static final Function3<RemoteWebDriver, WebElement, String, Object> setBorder =
      (driver, element, style) ->
          ((JavascriptExecutor) driver).executeScript(
              String.format("arguments[0].setAttribute('style', '%s');", style),
              element);

  public static Function2<RemoteWebDriver, HighlightContext, Function1<WebElement, WebElement>> highlightElement =
      (driver, hlx) -> element -> {

        Boolean highlightElements = Boolean.parseBoolean(
            Thekla4jProperty.of(DefaultThekla4jBrowserProperties.HIGHLIGHT_ELEMENTS.property()));

        if (!highlightElements) {
          return element;
        }

        if (!Objects.isNull(hlx.lastChangedWebElement.get()) && hlx.lastChangedWebElement.get().isDisplayed()) {
          Try.of(() ->
              hlx.lastChangedWebElement.get().isDisplayed() ?
                  setBorder.apply(driver, hlx.lastChangedWebElement.get(), hlx.lastChangedElementStyle.get()) :
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
