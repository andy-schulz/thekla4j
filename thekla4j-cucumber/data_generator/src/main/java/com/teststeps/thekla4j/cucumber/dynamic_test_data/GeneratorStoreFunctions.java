package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functions for working with generator store
 */
@Log4j2
public class GeneratorStoreFunctions {

  private static final String SET_REGEX_PARAMETER_NAME = "[A-Za-z0-9_]+";
  private static final String GET_REGEX_PARAMETER = "\\$\\{(.*?)\\}";
  private static final String GET_REGEX_PARAMETER_NAME = "[A-Za-z0-9_.]+";
//  private static final String REGEX_ASSIGNMENT = "(.*?s)([A-Za-z0-9]+\\{.*\\}) => \\$\\{(.*)\\}";
  private static final String REGEX_ASSIGNMENT = "(.*) => \\$\\{(.*)\\}";

  /**
   * Regex pattern to match inline replacement e.g. ?{GENERATOR_NAME}
   */
  protected static final String REGEX_INLINE_REPLACEMENT_PATTERN = "(\\?\\{([A-Z0-9_]*)\\})";



  /**
   * match the groups in the input string group 1: whole generator function group 2: parameter string passed to generator function
   */
  private static final Function2<String, Pattern, List<String>> matchGroups =
    (input, pattern) -> {
      Matcher matcher = pattern.matcher(input);
      if (matcher.matches())
        return List.of(matcher.group(1), matcher.group(2));

      return List.empty();
    };

  /**
   * Parse and execute a generator function from the generator map
   */
  protected static final Function2<Map<Pattern, DataGenerator>, String, Try<String>> parseAndExecuteGeneratorFunction =
    (generatorMap, generatorInput) -> {

      Map<List<String>, DataGenerator> filteredGenerator = generatorMap
        .mapKeys(matchGroups.apply(generatorInput))
        .filterKeys(groupList -> groupList.size() == 2);

      if (filteredGenerator.size() > 1)
        return io.vavr.control.Try.failure(new IllegalArgumentException("Multiple generators found for input: " + generatorInput));


      if (filteredGenerator.isEmpty()) {
        log.debug("No generator found for input: {}", generatorInput);
        return io.vavr.control.Try.success(generatorInput);
      }

      String generatorParameterString = filteredGenerator.head()._1().get(1);
      DataGenerator generator = filteredGenerator.values().head();

      Map<String, String> genParameters = ParameterParsingFunctions.parseParameterStringToMap.apply(generatorParameterString);

      return generator.run(genParameters)
        .onSuccess(x -> log.debug("Generator Function executed: {}", x));
    };


  /**
   *  Match assignment pattern and return Assignment object
   */
  protected static final Function<String, Option<Assignment>> matchAssignment =
    generatorInput -> Option.of(Pattern.compile(REGEX_ASSIGNMENT).matcher(generatorInput))
      .flatMap(matcher -> {
        if (matcher.matches()) {
          return Option.of(Assignment.of(matcher.group(2), matcher.group(1)));
        } else {
          log.debug("No assignment match found");
          return Option.none();
        }
      });

  /**
   * Check if parameter name is valid for setting
   */
  protected static final Function<Assignment, Assignment> checkSetParameterName = assignment -> {
    if (!Pattern.compile(SET_REGEX_PARAMETER_NAME).matcher(assignment.name()).matches()) {
      throw new IllegalArgumentException("Cant assign value to parameter named: " + assignment.name() + ". Parameter names must match: " +
        SET_REGEX_PARAMETER_NAME);
    }
    return assignment;
  };


  /**
   * Assignment record for parameter name and value
   */
  protected record Assignment(String name, String value) {
    public static Assignment of(String name, String value) {
      return new Assignment(name, value);
    }
  }

  private static final Predicate<String> checkGetParameterName = parameterName -> Pattern.compile(GET_REGEX_PARAMETER_NAME)
    .matcher(parameterName)
    .matches();

  private static final Function2<Map<String, String>, String, Try<String>> returnStoredParameters =
    (storedParameter, key) -> storedParameter.get(key)
      .map(Try::success)
      .getOrElseThrow(() -> new IllegalArgumentException("Parameter not found: " + key));


  /**
   * Get the value of a json attribute iterate through the attributes and get the value of the last attribute
   */
  private static final Function2<List<String>, JSONObject, String> getJsonAttributes = (attributes, json) -> {
    Object elem = attributes.foldLeft((Object) json, (jsonObj, k) -> {

      if (jsonObj instanceof JSONObject j) {
        if (j.has(k))
          return j.get(k);
        log.error("Cant get value of attribute: {}. Attribute not found in JSONObject: {}", k, jsonObj);
        throw new IllegalArgumentException("Cant get value of attribute: " + k + ". Attribute not found in JSONObject: " + jsonObj);
      }

      log.error("Cant get value of attribute '{}' from object '{}'. Is not a JSONObject", k, jsonObj);
      throw new IllegalArgumentException("Cant get value of attribute '" + k + "' from object '" + jsonObj + "'. Is not a JSONObject");

    });

    return elem.toString();
  };

