package com.teststeps.thekla4j.core.base.errors;

import com.teststeps.thekla4j.core.base.activities.Activity;
import com.teststeps.thekla4j.core.base.persona.Actor;

public class DoesNotHave {

    public static class DoesNotHaveTheAbilityHelper {
        private String abilityName = "";

        public DoesNotHaveTheAbility usedBy(Actor actor) {
            String message =
                    "Actor " + actor.getName() + " does not have the ability " + this.abilityName + "\n" +
                            "try assigning an abiliy with:\n" +
                            actor.getName() + ".can(" + this.abilityName + ".<<abilityConfigMethod()>>)";

            return new DoesNotHaveTheAbility(message);
        }

        public DoesNotHaveTheAbilityHelper(String abilityName) {
            this.abilityName = abilityName;
        }
    }

    public static class DoesNotHaveLogAnnotationHelper {
        private String annotationName = "";

        public DoesNotHaveLogAnnotation in(Activity activity) {
            String message =
                    "Activity " + activity.getClass().getSimpleName() + " does not use the  " + this.annotationName + " annotation\n";

            return new DoesNotHaveLogAnnotation(message);
        }

        public DoesNotHaveLogAnnotationHelper(String annotationName) {
            this.annotationName = annotationName;
        }
    }

    public static DoesNotHaveTheAbilityHelper theAbility(String abilityName) {
        return new DoesNotHaveTheAbilityHelper(abilityName);
    }

    public static DoesNotHaveLogAnnotationHelper logAnnotation(String annotationName) {
        return new DoesNotHaveLogAnnotationHelper(annotationName);
    }

}
