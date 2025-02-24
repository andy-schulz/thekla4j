package com.teststeps.thekla4j.browser.spp.activities.keyActions;

/**
 * A key action to press a key up
 */
class Up implements KeyAction {

  private final Key key;

  /**
   * create a new key up action
   *
   * @param action - the key to release
   */
  @Override
  public void performKeyAction(KeyActions action) {
    action.keyUp(this.key);
  }

  Up(Key key) {
    this.key = key;
  }
}
