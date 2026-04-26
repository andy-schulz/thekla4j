package com.teststeps.thekla4j.allure.junit5.extensions.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an {@link com.teststeps.thekla4j.core.base.persona.Actor Actor} field for automatic
 * activity-log-to-Allure-step mapping.
 *
 * <p>After the test method executes, the extension scans for fields annotated with
 * {@code @AllureActor}, reads their {@code ActivityLogNode} tree, and maps every activity
 * into a native Allure step (with status, timing, parameters, and attachments).
 *
 * <pre>{@code
 * @ExtendWith(Thekla4jAllureJunit5Extension.class)
 * class MyTest {
 *
 * @AllureActor
 *              Actor actor = Actor.named("TestUser");
 *
 * @Test
 *       void myTest() {
 *       actor.attemptsTo(/* ... *​/);
 *       }
 *       }
 *       }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllureActor {
}
