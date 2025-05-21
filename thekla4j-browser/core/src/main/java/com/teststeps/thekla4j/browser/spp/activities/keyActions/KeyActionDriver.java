package com.teststeps.thekla4j.browser.spp.activities.keyActions;

import io.vavr.control.Try;
import java.time.Duration;

/**
 * interface for key actions
 */
public interface KeyActionDriver {

  /**
   * schedule the down key action
   *
   * @param key - the key to press
   * @return - the key actions object
   */
  KeyActionDriver keyDown(Key key);

  /**
   * schedule the up key action
   *
   * @param key - the key to release
   * @return - the key actions object
   */
  KeyActionDriver keyUp(Key key);

  /**
   * schedule the press key action
   *
   * @param key - the key to press
   * @return - the key actions object
   */
  KeyActionDriver keyPress(Key key);

  /**
   * schedule the press key sequence action
   *
   * @param sequence - the keys to press
   * @return - the key actions object
   */
  KeyActionDriver keyPress(CharSequence sequence);

  /**
   * schedule the pause action
   *
   * @param duration - the duration to pause
   * @return - the key actions driver
   */
  KeyActionDriver pause(Duration duration);


  /**
   * execute the key action
   *
   * @return - Try{VOID}
   */
  Try<Void> perform();
}
