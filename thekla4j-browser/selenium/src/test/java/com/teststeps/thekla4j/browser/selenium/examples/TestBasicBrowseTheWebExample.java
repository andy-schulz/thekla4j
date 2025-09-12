package com.teststeps.thekla4j.browser.selenium.examples;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.config.BrowserStartupConfig;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Enter;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Title;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.util.function.Function;


public class TestBasicBrowseTheWebExample {

//  @Test
  public void browseTheWeb() throws ActivityError {

    BrowserStartupConfig startUp = BrowserStartupConfig.startMaximized();

    Actor actor = Actor.named("TestUser")
        .whoCan(BrowseTheWeb.with(Selenium.browser().startUpConfig(startUp).build()));

    Element googleSearchField = Element.found(By.xpath("//input[@name='q']"))
        .called("Google Search Field");

    Element googleSearchButton = Element.found(By.xpath("//input[@name='btnK']"))
        .called("Google Search Button");

    actor.attemptsTo(

      Navigate.to("https://www.google.com"),

      Enter.text("thekla4j").into(googleSearchField),

      Click.on(googleSearchButton),

      See.ifThe(Title.ofPage())
          .is(Expected.to.pass(title -> title.contains("thekla4j"))))

        .getOrElseThrow(Function.identity());
  }
}
