package com.teststeps.thekla4j.browser.selenium.waiter;

import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.status.ElementStatusType;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;


@Log4j2(topic = "SeleniumElementStatus")
public enum SeleniumElementStatus {
  IS_ENABLED {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element::isEnabled;
    }
  },
  IS_VISIBLE {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element::isDisplayed;
    }
  },

  IS_CLICKABLE {
    @Override
    public CheckElementStatus resolve(WebElement element) {
      return element.isDisplayed() ? element::isEnabled : () -> false;
    }
  },

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

  public static SeleniumElementStatus forElement(Element element) {
    return SeleniumElementStatus.forWaiter(element.waiter().type());
  }

  public abstract CheckElementStatus resolve(WebElement element);

  public Try<Boolean> check(WebElement element) {
    log.debug("Checking element status: {}", this.name());
    return Try.of(() -> resolve(element).apply());
  }

}
