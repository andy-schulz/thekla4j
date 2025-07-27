package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get all cookies from browser
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "GetAllCookies")
@Action("get all cookies from browser")
public class GetAllCookies extends SupplierTask<List<Cookie>> {
  @Override
  protected Either<ActivityError, List<Cookie>> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info("Getting all cookies from browser"))
        .flatMap(Browser::getAllCookies)
        .transform(ActivityError.toEither("Error while getting all cookies from browser"));
  }


  /**
   * Get all cookies from browser
   *
   * @return - the task to get all cookies from browser
   */
  public static GetAllCookies fromBrowser() {
    return new GetAllCookies();
  }
}
