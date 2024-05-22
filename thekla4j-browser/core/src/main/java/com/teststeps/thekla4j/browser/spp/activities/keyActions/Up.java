package com.teststeps.thekla4j.browser.spp.activities.keyActions;

class Up implements KeyAction {

  private final Key key;

  @Override
  public void performKeyAction(KeyActions action) {
    action.keyUp(this.key);
  }

  public Up(Key key) {
    this.key = key;
  }
}
