package com.teststeps.thekla4j.browser.core.drawing;

/**
 * A move interface in a direction
 */
public interface PointerMove {

  /**
   * get the offset of the move
   *
   * @return - the offset of the move
   */
  Integer offset();

  /**
   * get the direction of the move
   *
   * @return - the direction of the move
   */
  Direction type();
}
