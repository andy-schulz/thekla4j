package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import com.teststeps.thekla4j.core.base.abilities.Ability;
import com.teststeps.thekla4j.core.base.errors.DoesNotHaveTheAbility;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Cast;
import com.teststeps.thekla4j.core.data.SomeAbility;
import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestActorAbility {


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

  @Test
  void actorHasMultipleAbilitiesAddedInOneCall() {
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

  @Test
  @DisplayName("withAbilityTo method should throw DoesNotHaveTheAbility exception")
  public void checkingForAbilityShouldThrowException() {

    Actor actor = Actor.named("actor");

    Throwable thrown = assertThrows(
      DoesNotHaveTheAbility.class,
      () -> actor.withAbilityTo(SomeAbility.class));

    assertThat(thrown.getMessage(), startsWith("Actor actor does not have the ability SomeAbility"));
  }


  static class FirstAbility implements Ability {

    @Override
    public void destroy() {

    }

    @Override
    public List<NodeAttachment> abilityLogDump() {
      return List.empty();
    }
  }

  static class SecondAbility implements Ability {

    @Override
    public void destroy() {

    }

    @Override
    public List<NodeAttachment> abilityLogDump() {
      return List.empty();
    }
  }
}
