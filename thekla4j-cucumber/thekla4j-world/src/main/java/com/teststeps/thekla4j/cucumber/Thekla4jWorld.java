package com.teststeps.thekla4j.cucumber;

import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Cast;
import com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStore;
import io.vavr.control.Try;

/**
 * Base world object for Cucumber tests using thekla4j.
 *
 * <p>Provides actor management via {@link Cast} and data generation via {@link GeneratorStore}.
 * Project-specific worlds should extend this class and add their own concerns
 * (browser setup, API clients, pool users, etc.).
 *
 * <p>When used together with the Allure cucumber plugin, the {@code Thekla4jAllureHook}
 * automatically maps each actor's activity log to Allure steps and attaches the HTML
 * activity log on test failure.
 *
 * <pre>{@code
 * // Project-specific world
 * public class World extends Thekla4jWorld {
 *     // add browser setup, API clients, etc.
 * }
 *
 * // Step definitions — inject via PicoContainer
 * public class MyStepDefs {
 * private final World world;
 *
 * public MyStepDefs(World world) {
 * this.world = world;
 * }
 *
 * @Given("Alice opens the app")
 * public void aliceOpensApp() {
 * Actor alice = world.callActorToStageNamed("Alice");
 * alice.attemptsTo(...);
 * }
 * }
 * }</pre>
 */
public class Thekla4jWorld {

  private static final ThreadLocal<Thekla4jWorld> CURRENT_WORLD = new ThreadLocal<>();

  private final Cast cast = Cast.setScene();
  private final GeneratorStore generatorStore = GeneratorStore.create();

  /**
   * Creates a new world instance and registers it as the current world on this thread.
   */
  public Thekla4jWorld() {
    CURRENT_WORLD.set(this);
  }

  /**
   * Returns the {@link Thekla4jWorld} instance bound to the current thread, or {@code null} if none is set.
   *
   * @return the current thread-local world instance
   */
  public static Thekla4jWorld getCurrentWorld() {
    return CURRENT_WORLD.get();
  }

  /**
   * Removes the current thread's world reference, allowing it to be garbage-collected.
   */
  public static void clearCurrentWorld() {
    CURRENT_WORLD.remove();
  }

  /**
   * Returns the cast of actors.
   *
   * @return the cast
   */
  public Cast getCast() {
    return cast;
  }

  /**
   * Calls an actor to the stage by name. Creates the actor if not already on stage.
   *
   * @param name the actor name
   * @return the actor
   */
  public Actor callActorToStageNamed(String name) {
    CURRENT_WORLD.set(this);
    return cast.callActorToStageNamed(name);
  }

  /**
   * Returns the generator store for dynamic test data.
   *
   * @return the generator store
   */
  public GeneratorStore getGeneratorStore() {
    CURRENT_WORLD.set(this);
    return generatorStore;
  }

  /**
   * Parses and executes data generators in the given string.
   *
   * @param parsableString the string containing generator expressions
   * @return the result with generators replaced by generated values
   */
  public Try<String> generateData(String parsableString) {
    CURRENT_WORLD.set(this);
    if (parsableString == null) {
      return Try.success(null);
    }
    return generatorStore.parseAndExecute(parsableString);
  }

  /**
   * Registers generator providers. Each provider's fields annotated with
   * {@code @Generator} or {@code @InlineGen} are scanned and registered.
   *
   * @param providers the generator provider objects
   */
  public void registerGenerators(Object... providers) {
    CURRENT_WORLD.set(this);
    generatorStore.registerGenerators(providers);
  }
}
