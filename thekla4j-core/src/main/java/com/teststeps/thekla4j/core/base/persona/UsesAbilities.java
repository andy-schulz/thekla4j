package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;

public interface UsesAbilities {
    <T extends Ability>Ability withAbilityTo(Class<T> abilityClass) throws DoesNotHaveTheAbility;

    boolean can(Class<? extends Ability> ability);

    UsesAbilities cleansStage();
}
