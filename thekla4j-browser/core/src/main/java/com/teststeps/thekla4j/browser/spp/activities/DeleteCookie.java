package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Interaction to delete a cookie from the browser
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("delete cookie '@cookie' from browser")
public class DeleteCookie extends BasicInteraction {

  @Called(name = "cookie")
  private String cookieName;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.deleteCookie(cookieName))
        .transform(ActivityError.toEither(String.format("Error while deleting cookie '%s' from browser", cookieName)));
  }

  /**
   * Factory method to create a new DeleteCookie activity
   *
   * @param cookieName the name of the cookie to delete
   * @return a new DeleteCookie instance
   */
  public static DeleteCookie named(String cookieName) {
    return new DeleteCookie(cookieName);
  }
}
