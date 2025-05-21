package com.teststeps.thekla4j.core.base.errors;

import com.teststeps.thekla4j.core.base.persona.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;

/**
 * A helper class to create error messages for missing abilities and annotations
 */
public class DoesNotHave {

  /**
   * An error message for when an actor does not have an ability
   */
  public static class DoesNotHaveTheAbilityHelper {
    private String abilityName = "";

    /**
     * Create an error message for when an actor does not have an ability
     * 
     * @param actor - the actor
     * @return - the error object
     */
    public DoesNotHaveTheAbility usedBy(Actor actor) {
      String message =
          "Actor " + actor.getName() + " does not have the ability " + this.abilityName + "\n" +
              "try assigning an abiliy with:\n" +
              actor.getName() + ".whoCan(" + this.abilityName + ".<<abilityConfigMethod()>>)";

      return new DoesNotHaveTheAbility(message);
    }

    /**
     * Create a new DoesNotHaveTheAbilityHelper
     * 
     * @param abilityName - the name of the ability
     */
    public DoesNotHaveTheAbilityHelper(String abilityName) {
      this.abilityName = abilityName;
    }
  }

  /**
   * An error message for when an activity does not have a log annotation
   */
  public static class DoesNotHaveLogAnnotationHelper {
    private String annotationName = "";

    /**
     * Create an error message for when an activity does not have a log annotation
     * 
     * @param activity - the activity
     * @param <I>      - the input type of the activity
     * @param <O>      - the output type of the activity
     * @return - the error object
     */
    public <I, O> DoesNotHaveLogAnnotation in(Activity<I, O> activity) {
      String message =
          "Activity " + activity.getClass().getSimpleName() + " does not use the  " + this.annotationName + " annotation\n";

      return new DoesNotHaveLogAnnotation(message);
    }

    /**
     * Create a new DoesNotHaveLogAnnotationHelper
     * 
     * @param annotationName - the name of the annotation
     */
    public DoesNotHaveLogAnnotationHelper(String annotationName) {
      this.annotationName = annotationName;
    }
  }

  /**
   * An error message for when an actor does not have an ability
   * 
   * @param abilityName - the name of the ability
   * @return - the error object
   */
  public static DoesNotHaveTheAbilityHelper theAbility(String abilityName) {
    return new DoesNotHaveTheAbilityHelper(abilityName);
  }

  /**
   * An error message for when an activity does not have a log annotation
   * 
   * @param annotationName - the name of the annotation
   * @return - the error object
   */
  public static DoesNotHaveLogAnnotationHelper logAnnotation(String annotationName) {
    return new DoesNotHaveLogAnnotationHelper(annotationName);
  }

}
