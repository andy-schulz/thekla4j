package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.activities.mouseActions.MouseActionDriver;
import io.vavr.control.Try;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Playwright implementation of {@link MouseActionDriver}.
 * Translates mouse actions to Playwright's {@link Mouse} API.
 */
class PlaywrightMouseActionDriver implements MouseActionDriver {

  private final Page page;
  private final PlaywrightLoader loader;
  private final List<Runnable> actions = new ArrayList<>();

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> clickAndHold() {
    actions.add(() -> page.mouse().down());
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> clickAndHold(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> {
        page.mouse().move(center[0], center[1]);
        page.mouse().down();
      });
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> release() {
    actions.add(() -> page.mouse().up());
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> release(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> {
        page.mouse().move(center[0], center[1]);
        page.mouse().up();
      });
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> click() {
    actions.add(() -> {
      // Click at current mouse position by pressing and releasing
      page.mouse().down();
      page.mouse().up();
    });
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> click(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> page.mouse().click(center[0], center[1]));
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> doubleClick() {
    actions.add(() -> {
      page.mouse().down();
      page.mouse().up();
      page.mouse().down();
      page.mouse().up();
    });
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> doubleClick(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> page.mouse().dblclick(center[0], center[1]));
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> contextClick() {
    actions.add(() -> {
      page.mouse().down(new Mouse.DownOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
      page.mouse().up(new Mouse.UpOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
    });
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> contextClick(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> page.mouse()
          .click(center[0], center[1],
            new Mouse.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT)));
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> moveToElement(Element element) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> page.mouse().move(center[0], center[1]));
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> moveToElement(Element element, int xOffset, int yOffset) {
    return resolveCenter(element).map(center -> {
      actions.add(() -> page.mouse().move(center[0] + xOffset, center[1] + yOffset));
      return (MouseActionDriver) this;
    });
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> moveByOffset(int xOffset, int yOffset) {
    // Playwright Mouse.move uses absolute coordinates; we need to track current position
    actions.add(() -> {
      // Use JavaScript to get the current mouse position and move relative
      // Playwright does not expose current mouse position, so we move in steps
      Object result = page.evaluate("() => ({ x: window._pwMouseX || 0, y: window._pwMouseY || 0 })");
      @SuppressWarnings("unchecked") java.util.Map<String, Object> pos = (java.util.Map<String, Object>) result;
      double currentX = ((Number) pos.get("x")).doubleValue();
      double currentY = ((Number) pos.get("y")).doubleValue();
      double newX = currentX + xOffset;
      double newY = currentY + yOffset;
      page.mouse().move(newX, newY);
      page.evaluate("(pos) => { window._pwMouseX = pos.x; window._pwMouseY = pos.y; }",
        java.util.Map.of("x", newX, "y", newY));
    });
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<MouseActionDriver> pause(Duration duration) {
    actions.add(() -> {
      try {
        Thread.sleep(duration.toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<Void> perform() {
    return Try.run(() -> actions.forEach(Runnable::run))
        .peek(__ -> actions.clear())
        .map(__ -> null);
  }

  /**
   * Resolves the center coordinates of an element.
   */
  private Try<double[]> resolveCenter(Element element) {
    return PlaywrightLocatorResolver.resolveElement(page, element)
        .flatMap(loc -> Try.of(loc::boundingBox))
        .map(box -> new double[]{box.x + box.width / 2.0, box.y + box.height / 2.0})
        .peek(center -> {
          // Track mouse position for relative moves
          page.evaluate("(pos) => { window._pwMouseX = pos.x; window._pwMouseY = pos.y; }",
            java.util.Map.of("x", center[0], "y", center[1]));
        });
  }

  PlaywrightMouseActionDriver(Page page, PlaywrightLoader loader) {
    this.page = page;
    this.loader = loader;
  }
}
