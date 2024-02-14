package com.teststeps.thekla4j.browser.core;

import com.teststeps.thekla4j.browser.spp.activities.State;
import io.vavr.control.Try;

public interface Browser {

  public Try<Void> navigateTo(String url);

  public Try<Void> clickOn(Element element);

  public Try<Void> enterTextInto(String text, Element element);

  public Try<String> textOf(Element element);

  public Try<String> valueOf(Element element);

  public Try<String> attributeValueOf(String attribute, Element element);

  public Try<State> getState(Element element);

  public Try<String> title();

  public Try<String> url();

  public Try<Void> quit();
}
