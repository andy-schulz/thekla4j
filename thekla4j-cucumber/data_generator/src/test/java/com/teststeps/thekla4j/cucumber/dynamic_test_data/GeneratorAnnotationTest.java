package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeneratorAnnotationTest {

  // Provider with explicit name
  static class ExplicitNameProvider {
    @Generator(name = "myExplicitGenerator", description = "An explicitly named generator")
    public final DataGenerator gen = parameterMap -> Try.success("explicit result");
  }

  // Provider without name (should use field name)
  static class FieldNameProvider {
    @Generator
    public final DataGenerator myFieldGenerator = parameterMap -> Try.success("field name result");
  }

  // Provider with description
  static class DescribedProvider {
    @Generator(name = "describedGen", description = "A generator with a description")
    public final DataGenerator gen = parameterMap -> Try.success("described result");
  }

  // Provider with multiple generators
  static class MultiProvider {
    @Generator(name = "genOne")
    public final DataGenerator generatorOne = parameterMap -> Try.success("result one");

    @Generator(name = "genTwo")
    public final DataGenerator generatorTwo = parameterMap -> Try.success("result two");
  }

  // Provider with wrong field type
  static class WrongTypeProvider {
    @Generator(name = "badField")
    public final String notAGenerator = "wrong type";
  }

  // Provider with null field
  static class NullFieldProvider {
    @Generator(name = "nullField")
    public final DataGenerator nullGen = null;
  }

  // Provider using field name with default description
  static class DefaultDescriptionProvider {
    @Generator
    public final DataGenerator autoNamedGen = parameterMap -> Try.success("auto named result");
  }

  // Provider with parameterized generator
  static class ParameterizedProvider {
    @Generator(name = "paramGen")
    public final DataGenerator parameterized = parameterMap ->
      Try.success(parameterMap.foldLeft("", (s, entry) -> s + entry._2));
  }

  // ===== Method-based generator providers =====

  // Simple method generator with String parameters
  static class MethodProvider {
    @Generator(name = "methodGen")
    public DataGenerator myGenerator(String one, String two) {
      return parameterMap -> Try.success(one + " " + two);
    }
  }

  // Method generator using field name as generator name
  static class MethodFieldNameProvider {
    @Generator
    public DataGenerator autoNamedMethod(String value) {
      return parameterMap -> Try.success("result: " + value);
    }
  }

  // Method generator with int parameter
  static class MethodIntParamProvider {
    @Generator(name = "intGen")
    public DataGenerator intGenerator(String prefix, int count) {
      return parameterMap -> Try.success(prefix + ":" + count);
    }
  }

  // Method generator with boolean parameter
  static class MethodBooleanParamProvider {
    @Generator(name = "boolGen")
    public DataGenerator boolGenerator(String label, boolean flag) {
      return parameterMap -> Try.success(label + "=" + flag);
    }
  }

  // Method with wrong return type
  static class MethodWrongReturnTypeProvider {
    @Generator(name = "badMethod")
    public String notAGenerator(String one) {
      return "wrong";
    }
  }

  // Method that returns null
  static class MethodNullReturnProvider {
    @Generator(name = "nullMethod")
    public DataGenerator nullReturning(String value) {
      return null;
    }
  }

  // Method with no parameters
  static class MethodNoParamsProvider {
    @Generator(name = "noParamGen")
    public DataGenerator noParams() {
      return parameterMap -> Try.success("no params needed");
    }
  }

  @Test
  public void registerGeneratorWithExplicitName() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new ExplicitNameProvider());

    Try<String> result = store.parseAndExecute("myExplicitGenerator{test}");
    assertThat("generator executed successfully", result.isSuccess());
    assertThat("generator returned expected value", result.get(), equalTo("explicit result"));
  }

  @Test
  public void registerGeneratorUsingFieldName() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new FieldNameProvider());

    Try<String> result = store.parseAndExecute("myFieldGenerator{test}");
    assertThat("generator executed successfully", result.isSuccess());
    assertThat("generator returned expected value", result.get(), equalTo("field name result"));
  }

  @Test
  public void registerMultipleGeneratorsFromSameProvider() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MultiProvider());

    Try<String> resultOne = store.parseAndExecute("genOne{test}");
    assertThat("first generator executed successfully", resultOne.isSuccess());
    assertThat("first generator returned expected value", resultOne.get(), equalTo("result one"));

    Try<String> resultTwo = store.parseAndExecute("genTwo{test}");
    assertThat("second generator executed successfully", resultTwo.isSuccess());
    assertThat("second generator returned expected value", resultTwo.get(), equalTo("result two"));
  }

  @Test
  public void registerGeneratorsFromMultipleProviders() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new ExplicitNameProvider(), new FieldNameProvider());

    Try<String> result1 = store.parseAndExecute("myExplicitGenerator{test}");
    assertThat("first generator executed successfully", result1.isSuccess());
    assertThat("first generator returned expected value", result1.get(), equalTo("explicit result"));

    Try<String> result2 = store.parseAndExecute("myFieldGenerator{test}");
    assertThat("second generator executed successfully", result2.isSuccess());
    assertThat("second generator returned expected value", result2.get(), equalTo("field name result"));
  }

  @Test
  public void registerGeneratorWithWrongFieldTypeThrowsException() {
    Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
      GeneratorStore.create().registerGenerators(new WrongTypeProvider()));

    assertThat("correct error message is thrown",
      thrown.getMessage(),
      equalTo("Field 'notAGenerator' annotated with @Generator must be of type DataGenerator, but is String"));
  }

  @Test
  public void registerGeneratorWithNullFieldThrowsException() {
    Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
      GeneratorStore.create().registerGenerators(new NullFieldProvider()));

    assertThat("correct error message is thrown",
      thrown.getMessage(),
      equalTo("Field 'nullGen' annotated with @Generator is null"));
  }

  @Test
  public void registerDuplicateGeneratorNameThrowsException() {
    Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
      GeneratorStore.create()
        .registerGenerators(new ExplicitNameProvider(), new ExplicitNameProvider()));

    assertThat("correct error message is thrown",
      thrown.getMessage(),
      equalTo("Generator with name 'myExplicitGenerator' already exists"));
  }

  @Test
  public void registerParameterizedGenerator() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new ParameterizedProvider());

    Try<String> result = store.parseAndExecute("paramGen{p1: one, p2: two, p3: three}");
    assertThat("generator executed successfully", result.isSuccess());
    assertThat("generator returned expected value", result.get(), equalTo("onetwothree"));
  }

  @Test
  public void registerGeneratorWithAssignment() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new ExplicitNameProvider());

    String generatorString = "myExplicitGenerator{test} => ${RESULT}";
    Try<String> result = store.parseAndExecute(generatorString);
    assertThat("generator executed successfully", result.isSuccess());
    assertThat("generator returned expected value", result.get(), equalTo("explicit result"));

    Try<String> storedResult = store.parseAndExecute("${RESULT}");
    assertThat("retrieving stored parameter succeeded", storedResult.isSuccess());
    assertThat("retrieved value is correct", storedResult.get(), equalTo("explicit result"));
  }

  @Test
  public void generatorWithDefaultDescription() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new DefaultDescriptionProvider());

    Try<String> result = store.parseAndExecute("autoNamedGen{test}");
    assertThat("generator executed successfully", result.isSuccess());
    assertThat("generator returned expected value", result.get(), equalTo("auto named result"));
  }

  @Test
  public void existingAddGeneratorStillWorks() {
    DataGenerator simpleGenerator = parameterMap -> Try.success("legacy result");

    @SuppressWarnings("deprecation")
    GeneratorStore store = GeneratorStore.create()
      .addGenerator("legacyGen", simpleGenerator);

    Try<String> result = store.parseAndExecute("legacyGen{test}");
    assertThat("legacy generator executed successfully", result.isSuccess());
    assertThat("legacy generator returned expected value", result.get(), equalTo("legacy result"));
  }

  @Test
  public void mixAnnotationAndLegacyRegistration() {
    DataGenerator legacyGen = parameterMap -> Try.success("legacy result");

    @SuppressWarnings("deprecation")
    GeneratorStore store = GeneratorStore.create()
      .addGenerator("legacyGen", legacyGen)
      .registerGenerators(new ExplicitNameProvider());

    Try<String> legacyResult = store.parseAndExecute("legacyGen{test}");
    assertThat("legacy generator executed successfully", legacyResult.isSuccess());
    assertThat("legacy generator returned expected value", legacyResult.get(), equalTo("legacy result"));

    Try<String> annotatedResult = store.parseAndExecute("myExplicitGenerator{test}");
    assertThat("annotated generator executed successfully", annotatedResult.isSuccess());
    assertThat("annotated generator returned expected value", annotatedResult.get(), equalTo("explicit result"));
  }

  // ===== Method-based generator tests =====

  @Test
  public void registerMethodGeneratorWithStringParameters() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodProvider());

    Try<String> result = store.parseAndExecute("methodGen{one: hello, two: world}");
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("hello world"));
  }

  @Test
  public void registerMethodGeneratorUsingMethodName() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodFieldNameProvider());

    Try<String> result = store.parseAndExecute("autoNamedMethod{value: test123}");
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("result: test123"));
  }

  @Test
  public void registerMethodGeneratorWithIntParameter() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodIntParamProvider());

    Try<String> result = store.parseAndExecute("intGen{prefix: item, count: 42}");
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("item:42"));
  }

  @Test
  public void registerMethodGeneratorWithBooleanParameter() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodBooleanParamProvider());

    Try<String> result = store.parseAndExecute("boolGen{label: active, flag: true}");
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("active=true"));
  }

  @Test
  public void registerMethodGeneratorWithMissingParameter() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodProvider());

    Try<String> result = store.parseAndExecute("methodGen{one: hello}");
    assertThat("method generator should fail with missing parameter", result.isFailure());
    assertThat("error message mentions missing parameter",
      result.getCause().getMessage().contains("Missing parameter 'two'"), equalTo(true));
  }

  @Test
  public void registerMethodGeneratorWithWrongReturnTypeThrowsException() {
    Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
      GeneratorStore.create().registerGenerators(new MethodWrongReturnTypeProvider()));

    assertThat("correct error message is thrown",
      thrown.getMessage(),
      equalTo("Method 'notAGenerator' annotated with @Generator must return DataGenerator, but returns String"));
  }

  @Test
  public void registerMethodGeneratorReturningNull() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodNullReturnProvider());

    Try<String> result = store.parseAndExecute("nullMethod{value: test}");
    assertThat("method generator should fail when returning null", result.isFailure());
    assertThat("error message mentions null",
      result.getCause().getMessage().contains("returned null"), equalTo(true));
  }

  @Test
  public void registerMethodGeneratorWithNoParameters() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodNoParamsProvider());

    Try<String> result = store.parseAndExecute("noParamGen{anything}");
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("no params needed"));
  }

  @Test
  public void registerMethodGeneratorWithAssignment() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodProvider());

    String generatorString = "methodGen{one: foo, two: bar} => ${METHOD_RESULT}";
    Try<String> result = store.parseAndExecute(generatorString);
    assertThat("method generator executed successfully", result.isSuccess());
    assertThat("method generator returned expected value", result.get(), equalTo("foo bar"));

    Try<String> storedResult = store.parseAndExecute("${METHOD_RESULT}");
    assertThat("retrieving stored parameter succeeded", storedResult.isSuccess());
    assertThat("retrieved value is correct", storedResult.get(), equalTo("foo bar"));
  }

  @Test
  public void mixFieldAndMethodGenerators() {
    // Provider with both a field and a method generator
    class MixedProvider {
      @Generator(name = "fieldGen")
      public final DataGenerator fieldBased = parameterMap -> Try.success("from field");

      @Generator(name = "methodBasedGen")
      public DataGenerator methodBased(String input) {
        return parameterMap -> Try.success("from method: " + input);
      }
    }

    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MixedProvider());

    Try<String> fieldResult = store.parseAndExecute("fieldGen{test}");
    assertThat("field generator executed successfully", fieldResult.isSuccess());
    assertThat("field generator returned expected value", fieldResult.get(), equalTo("from field"));

    Try<String> methodResult = store.parseAndExecute("methodBasedGen{input: data}");
    assertThat("method generator executed successfully", methodResult.isSuccess());
    assertThat("method generator returned expected value", methodResult.get(), equalTo("from method: data"));
  }

  @Test
  public void registerMethodGeneratorWithInvalidIntParameter() {
    GeneratorStore store = GeneratorStore.create()
      .registerGenerators(new MethodIntParamProvider());

    Try<String> result = store.parseAndExecute("intGen{prefix: item, count: notAnInt}");
    assertThat("method generator should fail with invalid int", result.isFailure());
    assertThat("error message mentions integer conversion",
      result.getCause().getMessage().contains("expects an integer"), equalTo(true));
  }
}

