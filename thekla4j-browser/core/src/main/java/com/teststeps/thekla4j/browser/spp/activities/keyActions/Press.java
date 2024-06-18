package com.teststeps.thekla4j.browser.spp.activities.keyActions;

class Press implements KeyAction {
    private final Key key;

    public Press(Key key) {
        this.key = key;
    }

    @Override
    public void performKeyAction(KeyActions action) {
        action.keyPress(this.key);
    }
}
