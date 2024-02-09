package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Cast;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActorAbilityTest {

  @Test
  public void destroyAbilitiesOfCast() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("actor");

    DestroyFistAbilities testDestroyAbilities = mock(DestroyFistAbilities.class);

    actor.whoCan(testDestroyAbilities);

    cast.cleanStage();

    verify(testDestroyAbilities).destroy();
  }

  @Test
  public void destroyAbilitiesOfCastWithMultipleAbilities() {

    Actor actor = Actor.named("actor");

    DestroyFistAbilities testDestroyAbilities = mock(DestroyFistAbilities.class);
    DestroySecondAbilities testDestroySecondAbilities = mock(DestroySecondAbilities.class);

    actor.whoCan(testDestroyAbilities);
    actor.whoCan(testDestroySecondAbilities);

    actor.cleansStage();

    verify(testDestroyAbilities).destroy();
    verify(testDestroySecondAbilities).destroy();
  }



  static class DestroyFistAbilities implements Ability {

    @Override
    public void destroy() {

    }
  }

  static class DestroySecondAbilities implements Ability {

    @Override
    public void destroy() {

    }
  }
}
