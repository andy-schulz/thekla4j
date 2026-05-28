package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.checkSetParameterName;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.convertShortParamSyntax;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.executeGeneratorFunction;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.matchAndRetrieveParameter;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.matchAssignment;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.parseAndExecuteInlineGeneratorFunction;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.PredefinedInlineGeneratorFunctions.TIMESTAMP_IN_MS;
import static com.teststeps.thekla4j.utils.terminal.FormattedOutput.CYAN;
import static com.teststeps.thekla4j.utils.terminal.FormattedOutput.GREEN;

import com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorCallParser.GeneratorCall;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;

/**
 * A store for data generators
 */
@Log4j2
public class GeneratorStore {

  /**
   * Pre-compiled pattern for valid generator function names
   */
  private static final Pattern FUNCTION_NAME_PATTERN = Pattern.compile("[A-Za-z0-9]+");


  private Map<String, String> storedParameters = HashMap.empty();

  private Map<String, DataGenerator> dataGeneratorMap = HashMap.empty();
  private Map<String, InlineGenerator> inlineGeneratorMap = HashMap.empty();
  private Map<String, String> nameList = HashMap.empty();

  /**
   * Add a generator to the store
   *
   * @param name      the name of the generator
   * @param generator the generator
   * @return the store
   * @deprecated Use {@link Generator} annotation on DataGenerator fields and register them via
   *             {@link #registerGenerators(Object...)} instead.
   */
  @Deprecated(since = "2.2.0", forRemoval = true)
  public GeneratorStore addGenerator(String name, DataGenerator generator) {
    return addGenerator(name, "no description given", generator);
  }

  /**
   * Add a generator to the store
   *
   * @param generatorName the name of the generator
   * @param description   the description of the generator
   * @param generator     the generator
   * @return the store
   * @deprecated Use {@link Generator} annotation on DataGenerator fields and register them via
   *             {@link #registerGenerators(Object...)} instead.
   */
  @Deprecated(since = "2.2.0", forRemoval = true)
  public GeneratorStore addGenerator(String generatorName, String description, DataGenerator generator) {
    return addGeneratorInternal(generatorName, description, generator);
  }

  /**
   * Internal method to register a generator in the store.
   * Used by both the deprecated {@link #addGenerator} methods and the {@link GeneratorScanner}.
   *
   * @param generatorName the name of the generator
   * @param description   the description of the generator
   * @param generator     the generator
   * @return the store
   */
  GeneratorStore addGeneratorInternal(String generatorName, String description, DataGenerator generator) {

    if (!FUNCTION_NAME_PATTERN.matcher(generatorName).matches())
      throw new IllegalArgumentException(
                                         "Generator name '" + generatorName + "' is invalid. Only alphanumeric characters are allowed " +
                                             FUNCTION_NAME_PATTERN.pattern());

    if (nameList.keySet().contains(generatorName))
      throw new IllegalArgumentException("Generator with name '" + generatorName + "' already exists");


    this.dataGeneratorMap = dataGeneratorMap.put(generatorName, generator);

    this.nameList = nameList.put(generatorName, description);
    return this;
  }

  /**
   * Register generators from provider objects. Scans the given objects for fields annotated with
   * {@link Generator} and registers them in this store.
   *
   * <p>Usage:</p>
   * <pre>{@code
   * GeneratorStore store = GeneratorStore.create()
   *     .registerGenerators(new MyGeneratorProvider());
   * }</pre>
   *
   * @param providers the objects containing annotated generator fields
   * @return the store
   */
  public GeneratorStore registerGenerators(Object... providers) {
    GeneratorScanner.scan(this, providers);
    return this;
  }

  /**
   * Internal method to register an inline generator in the store.
   * Used by both the deprecated {@link #addInlineGenerator} method and the {@link GeneratorScanner}.
   *
   * @param generatorName the name of the inline generator
   * @param generator     the inline generator
   * @return the store
   */
  GeneratorStore addInlineGeneratorInternal(String generatorName, InlineGenerator generator) {
    this.inlineGeneratorMap = inlineGeneratorMap.put(generatorName, generator);
    return this;
  }

  /**
   * Add an inline generator to the store
   *
   * @param generatorName the name of the generator
   * @param generator     the generator
   * @return the store
   * @deprecated Use {@link InlineGen} annotation on InlineGenerator fields and register them via
   *             {@link #registerGenerators(Object...)} instead.
   */
  @Deprecated(since = "2.2.0", forRemoval = true)
  public GeneratorStore addInlineGenerator(String generatorName, InlineGenerator generator) {
    return addInlineGeneratorInternal(generatorName, generator);
  }