  /**
   * Test if a parameter is valid json
   */
  private static final Function<String, Try<String>> testParamIsValidJson = json -> {
    try {
      new JSONObject(json);
      return Try.success(json);
    } catch (Exception e) {
      return Try.failure(new IllegalArgumentException("Cant get value of parameter. Parameter is not a valid json: " + json));
    }
  };

  /**
   * get the value of the stored parameter and try to get the value of the attribute
   * <pre>
   * e.g.
   * - TEST -> retrieve the value of the parameter TEST
   * - TEST.name -> retrieve the value of the parameter TEST and then get attribute name of the json object
   * - TEST.name.value -> retrieve the value of the parameter TEST and then get attribute name of the json object and
   * then get attribute value of the json object
   * </pre>
   */
  private static final Function2<Map<String, String>, String, Try<String>> parseStoredParameter = (storedParameters, NameAndProperties) -> {

    List<String> paramPropertyList = List.of(NameAndProperties.split("\\."));

    if (paramPropertyList.size() == 1) {
      return returnStoredParameters.apply(storedParameters, NameAndProperties);
    }


    String key = paramPropertyList.head();
    List<String> attributes = paramPropertyList.tail();

    return returnStoredParameters.apply(storedParameters, key)
      .flatMap(testParamIsValidJson)
      .map(JSONObject::new)
      .map(getJsonAttributes.apply(attributes))
      .onFailure(e -> log.error("Error while parsing stored parameter: {}", NameAndProperties, e));

  };

  /**
   * Match and retrieve a parameter
   */
  protected static final Function2<Map<String, String>, String, Try<String>> matchAndRetrieveSingleParameter = (storedParameters, singleParameter) -> {

    Matcher matcher = Pattern.compile(GET_REGEX_PARAMETER).matcher(singleParameter);

    if (matcher.matches()) {
      if (checkGetParameterName.test(matcher.group(1))) {
        return parseStoredParameter.apply(storedParameters, matcher.group(1));
      } else {
        return Try.failure(new IllegalArgumentException("Cant get value of parameter named: " + matcher.group(1) + ". Parameter names must match: " +
          GET_REGEX_PARAMETER_NAME));
      }
    } else {
      return Try.success(singleParameter);
    }
  };

  /**
   * Match and retrieve a parameter
   */
  protected static final Function2<Map<String, String>, String, Try<String>> matchAndRetrieveParameter = (storedParameters, input) -> {

    Matcher matcher = Pattern.compile(GET_REGEX_PARAMETER).matcher(input);

    List<Tuple2<Integer, Integer>> matches = List.empty();
    while (matcher.find()) {
      matches = matches.append(Tuple.of(matcher.start(), matcher.end()));
    }


    return matches.map(boundary -> boundary.append(input.substring(boundary._1(), boundary._2())))
      .map(t -> t.map3(matchAndRetrieveSingleParameter.apply(storedParameters)))
      .map(LiftTry.fromTuple3$3())
      .transform(LiftTry.fromList())
      .map(l -> l.foldRight(new StringBuilder(input), (t, acc) -> acc.replace(t._1, t._2, t._3())))
      .map(StringBuilder::toString);
  };


  /**
   * Find and run single inline generator
   */
  protected static final Function2<Map<String, InlineGenerator>, String, Try<String>> replaceSingleInlineGenerator =
    (inlineGeneratorMap, generatorName) -> inlineGeneratorMap.get(generatorName)
    .toTry(() -> new IllegalArgumentException("No inline generator found for name: " + generatorName))
    .flatMap(InlineGenerator::run);



  private static final Function<String, String> extractInlineGeneratorName = input -> {
    Matcher matcher = Pattern.compile(REGEX_INLINE_REPLACEMENT_PATTERN).matcher(input);
    return matcher.matches() ? matcher.group(2) : "";
  };

  /**
   * Parse and execute inline generator
   */
  public static final Function2<String, Map<String, InlineGenerator>, Try<String>> parseAndExecuteInlineGeneratorFunction =
    (generatorInput, inlineGeneratorMap) -> {


      Matcher matcher = Pattern.compile(REGEX_INLINE_REPLACEMENT_PATTERN).matcher(generatorInput);

      List<Tuple2<Integer, Integer>> matches = List.empty();
      while (matcher.find()) {
        matches = matches.append(Tuple.of(matcher.start(), matcher.end()));
      }


      return matches.map(boundary -> boundary.append(generatorInput.substring(boundary._1(), boundary._2())))
        .map(t -> t.map3(extractInlineGeneratorName))
        .map(t -> t.map3(replaceSingleInlineGenerator.apply(inlineGeneratorMap)))
        .map(LiftTry.fromTuple3$3())
        .transform(LiftTry.fromList())
        .map(l -> l.foldRight(new StringBuilder(generatorInput), (t, acc) -> acc.replace(t._1, t._2, t._3())))
        .map(StringBuilder::toString)
        .onSuccess(x -> log.debug("Inline generator replaced: {}", x));
    };


  private GeneratorStoreFunctions() {
    // utility class
  }

}
