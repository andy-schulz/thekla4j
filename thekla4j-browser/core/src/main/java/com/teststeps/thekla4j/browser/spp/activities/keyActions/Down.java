package com.teststeps.thekla4j.browser.spp.activities.keyActions;

class Down implements KeyAction {

  private final Key key;

  public Down(Key key) {
    this.key = key;
  }

  public void performKeyAction(KeyActions action) {
    action.keyDown(this.key);
  }
}
