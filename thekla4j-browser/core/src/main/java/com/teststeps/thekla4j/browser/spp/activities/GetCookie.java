package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("get cookie '@cookie' from browser")
public class GetCookie extends Task<Void, Cookie> {

  @Called(name = "cookie")
  private String cookieName;

  @Override
  protected Either<ActivityError, Cookie> performAs(Actor actor, Void result) {
    return BrowseTheWeb.as(actor)
      .flatMap(b -> b.getCookie(cookieName))
      .transform(ActivityError.toEither(String.format("Error while getting cookie '%s' from browser", cookieName)));
  }

  public static GetCookie fromBrowser(String cookieName) {
    return new GetCookie(cookieName);
  }
}
