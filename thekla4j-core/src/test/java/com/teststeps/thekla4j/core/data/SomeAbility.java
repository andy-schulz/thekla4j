package com.teststeps.thekla4j.core.data;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import io.vavr.collection.List;

public class SomeAbility implements Ability {

  @Override
  public void destroy() {

  }

  @Override
  public List<NodeAttachment> abilityLogDump() {
    return List.empty();
  }
}
