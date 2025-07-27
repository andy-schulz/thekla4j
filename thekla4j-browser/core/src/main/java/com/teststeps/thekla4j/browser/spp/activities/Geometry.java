package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.vavr.TransformTry;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Get the geometry of an element (x, y, height, width)
 */
@AllArgsConstructor
@Log4j2(topic = "ElementGeometry")
@Action("get the geometry (x, y, height, width) of element @{element}")
public class Geometry extends SupplierTask<Rectangle> {

  @Called(name = "element", value = "name")
  private final Element element;

  @Override
  protected Either<ActivityError, Rectangle> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info(() -> "Getting geometry of element '%s'".formatted(element.name())))
        .flatMap(b -> b.getGeometryOfElement(element))
        .transform(TransformTry.toEither(x -> ActivityError.of(x.getMessage())));
  }

  /**
   * create a new Geometry activity
   *
   * @param element - the element to get the geometry of
   * @return - a new Geometry activity
   */
  public static Geometry of(Element element) {
    return new Geometry(element);
  }
}
