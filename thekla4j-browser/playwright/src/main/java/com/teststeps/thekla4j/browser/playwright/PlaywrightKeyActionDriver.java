package com.teststeps.thekla4j.browser.playwright;

import com.microsoft.playwright.Keyboard;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActionDriver;
import io.vavr.control.Try;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Playwright implementation of {@link KeyActionDriver}.
 * Translates key actions to Playwright's {@link Keyboard} API.
 */
class PlaywrightKeyActionDriver implements KeyActionDriver {

  private final Keyboard keyboard;
  private final List<Runnable> actions = new ArrayList<>();

  /**
   * Maps a thekla4j {@link Key} to a Playwright key string.
   */
  private static String toPlaywrightKey(Key key) {
    return switch (key) {
      case TAB -> "Tab";
      case ENTER -> "Enter";
      case ESCAPE -> "Escape";
      case SPACE -> " ";
      case ARROW_DOWN -> "ArrowDown";
      case ARROW_UP -> "ArrowUp";
      case ARROW_LEFT -> "ArrowLeft";
      case ARROW_RIGHT -> "ArrowRight";
      case BACK_SPACE -> "Backspace";
      case DELETE -> "Delete";
      case CONTROL -> "Control";
      case SHIFT -> "Shift";
      case ALT -> "Alt";
      case COMMAND -> "Meta";
    };
  }

  /** {@inheritDoc} */
  @Override
  public Try<KeyActionDriver> keyDown(Key key) {
    actions.add(() -> keyboard.down(toPlaywrightKey(key)));
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<KeyActionDriver> keyUp(Key key) {
    actions.add(() -> keyboard.up(toPlaywrightKey(key)));
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<KeyActionDriver> keyPress(Key key) {
    actions.add(() -> keyboard.press(toPlaywrightKey(key)));
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<KeyActionDriver> keyPress(CharSequence sequence) {
    actions.add(() -> keyboard.type(sequence.toString()));
    return Try.success(this);
  }

  /** {@inheritDoc} */
  @Override
  public Try<KeyActionDriver> pause(Duration duration) {
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

  PlaywrightKeyActionDriver(Keyboard keyboard) {
    this.keyboard = keyboard;
  }
}
