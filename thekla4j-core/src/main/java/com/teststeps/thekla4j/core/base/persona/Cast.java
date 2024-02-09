package com.teststeps.thekla4j.core.base.persona;


import io.vavr.collection.HashMap;
import io.vavr.collection.List;

public class Cast {

    private HashMap<String, Actor> crew = HashMap.empty();
    private Actor currentActor;
    private final List<String> currentActorAlias = List.of("he", "she", "it", "");

    public static Cast setScene() {
        return new Cast();
    }

    public Actor callActorToStageNamed(String actorName) {

        if (currentActorAlias.contains(actorName))
            return this.currentActor();

        if (crew.containsKey(actorName))
            this.currentActor = crew.get(actorName).get();
        else {
            crew = crew.put(actorName, Actor.named(actorName));
            this.currentActor = crew.get(actorName).get();
        }

        return this.currentActor;
    }

    public HashMap<String, Actor> crew() {
        return this.crew;
    }

    public Actor currentActor() {
        if (this.currentActor == null) {
            return this.callActorToStageNamed("Janitor");
        }

        return this.currentActor;
    }

    public void cleanStage() {
        this.crew.mapValues(Actor::cleansStage);
    }

    public Cast() {

    }
}
