package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Cast;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActorAbilityTest {


  @Test
  public void actorHasName() {
    Actor actor = Actor.named("TheActor");

    assertThat("check actor has name", actor.getName(), equalTo("TheActor"));
  }

  @Test
  public void actorHasNoAbilities() {
    Actor actor = Actor.named("actor");

    assertThat("check actor has no ability", actor.can(FirstAbility.class), equalTo(false));
  }

  @Test
  public void actorHasOneAbility() {
    Actor actor = Actor.named("actor");

    FirstAbility ability = mock(FirstAbility.class);

    actor.whoCan(ability);

    assertThat("check actor has ability", actor.can(FirstAbility.class), equalTo(true));
  }

  @Test
  public void actorHasMultipleAbilities() {
    Actor actor = Actor.named("actor");

    FirstAbility firstAbility = new FirstAbility();
    SecondAbility secondAbility = new SecondAbility();

    actor.whoCan(firstAbility);
    actor.whoCan(secondAbility);

    assertThat("check actor has first ability", actor.can(FirstAbility.class), equalTo(true));
    assertThat("check actor has second ability", actor.can(SecondAbility.class), equalTo(true));
  }

  @Test
  public void actorRetrievesAbility() throws DoesNotHaveTheAbility {
    Actor actor = Actor.named("actor");

    FirstAbility firstAbility = new FirstAbility();
    SecondAbility secondAbility = new SecondAbility();

    actor.whoCan(firstAbility);
    actor.whoCan(secondAbility);

    Ability retrievedFirstAbility = actor.withAbilityTo(FirstAbility.class);
    Ability retrievedSecondAbility = actor.withAbilityTo(SecondAbility.class);

    assertThat("check actor has first ability", retrievedFirstAbility, equalTo(firstAbility));
    assertThat("check actor has second ability", retrievedSecondAbility, equalTo(secondAbility));
  }

  @Test void actorHasMultipleAbilitiesAddedInOneCall() {
    Actor actor = Actor.named("actor");

    FirstAbility firstAbility = new FirstAbility();
    SecondAbility secondAbility = new SecondAbility();

    actor.whoCan(firstAbility, secondAbility);

    assertThat("check actor has first ability", actor.can(FirstAbility.class), equalTo(true));
    assertThat("check actor has second ability", actor.can(SecondAbility.class), equalTo(true));
  }


  @Test
  public void destroyAbilitiesOfCast() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("actor");

    FirstAbility testDestroyAbilities = mock(FirstAbility.class);

    actor.whoCan(testDestroyAbilities);

    cast.cleanStage();

    verify(testDestroyAbilities).destroy();
  }

  @Test
  public void destroyAbilitiesOfCastWithMultipleAbilities() {

    Actor actor = Actor.named("actor");

    FirstAbility testDestroyAbilities = mock(FirstAbility.class);
    SecondAbility testDestroySecondAbilities = mock(SecondAbility.class);

    actor.whoCan(testDestroyAbilities);
    actor.whoCan(testDestroySecondAbilities);

    actor.cleansStage();

    verify(testDestroyAbilities).destroy();
    verify(testDestroySecondAbilities).destroy();
  }



  static class FirstAbility implements Ability {

    @Override
    public void destroy() {

    }
  }

  static class SecondAbility implements Ability {

    @Override
    public void destroy() {

    }
  }
}
