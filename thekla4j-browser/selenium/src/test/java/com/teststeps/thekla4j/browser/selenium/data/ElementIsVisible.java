package com.teststeps.thekla4j.browser.selenium.data;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.activities.ExecuteJavaScript;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ElementIsVisible extends SupplierTask<Boolean> {

  Element element;
  Element area;

  private final String script =
      """
            function isElementInViewport(element, scrollContainer) {

              if (!element || !scrollContainer) {
                 return false;
              }

              const elementRect = element.getBoundingClientRect();
              const containerRect = scrollContainer.getBoundingClientRect();

              // Check if the element is within the visible bounds of the scroll container
              return (
                elementRect.top >= containerRect.top &&
                elementRect.bottom <= containerRect.bottom &&
                elementRect.left >= containerRect.left &&
                elementRect.right <= containerRect.right
              );
            }

            return isElementInViewport(arguments[0], arguments[1]);
          """;

  @Override
  protected Either<ActivityError, Boolean> performAs(Actor actor) {

    return actor.attemptsTo(
      ExecuteJavaScript.onElement(script, element, area))
        .map(x -> {
          if (x instanceof Boolean) {
            return (Boolean) x;
          } else {
            return false;
          }
        });
  }

  public static ElementIsVisible withinArea(Element element, Element area) {
    return new ElementIsVisible(element, area);
  }
}
