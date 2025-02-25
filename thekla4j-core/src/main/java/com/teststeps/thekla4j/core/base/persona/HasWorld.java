package com.teststeps.thekla4j.core.base.persona;

/**
 * An actor that has a world
 */
public interface HasWorld {

  /**
   * Get the world of the actor
   * @return - the world of the actor
   */
  ActorsWorld getWorld();

  /**
   * Set the world of the actor
   * @param world - the world of the actor
   * @return - the actor with the new world
   */
  Actor withWorld(ActorsWorld world);
}
