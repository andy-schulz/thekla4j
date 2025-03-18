package com.teststeps.thekla4j.browser.selenium.examples;

import com.teststeps.thekla4j.commons.error.ActivityError;


public class TestBasicBrowseTheWebExample {

//  @Test
  public void browseTheWeb() throws ActivityError {

//    BrowserStartupConfig startUp = BrowserStartupConfig.startMaximized();
//
//    Actor actor = Actor.named("TestUser")
//      .whoCan(BrowseTheWeb.with(ChromeBrowser.withoutOptions(Option.of(startUp))));
//
//    Element googleSearchField = Element.found(By.xpath("//input[@name='q']"))
//      .called("Google Search Field");
//
//    Element googleSearchButton = Element.found(By.xpath("//input[@name='btnK']"))
//      .called("Google Search Button");
//
//    actor.attemptsTo(
//
//      Navigate.to("https://www.google.com"),
//
//      Enter.text("thekla4j").into(googleSearchField),
//
//      Click.on(googleSearchButton),
//
//      See.ifThe(Title.ofPage())
//        .is(Expected.to.pass(title -> title.contains("thekla4j"))))
//
//      .getOrElseThrow(Function.identity());
  }
}
