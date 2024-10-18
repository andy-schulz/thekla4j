package com.teststeps.thekla4j.core.base.abilities;


import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;

public interface Ability {
//    public static Ability as(UsesAbilities actor) throws DoesNotHaveTheAbility;

  public void destroy();

  List<NodeAttachment> abilityLogDump();
}
