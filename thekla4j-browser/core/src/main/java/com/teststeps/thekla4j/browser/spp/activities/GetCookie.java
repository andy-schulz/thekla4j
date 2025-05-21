package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Get cookie from browser
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("get cookie '@cookie' from browser")
public class GetCookie extends SupplierTask<Cookie> {

  @Called(name = "cookie")
  private String cookieName;

  @Override
  protected Either<ActivityError, Cookie> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.getCookie(cookieName))
        .transform(ActivityError.toEither(String.format("Error while getting cookie '%s' from browser", cookieName)));
  }

  /**
   * Create a task to get cookie from browser
   *
   * @param cookieName - the name of the cookie to get
   * @return - the task to get cookie from browser
   */
  public static GetCookie named(String cookieName) {
    return new GetCookie(cookieName);
  }
}
