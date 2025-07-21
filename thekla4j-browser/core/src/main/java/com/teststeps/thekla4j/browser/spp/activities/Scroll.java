package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2(topic = "ScrollElement")
@Action("Scroll Element @{element} to @{direction} of Area @{scrollArea}")
public class Scroll extends BasicInteraction {

  @Called(name = "element", value = "name")
  private Element element;
  @Called(name = "scrollArea", value = "name")
  private Element scrollArea;
  @Called(name = "direction")
  private ScrollDirection scrollDirection;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    if (scrollArea == null) {
      return Either.left(ActivityError.of("Scroll area is not set. Use toTopOfArea or toLeftOfArea methods."));
    }

    return BrowseTheWeb.as(actor)
        .peek(b -> log.info(() -> "Scrolling element '%s' to %s of area '%s'".formatted(
          element.name(), scrollDirection.name(), scrollArea.name())))

        .flatMap(browser -> switch (scrollDirection) {
          case TOP -> browser.scrollElementToTopOfArea(element, scrollArea);
          case LEFT -> browser.scrollElementToLeftOfArea(element, scrollArea);
        })

        .transform(ActivityError.toEither("Error while scrolling element '%s' to %s of area."
            .formatted(element.name(), scrollDirection.name())))
        .map(__ -> null);
  }

  public static Scroll element(Element element) {
    return new Scroll(element, null, null);
  }

  public static BasicInteraction toEndOfArea(Element scrollArea) {
    return new ScrollArea(scrollArea);
  }

  public Scroll toTopOfArea(Element scrollArea) {
    this.scrollArea = scrollArea;
    this.scrollDirection = ScrollDirection.TOP;
    return this;
  }

  public Scroll toLeftOfArea(Element scrollArea) {
    this.scrollArea = scrollArea;
    this.scrollDirection = ScrollDirection.LEFT;
    return this;
  }


  @AllArgsConstructor
  @Action("Scroll to end of scrollable area @{scrollArea}")
  private static class ScrollArea extends BasicInteraction {

    @Called(name = "scrollArea", value = "name")
    private Element scrollArea;

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .peek(b -> log.info(() -> "Scrolling to end of area '%s'".formatted(scrollArea.name())))
          .flatMap(browser -> browser.scrollToEndOfScrollableArea(scrollArea))
          .transform(ActivityError.toEither("Error while scrolling to end of area %s."
              .formatted(scrollArea.name())))
          .map(__ -> null);
    }
  }


  private enum ScrollDirection {
    TOP, LEFT
  }
}
