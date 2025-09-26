package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Scroll an element to the top or left of a scrollable area
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "ScrollElement")
@Action("Scroll Element @{element} to @{direction} of Area @{scrollArea}")
public class Scroll extends BasicInteraction {

  @Called(name = "element", value = "name")
  private Element element;
  @Called(name = "scrollArea", value = "name")
  private Element scrollArea;
  @Called(name = "direction")
  private ScrollDirection scrollDirection;

  /**
   * perform the scroll action
   * 
   * @param actor - the actor performing the action
   * @return - Either an ActivityError or null if successful
   */
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
          default -> Try.failure(new Throwable("Unsupported scroll direction: %s to scroll an element"
              .formatted(scrollDirection)));
        })

        .transform(ActivityError.toEither("Error while scrolling element '%s' to %s of area."
            .formatted(element.name(), scrollDirection.name())))
        .map(__ -> null);
  }

  /**
   * create a new Scroll activity for an element
   * 
   * @param element - the element to scroll
   * @return - a new Scroll activity
   */
  public static Scroll element(Element element) {
    return new Scroll(element, null, null);
  }

  /**
   * create a new Scroll activity to scroll to the end of a scrollable area
   * 
   * @param scrollArea - the scrollable area to scroll to the end of
   * @return - a new Scroll activity
   */
  public static BasicInteraction toEndOfArea(Element scrollArea) {
    return new ScrollArea(scrollArea);
  }

  /**
   * Scroll the given area up or down by a number of pixels (default 100)
   * 
   * @param scrollArea - the scrollable area to scroll
   * @return the ScrollByPixels interaction
   */
  public static BasicInteraction areaDown(Element scrollArea) {
    return ScrollByPixels.of(scrollArea, ScrollDirection.DOWN);
  }

  /**
   * Scroll the given area up or down by a number of pixels (default 100)
   * 
   * @param scrollArea - the scrollable area to scroll
   * @param byPixels   - the number of pixels to scroll by
   * @return the ScrollByPixels interaction
   */
  public static BasicInteraction areaDown(Element scrollArea, int byPixels) {
    return ScrollByPixels.of(scrollArea, byPixels, ScrollDirection.DOWN);
  }

  /**
   * Scroll the given area up or down by a number of pixels (default 100)
   * 
   * @param scrollArea - the scrollable area to scroll
   * @return the ScrollByPixels interaction
   */
  public static BasicInteraction areaUp(Element scrollArea) {
    return ScrollByPixels.of(scrollArea, ScrollDirection.UP);
  }

  /**
   * Scroll the given area up or down by a number of pixels (default 100)
   * 
   * @param scrollArea - the scrollable area to scroll
   * @param byPixels   - the number of pixels to scroll by
   * @return the ScrollByPixels interaction
   */
  public static BasicInteraction areaUp(Element scrollArea, int byPixels) {
    return ScrollByPixels.of(scrollArea, byPixels, ScrollDirection.UP);
  }

  /**
   * set the scroll area to scroll to top
   *
   * @param scrollArea - the scrollable area to scroll
   * @return - the Scroll interaction
   */
  public Scroll toTopOfArea(Element scrollArea) {
    this.scrollArea = scrollArea;
    this.scrollDirection = ScrollDirection.TOP;
    return this;
  }

  /**
   * set the scroll area to scroll to left
   *
   * @param scrollArea - the scrollable area to scroll
   * @return - the Scroll interaction
   */
  public Scroll toLeftOfArea(Element scrollArea) {
    this.scrollArea = scrollArea;
    this.scrollDirection = ScrollDirection.LEFT;
    return this;
  }


  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Action("Scroll @{scrollArea} by @{pixels} pixels in direction @{scrollDirection}")
  private static class ScrollByPixels extends BasicInteraction {

    @Called(name = "scrollArea", value = "name")
    private Element scrollArea;
    @Called(name = "pixels")
    private int pixels;
    @Called(name = "scrollDirection")
    private ScrollDirection scrollDirection;

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .onSuccess(__ -> log.info(() -> "Scrolling %s by %d pixels in area '%s'".formatted(
            scrollDirection, pixels, scrollArea.name())))

          .flatMap(browser -> switch (scrollDirection) {
            case UP -> browser.scrollAreaUpByPixels(scrollArea, pixels);
            case DOWN -> browser.scrollAreaDownByPixels(scrollArea, pixels);
            default -> Try.failure(new Throwable("Unsupported scroll direction: %s to scroll an area"
                .formatted(scrollDirection)));
          })

          .transform(ActivityError.toEither("Error while scrolling '%s' by %d pixels in area '%s'."
              .formatted(scrollDirection, pixels, scrollArea.name())))
          .map(__ -> null);
    }

    private static ScrollByPixels of(Element scrollArea, ScrollDirection scrollDirection) {
      return new ScrollByPixels(scrollArea, 100, scrollDirection);
    }

    private static ScrollByPixels of(Element scrollArea, int pixels, ScrollDirection scrollDirection) {
      return new ScrollByPixels(scrollArea, pixels, scrollDirection);
    }

    public ScrollByPixels byPixels(int pixels) {
      this.pixels = pixels;
      return this;
    }
  }


  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Action("Scroll to end of scrollable area @{scrollArea}")
  private static class ScrollArea extends BasicInteraction {

    @Called(name = "scrollArea", value = "name")
    private Element scrollArea;

    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {
      return BrowseTheWeb.as(actor)
          .onSuccess(__ -> log.info(() -> "Scrolling to end of area '%s'".formatted(scrollArea.name())))
          .flatMap(browser -> browser.scrollToEndOfScrollableArea(scrollArea))
          .transform(ActivityError.toEither("Error while scrolling to end of area %s."
              .formatted(scrollArea.name())))
          .map(__ -> null);
    }
  }


  private enum ScrollDirection {
    UP, DOWN, TOP, LEFT, RIGHT
  }
}
