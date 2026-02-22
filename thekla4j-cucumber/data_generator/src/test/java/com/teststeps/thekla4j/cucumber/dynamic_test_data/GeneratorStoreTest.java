package com.teststeps.thekla4j.cucumber.dynamic_test_data;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeneratorStoreTest {

  private final DataGenerator simpleGenerator = parameterMap -> Try.success("Hello World");
  private final DataGenerator jsonGenerator = parameterMap -> Try.success("""
      {
        "name": "Test",
        "value": "TestValue",
        "details": {
          "key": "value"
        },
        "emptyDetails": {}
      }
      """);

  private final DataGenerator failingJson = parameterMap -> Try.success("{ name:: Test }");

  private final DataGenerator simpleGeneratorWithParameter = parameterMap -> Try.success(parameterMap.foldLeft("", (s, entry) -> s + entry._2));

  @Test
  public void assignGeneratorWithInvalidName() {
    @SuppressWarnings("deprecation")
    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> GeneratorStore.create().addGenerator("Invalid!Generator", simpleGenerator));

    assertThat("correct error message is thrown",
      thrown.getMessage(),
      equalTo("Generator name 'Invalid!Generator' is invalid. Only alphanumeric characters are allowed [A-Za-z0-9]+"));

  }

  @Test
  public void executeSimpleGenerator() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator);

    String generatorString = "simpleGenerator{TestString}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);
    assertThat("executing the generator succeeded", result.isSuccess());
    assertThat("executing the generator returned the expected value", result.get().equals("Hello World"));

  }

  @Test
  public void executeSimpleGeneratorWithAssignment() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator);

    String generatorString = "simpleGenerator{TestString} => ${TEST}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);
    assertThat("executing the generator succeeded", result.isSuccess());
    assertThat("executing the generator returned the expected value", result.get(), equalTo("Hello World"));

    Try<String> storedResult = generatorStore.parseAndExecute("${TEST}");
    assertThat("retrieving the stored parameter succeeded", storedResult.isSuccess());
    assertThat("retrieved value is correct", storedResult.get(), equalTo("Hello World"));

  }

  @Test
  public void assignToInvalidParameterName() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator);

    String generatorString = "simpleGenerator{TestString} => ${TEST.name}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);
    assertThat("executing the generator should fail", result.isFailure(), equalTo(true));
    assertThat("Thrown error",
      result.getCause().getMessage(),
      equalTo("Cant assign value to parameter named: TEST.name. Parameter names must match: [A-Za-z0-9_]+"));
  }

  @Test
  public void accessingValidAttributeOfParameterJson() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "${TEST.details.key}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("executing the generator succeeded", retrievalResult.isSuccess());
    assertThat("executing the generator returned the expected value", retrievalResult.get(), equalTo("value"));
  }

  @Test
  public void accessingNotExistingAttributeOfParameter() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "${TEST.details.key1}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("retrieving the stored parameter should fail", retrievalResult.isFailure(), equalTo(true));
    assertThat("retrieving the stored parameter failed with the correct error message",
      retrievalResult.getCause().getMessage(),
      equalTo("Cant get value of attribute: key1. Attribute not found in JSONObject: {\"key\":\"value\"}"));
  }

  @Test
  public void accessingNotExistingParentAttributeOfParameter() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
      .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "${TEST.emptyDetails.key}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("retrieving the stored parameter should fail", retrievalResult.isFailure(), equalTo(true));

  }

  @Test
  public void accessingAttributeOfString() {

    @SuppressWarnings("deprecation")
    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "${TEST.name.firstname}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("retrieving the stored parameter should fail", retrievalResult.isFailure(), equalTo(true));
    assertThat("retrieving the stored parameter failed with the correct error message",
      retrievalResult.getCause().getMessage(),
      equalTo("Cant get value of attribute 'firstname' from object 'Test'. Is not a JSONObject"));
  }

  @Test
  public void accessingAttributeOfInvalidJsonObject() {

      GeneratorStore generatorStore = GeneratorStore.create()
          .addGenerator("failingJson", failingJson);

      String generatorString = "failingJson{TestString} => ${TEST}";
      generatorStore.parseAndExecute(generatorString);

      String retrievalString = "${TEST.name}";
      Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

      assertThat("executing the generator should fail", retrievalResult.isFailure(), equalTo(true));
      assertThat("Thrown error",
        retrievalResult.getCause().getMessage(),
        equalTo("Cant get value of parameter. Parameter is not a valid json: { name:: Test }"));
  }

  @Test
  public void retrieveNotExistingParameter() {

    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("failingJson", failingJson);

    String generatorString = "failingJson{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "${TEST1}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("retrieving an attribute on a normal string should fail",
      retrievalResult.isFailure(), equalTo(true));
    assertThat("retrieving the attribute of parameter failed with the correct error message",
      retrievalResult.getCause().getMessage(),
      equalTo("Parameter not found: TEST1"));
  }

  @Test
  public void retrieveInvalidParameter() {

      GeneratorStore generatorStore = GeneratorStore.create()
          .addGenerator("failingJson", failingJson);

      String generatorString = "failingJson{TestString} => ${TEST}";
      generatorStore.parseAndExecute(generatorString);

      String retrievalString = "${TEST::}";
      Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

      assertThat("retrieving an attribute on an invalid parameter string should fail",
        retrievalResult.isFailure(), equalTo(true));
      assertThat("retrieving the attribute of parameter failed with the correct error message",
        retrievalResult.getCause().getMessage(),
        equalTo("Cant get value of parameter named: TEST::. Parameter names must match: [A-Za-z0-9_.]+"));
  }

  @Test
  public void simpleTestString() {

    GeneratorStore generatorStore = GeneratorStore.create();
    String generatorString = "My New String";

    Try<String> result = generatorStore.parseAndExecute(generatorString);
    assertThat("executing the generator succeeded", result.isSuccess());
    assertThat("executing the generator returned the expected value", result.get(), equalTo("My New String"));

  }

  @Test
  public void usingANormalStringWithGenerator() {

    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator);

    String generatorString = "Standard Generator String";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));
    assertThat("executing the generator returned the expected value", result.get().equals("Standard Generator String"));

  }

  @Test
  public void usingNotExistingGeneratorFunction() {

    GeneratorStore generatorStore = GeneratorStore.create();

    String generatorString = "nonExistingGenerator{TestString}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess());
    assertThat("non existing generator string is passed", result.get(), equalTo("nonExistingGenerator{TestString}"));
  }

  @Test
  public void parameterizedGenerator() {

    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGeneratorWithParameter);

    String generatorString = "simpleGenerator{p1: one, p2: two, p3: three}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess());
    assertThat("executing the generator returned the expected value", result.get(), equalTo("onetwothree"));

  }

  @Test
  public void addingTheSameDataGenerator() {

    Throwable thrown = assertThrows(IllegalArgumentException.class, () -> GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator)
        .addGenerator("simpleGenerator", simpleGenerator));

    assertThat("correct error message is thrown", thrown.getMessage(), equalTo("Generator with name 'simpleGenerator' already exists"));
  }

  @Test
  public void replaceParameterWithinString() {

    GeneratorStore generatorStore = GeneratorStore.create()
        .addGenerator("simpleGenerator", simpleGenerator);

    String generatorString = "simpleGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "Calling ${TEST}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("executing the generator succeeded", retrievalResult.isSuccess());
    assertThat("executing the generator returned the expected value", retrievalResult.get(), equalTo("Calling Hello World"));

  }

  @Test
  public void replaceParameterAttributeWithinString() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "Calling ${TEST.name}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("executing the generator succeeded", retrievalResult.isSuccess());
    assertThat("executing the generator returned the expected value", retrievalResult.get(), equalTo("Calling Test"));

  }

  @Test
  public void replaceMultipleParameterAttributesWithinString() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addGenerator("jsonGenerator", jsonGenerator);

    String generatorString = "jsonGenerator{TestString} => ${TEST}";
    generatorStore.parseAndExecute(generatorString);

    String retrievalString = "Calling ${TEST.name} on Object ${TEST}";
    Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

    assertThat("executing the generator succeeded", retrievalResult.isSuccess());
    assertThat("executing the generator returned the expected value", retrievalResult.get(),
      equalTo("Calling Test on Object {\n  \"name\": \"Test\",\n  \"value\": \"TestValue\",\n  \"details\": {\n    \"key\": \"value\"\n  },\n  \"emptyDetails\": {}\n}\n"));


    String retrievalString2 = "Calling ${TEST.name} with value ${TEST.value}";
    Try<String> retrievalResult2 = generatorStore.parseAndExecute(retrievalString2);

    assertThat("executing the generator succeeded", retrievalResult2.isSuccess());
    assertThat("executing the generator returned the expected value", retrievalResult2.get(),
      equalTo("Calling Test with value TestValue"));
  }

  @Test
  public void replaceParameterWithAssignmentOperator() {

      GeneratorStore generatorStore = GeneratorStore.create()
          .addGenerator("simpleGenerator", simpleGenerator);

      String generatorString = "simpleGenerator{TestString} => ${TEST}";
      generatorStore.parseAndExecute(generatorString);

      String retrievalString = "Calling => ${TEST}";
      Try<String> retrievalResult = generatorStore.parseAndExecute(retrievalString);

      assertThat("executing the generator succeeded", retrievalResult.isSuccess());
      assertThat("executing the generator returned the expected value", retrievalResult.get(), equalTo("Calling"));
  }


  @Test
  public void replaceSingleInlineGenerator() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));


    String generatorString = "?{INLINE}";
    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));
    assertThat("inline generator value is replaced", result.get(), equalTo("1234567890"));

  }

  @Test
  public void replaceMultipleInlineGenerators() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));

    String generatorString = "?{INLINE} ?{INLINE} ?{INLINE}";
    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));
    assertThat("inline generator value is replaced", result.get(), equalTo("1234567890 1234567890 1234567890"));

  }

  @Test
  public void replaceInlineGeneratorWithinText() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));

    String generatorString = "This is a test string with an inline generator ?{INLINE}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));
    assertThat("inline generator value is replaced", result.get(),
      equalTo("This is a test string with an inline generator 1234567890"));


  }

  @Test
  public void assignInlineGeneratorToParameter() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));

    String generatorString = "?{INLINE} => ${TEST}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));

    Try<String> storedResult = generatorStore.parseAndExecute("${TEST}");

    assertThat("retrieving the stored parameter succeeded", storedResult.isSuccess(), equalTo(true));
    assertThat("retrieved value is correct", storedResult.get(), equalTo("1234567890"));

  }

  @Test
  public void assignInlineGeneratorWithTextToParameter() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));

    String generatorString = "This is a test string with an inline generator ?{INLINE} => ${TEST}";

    Try<String> result = generatorStore.parseAndExecute(generatorString);

    assertThat("executing the generator succeeded", result.isSuccess(), equalTo(true));

    Try<String> storedResult = generatorStore.parseAndExecute("${TEST}");

    assertThat("retrieving the stored parameter succeeded", storedResult.isSuccess(), equalTo(true));
    assertThat("retrieved value is correct", storedResult.get(), equalTo("This is a test string with an inline generator 1234567890"));

  }

  @Test
  public void testToStringMethod() {

    GeneratorStore generatorStore = GeneratorStore.create()
      .addInlineGenerator("INLINE", () -> Try.success("1234567890"));

    assertThat("toString method returns correct string",
      generatorStore.toString(),
      equalTo("\u001B[36mGeneratorFunction: randomString\u001B[0m\n" +
        "\u001B[32mrandomString{length: int, prefix: string, specialChars: boolean}\u001B[0m\n" +
        "\u001B[32mrandomString{256} -> length: 256\u001B[0m\n" +
        "\u001B[32m\u001B[0m\n" +
        "\u001B[32m    Generate a random string with a given prefix and length.\u001B[0m\n" +
        "\u001B[32m    length:        the generated string will have this length,             restriction: length > prefix.length,     default 8,\u001B[0m\n" +
        "\u001B[32m    prefix:        the generated string will be prefixed by this string,                                            default: empty string.\"\"\u001B[0m\n" +
        "\u001B[32m    specialChars:  the generated string will include special characters    ( !@#$%^&*()-_=+[]}|;:,.<>? )            default: false.\u001B[0m"));

  }
}
