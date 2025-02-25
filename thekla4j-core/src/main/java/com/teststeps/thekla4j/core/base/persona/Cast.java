package com.teststeps.thekla4j.core.base.persona;


import io.vavr.collection.HashMap;
import io.vavr.collection.List;

/**
 * The cast of actors in a test
 */
public class Cast {

    private HashMap<String, Actor> crew = HashMap.empty();
    private Actor currentActor;
    private final List<String> currentActorAlias = List.of("he", "she", "it", "");

    /**
     * Set the scene for the test
     *
     * @return - the cast of actors
     */
    public static Cast setScene() {
        return new Cast();
    }

    /**
     * Call an actor to the stage (return the actor if already on stage)
     *
     * @param actorName - the name of the actor
     * @return - the actor
     */
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

    /**
     * Get the crew of actors
     *
     * @return - the crew of actors
     */
    public HashMap<String, Actor> crew() {
        return this.crew;
    }

    /**
     * Get the current actor
     *
     * @return - the current actor
     */
    public Actor currentActor() {
        if (this.currentActor == null) {
            return this.callActorToStageNamed("Janitor");
        }

        return this.currentActor;
    }

    /**
     * release all resources used by the actors
     */
    public void cleanStage() {
        this.crew.mapValues(Actor::cleansStage);
    }

    /**
     * Create a new Cast
     */
    private Cast() {

    }
}
