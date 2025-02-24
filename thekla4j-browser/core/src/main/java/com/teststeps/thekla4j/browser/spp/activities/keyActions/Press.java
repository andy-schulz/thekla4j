package com.teststeps.thekla4j.browser.spp.activities.keyActions;

/**
 * A key action to press a key
 */
class Press implements KeyAction {
  private final Key key;

  Press(Key key) {
    this.key = key;
  }

  /**
   * perform the key press action
   *
   * @param action - the action to perform
   */
  @Override
  public void performKeyAction(KeyActions action) {
    action.keyPress(this.key);
  }
}
