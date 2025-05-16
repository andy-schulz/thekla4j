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
  public void performKeyAction(KeyActions action) {
    action.keyPress(this.sequence);
  }
}
