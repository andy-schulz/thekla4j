package com.teststeps.thekla4j.browser.spp.activities.keyActions;

/**
 * A key sequence to press
 */
class PressSequence implements KeyAction {
  private final CharSequence sequence;

  PressSequence(CharSequence sequence) {
    this.sequence = sequence;
  }

  /**
   * perform the key press action of the key sequence
   *
   * @param action - the action to perform
   */
  @Override
  public void performKeyAction(KeyActionDriver action) {
    action.keyPress(this.sequence);
  }

  /**
   * get the string representation of the key press action
   *
   * @return - the string representation of the key press action
   */
  @Override
  public String toString() {
    return "PressSequence{" +
        "sequence='" + sequence + '\'' +
        '}';
  }
}
