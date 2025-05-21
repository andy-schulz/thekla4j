package com.teststeps.thekla4j.core;

import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Cast;
import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;

public class TestCast {

  @Test
  public void createCastObjectAndCallActor() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("Tester");

    assertThat("actor is not null", actor != null);
    assertThat("actor name is Tester", actor.getName().equals("Tester"));
  }

  @Test
  public void createCastObjectAndCallActorWithAlias() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("TheTester");

    Actor actor2 = cast.callActorToStageNamed("he");

    assertThat("actor is not null", actor2 != null);
    assertThat("actor name is TheTester", actor2.getName().equals("TheTester"));

    assertThat("actors are the same", actor.equals(actor2));
  }

  @Test
  public void createCastObjectAndCallMultipleActorsWithAlias() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("TheFirstTester");
    Actor actor2 = cast.callActorToStageNamed("TheSecondTester");
    Actor actor3 = cast.callActorToStageNamed("TheThirdTester");
    Actor actor4 = cast.callActorToStageNamed("TheFirstTester");

    Actor actorHe = cast.callActorToStageNamed("he");

    assertThat("actor is not null", actorHe != null);
    assertThat("actor name is TheFirstTester", actorHe.getName().equals("TheFirstTester"));

    assertThat("actors are the same", actor4.equals(actorHe));
  }

  @Test
  public void callCurrentActorOfCast() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("TheFirstTester");
    Actor actor2 = cast.callActorToStageNamed("TheSecondTester");
    Actor actor3 = cast.callActorToStageNamed("TheThirdTester");
    Actor actor4 = cast.callActorToStageNamed("TheFirstTester");

    Actor currentActor = cast.currentActor();

    assertThat("actor is not null", currentActor != null);
    assertThat("actor name is TheFirstTester", currentActor.getName().equals("TheFirstTester"));

    assertThat("actors are the same", actor4.equals(currentActor));
  }

  @Test
  public void callCurrentActorOfEmptyCast() {
    Cast cast = Cast.setScene();

    Actor currentActor = cast.currentActor();

    assertThat("actor is not null", currentActor != null);
    assertThat("actor name is Janitor", currentActor.getName().equals("Janitor"));

  }

  @Test
  public void createCastObjectAndCallMultipleActorsWithAliasAsCrew() {
    Cast cast = Cast.setScene();

    Actor actor = cast.callActorToStageNamed("TheFirstTester");
    Actor actor2 = cast.callActorToStageNamed("TheSecondTester");
    Actor actor3 = cast.callActorToStageNamed("TheThirdTester");
    Actor actor4 = cast.callActorToStageNamed("TheFirstTester");

    HashMap<String, Actor> crew = cast.crew();

    assertThat("crew is not empty", !crew.isEmpty());
    assertThat("crew has 3 actors", crew.size() == 3);
  }

}
