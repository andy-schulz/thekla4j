package com.teststeps.thekla4j.core.base.activities;

import com.teststeps.thekla4j.core.base.persona.UsesAbilities;

public interface Question<PT,RT> {
    RT answeredBy(UsesAbilities actor, PT activityResult);
}
