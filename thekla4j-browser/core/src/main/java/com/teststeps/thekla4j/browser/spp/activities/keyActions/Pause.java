package com.teststeps.thekla4j.browser.spp.activities.keyActions;

import java.time.Duration;

/**
 * Pause action for key sequences.
 * <p>
 * This class represents a pause action that can be used in a sequence of key actions.
 * It implements the {@link KeyAction} interface and allows for pausing the execution
 * of key actions for a specified duration.
 */
public class Pause implements KeyAction {

  private final Duration duration;

  /**
   * create a new Pause action
   *
   * @param duration - the key to press down
   */
  public Pause(Duration duration) {
    this.duration = duration;
  }

  /**
   * perform the key down action
   *
   * @param action - the action to perform
   */
  @Override
  public void performKeyAction(KeyActionDriver action) {
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
        "duration=" + duration.toMillis() +
        '}';
  }
}
