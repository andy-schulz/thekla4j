package com.teststeps.thekla4j.browser.spp.activities.keyActions;

/**
 * A key action to press a key down
 */
class Down implements KeyAction {

  private final Key key;

  /**
   * create a new key down action
   *
   * @param key - the key to press down
   */
  public Down(Key key) {
    this.key = key;
  }

  /**
   * perform the key down action
   *
   * @param action - the action to perform
   */
  @Override
  public void performKeyAction(KeyActionDriver action) {
    action.keyDown(this.key);
  }

  /**
   * return the string representation of the key down action
   *
   * @return - the string representation of the key down action
   */
  @Override
  public String toString() {
    return "Down{" +
        "key=" + key.name() +
        '}';
  }
}
