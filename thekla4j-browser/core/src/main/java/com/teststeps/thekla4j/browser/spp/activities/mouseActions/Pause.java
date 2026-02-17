package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import java.time.Duration;

/**
 * Pause action for mouse sequences.
 * <p>
 * This class represents a pause action that can be used in a sequence of mouse actions.
 * It implements the {@link MouseAction} interface and allows for pausing the execution
 * of mouse actions for a specified duration.
 */
public class Pause implements MouseAction {

  private final Duration duration;

  /**
   * create a new Pause action
   *
   * @param duration - the duration to pause
   */
  public Pause(Duration duration) {
    this.duration = duration;
  }

  /**
   * perform the pause action
   *
   * @param action - the action to perform
   */
  @Override
  public void performMouseAction(MouseActionDriver action) {
    action.pause(duration);
  }

  /**
   * get the string representation of the pause action
   *
   * @return - get the string representation of the pause action
   */
  @Override
  public String toString() {
    return "Pause{" +
        "duration=" + duration.toMillis() + "ms" +
        '}';
  }
}
