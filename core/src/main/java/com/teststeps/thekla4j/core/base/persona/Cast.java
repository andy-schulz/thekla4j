package com.teststeps.thekla4j.core.base.persona;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cast {

    private HashMap<String, Actor> crew = new HashMap<String, Actor>();
    private Actor currentActor;
    private List<String> currentActorAlias = Arrays.asList("he", "she", "it", "");

    public static Cast setScene() {
        return new Cast();
    }

    public Actor callActorToStageNamed(String actorName) {

        if (currentActorAlias.contains(actorName))
            return this.currentActor();

        if (crew.containsKey(actorName))
            this.currentActor = crew.get(actorName);
        else {
            crew.put(actorName, Actor.named(actorName));
            this.currentActor = crew.get(actorName);
        }

        return this.currentActor;
    }

    public Actor currentActor() {
        if (this.currentActor == null) {
            return this.callActorToStageNamed("Janitor");
        }

        return this.currentActor;
    }

    public Cast() {

    }
}
