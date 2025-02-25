package com.teststeps.thekla4j.core.base.abilities;


import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;

/**
 * An ability is a capability that an actor can have. It is used to perform tasks.
 */
public interface Ability {
//    public static Ability as(UsesAbilities actor) throws DoesNotHaveTheAbility;

  /**
   * This method is called when the ability is no longer needed. It is used to clean up resources.
   */
  void destroy();

  /**
   * In case the test fails, an ability log dump can be created to help with test case analysis.
   *
   * @return - the ability log dump
   */
  List<NodeAttachment> abilityLogDump();
}
