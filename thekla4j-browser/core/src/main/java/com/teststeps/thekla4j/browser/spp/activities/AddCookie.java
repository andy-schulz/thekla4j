package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;



@AllArgsConstructor
@Action("add cookies '@{cookie}' to browser")
public class AddCookie extends BasicInteraction {

  @Called(name = "cookie")
  private List<Cookie> cookies;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .map(b -> cookies.map(b::addCookie))
        .flatMap(LiftTry.fromList())
        .map(List::getOrNull)
        .transform(ActivityError.toEither("Error while adding cookies to browser"));
  }

  public static AddCookie toBrowser(Cookie cookie) {
    return new AddCookie(List.of(cookie));
  }

  public static AddCookie list(List<Cookie> cookieList) {
    return new AddCookie(cookieList);
  }
}
