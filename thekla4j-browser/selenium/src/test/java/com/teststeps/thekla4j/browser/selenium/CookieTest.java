package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.core.Cookie;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.AddCookie;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class CookieTest {

  private Actor actor;

  @Test
  public void testAddCookie() throws ActivityError {

    actor = Actor.named("TestUser")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));


    Cookie c1 = new Cookie("Cookie1", "CookieValue1");
    Cookie c2 = new Cookie("Cookie2", "CookieValue2");
    Cookie c3 = new Cookie("Cookie3", "CookieValue3");

    List<Cookie> cookies = List.of(c1, c2, c3);


    actor.attemptsTo(
            Navigate.to("http://localhost:3000/"),

            AddCookie.list(cookies))

        .getOrElseThrow(Function.identity());
  }
}