  /**
   * find and execute a generator found in the generatorInput
   *
   * @param generatorInput the name of the generator
   * @return the description
   */
  public Try<String> parseAndExecute(String generatorInput) {

    Option<GeneratorCall> call = GeneratorCallParser.parse(generatorInput);

    if (call.isDefined()) {
      GeneratorCall gc = call.get();
      Option<DataGenerator> generator = dataGeneratorMap.get(gc.name());

      if (generator.isDefined()) {
        return executeGeneratorFunction.apply(generator.get(), storedParameters, gc.parameterString())
            .map(Option::of)
            .map(assignResultToNamedParameter.apply(generatorInput));
      }

      log.warn("No registered generator found for '{}'. Available generators: {}",
        gc.name(), nameList.keySet().mkString(", "));
      return Try.success(generatorInput);
    }

    /*
      * 1. replace inline generators
      * 2. check if variable assignment is present and if yes assign the string to the variable
      * 3. convert $PARAM → ${PARAM} for unified resolution
      * 4. resolve ${PARAM} references
     */
    return parseAndExecuteInlineGeneratorFunction.apply(generatorInput, inlineGeneratorMap)
        .map(replacedString -> assignResultToNamedParameter.apply(replacedString, Option.none()))
        .map(convertShortParamSyntax)
        .flatMap(matchAndRetrieveParameter.apply(storedParameters));
  }

  /**
   * Resolves a parameter map by renaming the "default" key and resolving all values
   * through {@link #parseAndExecute}. This eliminates the need for consumer projects
   * to manually call {@code replaceShortVariable} and {@code generateData} on each value.
   *
   * <p>Usage in a generator implementation:</p>
   * <pre>{@code
   * @Generator(name = "formatDate")
   * public DataGenerator formatDate() {
   * return functionParams -> {
   * Map<String, String> params = world.getGeneratorStore()
   * .resolveParameterMap(functionParams, "date")
   * .getOrElseThrow(x -> new IllegalArgumentException(x.getMessage()));
   * // params now contains resolved values
   * return Try.of(() -> ...);
   * };
   * }
   * }</pre>
   *
   * @param params         the parameter map from the generator call
   * @param defaultKeyName the name to assign to the "default" parameter (if present)
   * @return a Try containing the resolved parameter map
   */
  public Try<Map<String, String>> resolveParameterMap(Map<String, String> params, String defaultKeyName) {
    Map<String, String> prepared = params.containsKey("default") ? params.remove("default").put(defaultKeyName, params.get("default").get()) : params;
    return prepared
        .mapValues(this::parseAndExecute)
        .transform(LiftTry.fromMap());
  }

  /**
   * Set the parameter map
   *
   * @param parameters the parameter map
   */
  private void setParameterMap(Map<String, String> parameters) {
    this.storedParameters = parameters;
  }

  /**
   * Assign the result of a generator to a parameter
   *
   * @param result the result
   * @param name   parameter name
   */
  private void assignResult(String result, String name) {
    setParameterMap(storedParameters.put(name, result));
  }

  /**
   * Assign the result of a generator to a named parameter if present
   */
  protected final Function2<String, Option<String>, String> assignResultToNamedParameter =
      (generatorInput, res) -> matchAssignment.apply(generatorInput)
          .map(checkSetParameterName)
          .peek(a -> assignResult(res.getOrElse(a.value()), a.name()))
          .map(a -> res.getOrElse(a.value()))
          .getOrElse(res.getOrElse(generatorInput));

  /**
   * Create a new GeneratorStore with predefined generators
   *
   * @return the generator store
   */
  public static GeneratorStore create() {
    return (new GeneratorStore())

        .addGeneratorInternal("randomString", PredefinedGeneratorFunctions.randomStringDescription, PredefinedGeneratorFunctions.randomString)

        .addInlineGeneratorInternal("TIMESTAMP_IN_MS", TIMESTAMP_IN_MS);
  }

  @Override
  public String toString() {
    return nameList.toList()
        .collect(
          Collectors.mapping(t -> CYAN("GeneratorFunction: " + t._1) + "\n" + GREEN(t._2),
            Collectors.joining("\n" + CYAN("--------------------") + "\n")));
  }
}
