package com.teststeps.thekla4j.browser.core;

public interface Browser {

  public void navigateTo(String url);

  public void clickOn(Element element);

  public void EnterTextInto(String text, Element element);

  public String getTextFrom(Element element);

  public String valueOf(Element element);
}
