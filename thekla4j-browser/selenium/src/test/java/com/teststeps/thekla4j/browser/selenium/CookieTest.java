package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.AddCookie;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class CookieTest {

  private static Actor actor;

  @AfterAll
  public static void tearDown() {
    Option.of(actor)
      .peek(Actor::cleansStage);
  }

  @Test
  public void testAddCookie() throws ActivityError {

    actor = Actor.named("TestUser")
        .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));


    Cookie c1 = Cookie.of("Cookie1", "CookieValue1")
      .withHttpOnly(true)
      .withSameSite("Lax")
      .withSecure(false);
    Cookie c2 = Cookie.of("Cookie2", "CookieValue2");
    Cookie c3 = Cookie.of("Cookie3", "CookieValue3");

    List<Cookie> cookies = List.of(c1, c2, c3);


    actor.attemptsTo(
            Navigate.to("http://localhost:3000/"),

            AddCookie.list(cookies))

        .getOrElseThrow(Function.identity());

    System.out.println("Cookies added");
  }
}
