package com.teststeps.thekla4j.browser.core.status;

public class ElementStatusFunctions {

  public static UntilElement defaultWaiter() {
    return UntilElement.of(ElementStatusType.IS_NOT_STALE);
  }
}
