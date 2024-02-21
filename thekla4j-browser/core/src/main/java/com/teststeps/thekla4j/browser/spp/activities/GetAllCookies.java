package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;

@Action("get all cookies from browser")
public class GetAllCookies extends Task<Void, List<Cookie>> {
  @Override
  protected Either<ActivityError, List<Cookie>> performAs(Actor actor, Void result) {
    return BrowseTheWeb.as(actor)
        .flatMap(Browser::getAllCookies)
        .transform(ActivityError.toEither("Error while getting all cookies from browser"));
  }


  public static GetAllCookies fromBrowser() {
    return new GetAllCookies();
  }
}
