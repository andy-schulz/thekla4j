package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.teststeps.thekla4j.utils.terminal.FormattedOutput.CYAN;
import static com.teststeps.thekla4j.utils.terminal.FormattedOutput.GREEN;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.checkSetParameterName;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.matchAndRetrieveParameter;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.matchAssignment;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.parseAndExecuteGeneratorFunction;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.GeneratorStoreFunctions.parseAndExecuteInlineGeneratorFunction;
import static com.teststeps.thekla4j.cucumber.dynamic_test_data.PredefinedInlineGeneratorFunctions.TIMESTAMP_IN_MS;

@Log4j2
public class GeneratorStore {

  protected static final Function1<String, String> REGEX_SPECIFIC_GENERATOR_PATTERN =
    prefix -> "(" + prefix + "\\{([A-Za-z0-9\\-\\+\\_\\.\\;\\=\\$\\:\\,\\s]*)\\}).*";
  protected static final String REGEX_GENERAL_GENERATOR_PATTERN = "([A-Za-z0-9]+\\{([A-Za-z0-9\\-\\+\\_\\.\\;\\=\\$\\:\\,\\s]*)\\}).*";
  protected static final String REGEX_FUNCTION_NAME = "[A-Za-z0-9]+";


  private Map<String, String> storedParameters = HashMap.empty();

  private Map<Pattern, DataGenerator> dataGeneratorMap = HashMap.empty();
  private Map<String, InlineGenerator> inlineGeneratorMap = HashMap.empty();
  private Map<String, String> nameList = HashMap.empty();

  /**
   * Add a generator to the store
   *
   * @param name      the name of the generator
   * @param generator the generator
   * @return the store
   */
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
   */
  public GeneratorStore addGenerator(String generatorName, String description, DataGenerator generator) {

    if (!Pattern.compile(REGEX_FUNCTION_NAME).matcher(generatorName).matches())
      throw new IllegalArgumentException(
        "Generator name '" + generatorName + "' is invalid. Only alphanumeric characters are allowed " + REGEX_FUNCTION_NAME);

    if (nameList.keySet().contains(generatorName))
      throw new IllegalArgumentException("Generator with name '" + generatorName + "' already exists");


    this.dataGeneratorMap = dataGeneratorMap.put(
      Pattern.compile(REGEX_SPECIFIC_GENERATOR_PATTERN.apply(generatorName)),
      generator);

    this.nameList = nameList.put(generatorName, description);
    return this;
  }

  public GeneratorStore addInlineGenerator(String generatorName, InlineGenerator generator) {

    this.inlineGeneratorMap = inlineGeneratorMap.put(generatorName, generator);
    return this;
  }

  /**
   * find and execute a generator found in the generatorInput
   *
   * @param generatorInput the name of the generator
   * @return the description
   */
  public Try<String> parseAndExecute(String generatorInput) {

    if (Pattern.compile(REGEX_GENERAL_GENERATOR_PATTERN).matcher(generatorInput).matches()) {
      return parseAndExecuteGeneratorFunction.apply(dataGeneratorMap,generatorInput)
        .map(Option::of)
        .map(assignResultToNamedParameter.apply(generatorInput));
    }

    /*
      * 1. replace inline generators
      * 2. check if variable assignment is present and if yes assign the string to the variable
      * 3. assign result to variable
     */
    return
      parseAndExecuteInlineGeneratorFunction.apply(generatorInput, inlineGeneratorMap)
        .map(replacedString -> assignResultToNamedParameter.apply(replacedString, Option.none()))
        .flatMap(matchAndRetrieveParameter.apply(storedParameters));
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

  protected final Function2<String, Option<String>, String> assignResultToNamedParameter =
    (generatorInput, res) ->
      matchAssignment.apply(generatorInput)
        .map(checkSetParameterName)
        .peek(a -> assignResult(res.getOrElse(a.value()), a.name()))
        .map(a -> res.getOrElse(a.value()))
        .getOrElse(res.getOrElse(generatorInput));

  /**
   * Create a new GeneratorStore with predefined generators
   */
  public static GeneratorStore create() {
    return (new GeneratorStore())

      .addGenerator("randomString", PredefinedGeneratorFunctions.randomStringDescription, PredefinedGeneratorFunctions.randomString)

      .addInlineGenerator("TIMESTAMP_IN_MS", TIMESTAMP_IN_MS);
  }

  @Override
  public String toString() {
    return
      nameList.toList().collect(
        Collectors.mapping(t -> CYAN("GeneratorFunction: " + t._1) + "\n" + GREEN(t._2),
          Collectors.joining("\n" + CYAN("--------------------") + "\n")));
  }
}
