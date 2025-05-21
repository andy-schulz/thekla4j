package com.teststeps.thekla4j.core.base.persona;

import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;

/**
 * An actor that can use abilities
 */
public interface UsesAbilities {

  /**
   * get the ability instance from the actor
   *
   * @param abilityClass the ability to get
   * @param <T>          the type of the ability
   * @return the ability instance
   * @throws DoesNotHaveTheAbility if the actor does not have the ability
   */
  <T extends Ability> Ability withAbilityTo(Class<T> abilityClass) throws DoesNotHaveTheAbility;

  /**
   * check if the actor has the given ability
   *
   * @param abilityClass the ability to check for
   * @return true if the actor has the ability, false otherwise
   */
  boolean can(Class<? extends Ability> abilityClass);

  /**
   * release all resources used by the actor
   * 
   * @return the actor with all resources released
   */
  UsesAbilities cleansStage();
}
