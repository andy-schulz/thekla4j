package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class ElementTest {

  @Test
  public void testElement() throws ActivityError {

    Actor actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element clientButton = Element.found(By.id("clientButton1"));
    Element header = Element.found(By.css(".headerElement"));

    String url = "http://localhost:3000";

    actor.attemptsTo(

            Navigate.to(url),

            Click.on(clientButton),

            See.ifThe(Text.of(header))
                .is(Expected.to.equal("Clicked on: id = clientButton1")))

        .getOrElseThrow(Function.identity());

  }
}
