package com.teststeps.thekla4j.browser.selenium.status;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.status.ElementStatusType;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

/**
 * The status of a Selenium Element
 */
@Log4j2(topic = "SeleniumElementStatus")
public enum SeleniumElementStatus {

  /**
   * The element is enabled
   */
  IS_ENABLED {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element::isEnabled;
    }
  },

  /**
   * The element is visible
   */
  IS_VISIBLE {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element::isDisplayed;
    }
  },

  /**
   * The element is clickable
   */
  IS_CLICKABLE {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element.isDisplayed() ? element::isEnabled : () -> false;
    }
  },

  /**
   * The element is present
   */
  IS_NOT_STALE {
    @Override
    public CheckElementStatus resolve(WebElement element) {

      return () -> Try.of(element::isEnabled)
          .onFailure(x -> log.error("StaleElementReferenceException check: " + x.getMessage()))
          .transform(status -> status.isFailure() && status.getCause() instanceof StaleElementReferenceException ?
              Try.success(false) :
              Try.success(true))
          .getOrElse(false);
    }
  };

  private static SeleniumElementStatus forWaiter(ElementStatusType elementStatusType) {
    return SeleniumElementStatus.valueOf(elementStatusType.name());
  }

  /**
   * Get the SeleniumElementStatus for an Element
   *
   * @param element - the element
   * @return - the SeleniumElementStatus
   */
  public static SeleniumElementStatus forElement(Element element) {
    return SeleniumElementStatus.forWaiter(element.waiter().type());
  }

  /**
   * Resolve the status of an element, is implemented for every status type
   *
   * @param element - the element
   * @return - the status of the element
   */
  public abstract CheckElementStatus resolve(WebElement element);

  /**
   * Check the status of an element executes the implemented resolve method
   *
   * @param element - the element
   * @return - the status of the element
   */
  public Try<Boolean> check(WebElement element) {
    log.debug("Checking element status: {}", this.name());
    return Try.of(() -> resolve(element).apply());
  }

}
