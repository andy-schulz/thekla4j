package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Resize the browser window
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2(topic = "Resize")
@Action("resize browser window")
public class ResizeWindow extends BasicInteraction {

  private enum ResizeMode {
    CUSTOM,
    MAXIMIZE,
    MINIMIZE,
    FULLSCREEN
  }

  @Called(name = "width")
  private final Option<Integer> width;

  @Called(name = "height")
  private final Option<Integer> height;

  private final ResizeMode mode;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .peek(browser -> {
          switch (mode) {
            case CUSTOM -> log.info("Resizing browser window to {}x{}", width.get(), height.get());
            case MAXIMIZE -> log.info("Maximizing browser window");
            case MINIMIZE -> log.info("Minimizing browser window");
            case FULLSCREEN -> log.info("Setting browser window to fullscreen");
          }
        })
        .flatMap(browser -> switch (mode) {
          case CUSTOM -> browser.resizeWindow(width.get(), height.get());
          case MAXIMIZE -> browser.maximizeWindow();
          case MINIMIZE -> browser.minimizeWindow();
          case FULLSCREEN -> browser.fullscreenWindow();
        })
        .transform(ActivityError.toEither("Error while resizing browser window"));
  }

  /**
   * Resize the browser window to a custom width and height
   *
   * @param width  the width to resize to
   * @param height the height to resize to
   * @return a new Resize activity
   */
  public static ResizeWindow to(int width, int height) {
    return new ResizeWindow(Option.of(width), Option.of(height), ResizeMode.CUSTOM);
  }

  /**
   * Maximize the browser window
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toMaximum() {
    return new ResizeWindow(Option.none(), Option.none(), ResizeMode.MAXIMIZE);
  }

  /**
   * Minimize the browser window
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toMinimum() {
    return new ResizeWindow(Option.none(), Option.none(), ResizeMode.MINIMIZE);
  }

  /**
   * Set the browser window to fullscreen
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toFullscreen() {
    return new ResizeWindow(Option.none(), Option.none(), ResizeMode.FULLSCREEN);
  }

  /**
   * Resize to common desktop viewport (1920x1080)
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toStandardDesktopSize() {
    return new ResizeWindow(Option.of(1920), Option.of(1080), ResizeMode.CUSTOM);
  }


  /**
   * Resize to common tablet landscape viewport (1024x768)
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toTabletLandscape() {
    return new ResizeWindow(Option.of(1024), Option.of(768), ResizeMode.CUSTOM);
  }

  /**
   * Resize to common tablet portrait viewport (768x1024)
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toTabletPortrait() {
    return new ResizeWindow(Option.of(768), Option.of(1024), ResizeMode.CUSTOM);
  }

  /**
   * Resize to common mobile landscape viewport (812x375)
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toMobileLandscape() {
    return new ResizeWindow(Option.of(812), Option.of(375), ResizeMode.CUSTOM);
  }

  /**
   * Resize to common mobile portrait viewport (375x812)
   *
   * @return a new Resize activity
   */
  public static ResizeWindow toMobilePortrait() {
    return new ResizeWindow(Option.of(375), Option.of(812), ResizeMode.CUSTOM);
  }
}
