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
  public void performKeyAction(KeyActionDriver action) {
    action.keyUp(this.key);
  }

  /**
   * get the string representation of the key up action
   *
   * @return - the string representation of the key up action
   */
  @Override
  public String toString() {
    return "Up{" +
        "key=" + key.name() +
        '}';
  }

  Up(Key key) {
    this.key = key;
  }
}
