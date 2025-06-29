package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Action("Check Visibility of Element")
public class Visibility extends SupplierTask<Boolean> {

  private Element element;

  @Override
  protected Either<ActivityError, Boolean> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .flatMap(b -> b.visibilityOf(element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  public static Visibility of(Element element) {
    return new Visibility(element);
  }
}
