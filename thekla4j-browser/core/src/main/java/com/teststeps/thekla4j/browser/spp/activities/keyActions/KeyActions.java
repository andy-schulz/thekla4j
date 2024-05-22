package com.teststeps.thekla4j.browser.spp.activities.keyActions;

import io.vavr.control.Try;

public interface KeyActions {

    /**
     * schedule the down key action
     * @param key - the key to press
     * @return - the key actions object
     */
    KeyActions keyDown(Key key);

    /**
     * schedule the up key action
     * @param key - the key to release
     * @return - the key actions object
     */
    KeyActions keyUp(Key key);

    /**
     * schedule the press key action
     * @param key - the key to press
     * @return - the key actions object
     */
    KeyActions keyPress(Key key);

    /**
     * execute the key action
     * @return
     */
    Try<Void> perform();
}
