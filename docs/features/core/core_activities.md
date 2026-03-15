---
title: Activities
parent: Core Features
layout: default
has_children: false
nav_order: 110
---

# Activities Overview

| Activity        | Description                                                                                     |
|-----------------|-------------------------------------------------------------------------------------------------|
| [See](#see)     | The `See` activity is used to assert the result of a task.                                      |
| [Retry](#retry) | The `Retry` activity is used to repeat a task until it succeeds or the timeout is reached.      |
| [Map](#map)     | The `Map` activity is used to transform the result of a task.                                   |
| [Sleep](#sleep) | The `Sleep` activity is used to pause the execution of the test for a specified amount of time. |

___
## Data Validation with See

### Validating Activity Results

Activities can be validated by calling `.is()` directly on them. This is the **recommended approach** for checking task results in the screenplay pattern.

The `.is()` method is available on all activity types:
- `Task<PT, RT>` - validates the return type RT
- `SupplierTask<RT>` - validates the return type RT  
- `ConsumerTask<PT>` - validates Void return
- `BasicInteraction` - validates Void return

Methods:

| type            | method                     | description                                                                                                      |
|-----------------|----------------------------|------------------------------------------------------------------------------------------------------------------|
| Activity method | `.is( SeeAssertion )`      | **Recommended**: Validate activity result directly. Accepts a SeeAssertion to check the result for conditions.   |
|                 | `.forAsLongAs(Duration x)` | Sets the timeout for which validation is repeated until the assertion succeeds.                                  |
|                 | `.every( Duration x )`     | Repeat the validation every x seconds until the assertion succeeds or timeout is reached.                        |
| static          | `See.ifThe( Activity )`    | Alternative verbose form. Wraps an activity explicitly (rarely needed).                                          |
| static          | `See.ifResult()...`        | Special case: validates the result passed from the previous activity.                                             |

> **Best Practice**: Use `.is()` directly on activities for clearer test flow. The `See.ifThe()` wrapper adds unnecessary verbosity in most cases.


### SeeAssertion

The ``SeeAssertion`` is a functional interface that is used to check the result of a task. It is used in the ``See`` 
activity and has the following declaration:

```java
@FunctionalInterface
public interface SeeAssertion<T1> {
  Either<ActivityError, Void> affirm(T1 actual);
}
```

Two of the most simplest implementations are:
```java
// assertion is always true -> Either.Right means the assertion was successful
actualValue -> Either.right(null);
```
and
```java
// assertion is always false -> Either.Left means the assertion failed
actualValue -> Either.left(new ActivityError("Assertion failed for actual " + actualValue));
```

A more meaningful example would be:

```java
// assertion that the actual value is greater than 5
SeeAssertion<Integer> expectedToBeGreaterThanFive = actualValue -> {
  if (actualValue > 5) {
    return Either.right(null);
  } else {
    return Either.left(new ActivityError("Value is not greater than 5"));
  }
};
```

And then used directly on an activity:

```java
actor.attemptsTo(
  TaskReturningRandomInteger.between(1,10)
    .is(expectedToBeGreaterThanFive));
```

### Expected SeeAssertion

To facilitate the creation of assertions, the `Expected` class is provided. It contains a set of static methods that
return `SeeAssertion` functions.

Methods:

| method                                        | description                                                                                                                             |
|-----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `Expected.to.pass( Predicate )`               | Accepts a predicate and checks if the result passes the predicate.                                                                      |
| `Expected.to.pass( Predicate, String reason)` | Accepts a predicate and checks if the result passes the predicate. In case of an error the reason will be printed in the error message. |
| `Expected.not.to.pass( Predicate )`           | Accepts a predicate and checks if the result DOES NOT pass the predicate.                                                               |
| `Expected.to.equal( String )`                 | Accepts an object and checks if the result equals the object.                                                                           |
| `Expected.to.be.equal( String )`              | same as above, the `.to.be` is just language candy                                                                                      |
| `Expected.not.to.equal( String )`             | Accepts an object and checks if the result equals the object.                                                                           |
| `Expected.not.to.be.equal( String )`          | same as above, the `.to.be` is just language candy                                                                                      |


**Example with Task:**
The task ``TaskReturningRandomInteger.between(1,10)`` is executed and the result is checked if it is greater than five.

```java
actor.attemptsTo(
  TaskReturningRandomInteger.between(1,10)
    .is(Expected.to.pass(randomInteger -> randomInteger >= 5)));
```

or by defining a predicate:

```java
Predicate<Integer> toBeGreaterThanFive = randomInteger -> randomInteger >= 5;

actor.attemptsTo(
  TaskReturningRandomInteger.between(1,10)
    .is(Expected.to.pass(toBeGreaterThanFive)));
```

or if you want to use the same predicate to check for lower than 5:

```java
actor.attemptsTo(
  TaskReturningRandomInteger.between(1,10)
    .is(Expected.not.to.pass(toBeGreaterThanFive)));
```

_Note: The verbose form `See.ifThe(task).is(...)` also works but is less concise._

### Assertion Chaining

You can chain multiple assertions by calling `.is()` multiple times. All assertions will be checked and the validation 
fails if any assertion fails. If multiple assertions fail, the error message will contain all failed assertions.

```java
actor.attemptsTo(
  // the random integer has to be greater than 5 AND a prime number
  // for multiple assertions the reason parameter is useful to identify which assertion failed
  TaskReturningRandomInteger.between(1,10)
    .is(Expected.to.pass(toBeGreaterThanFive, "check if greater than 5"))
    .is(Expected.to.pass(isPrimeNumber, "check if prime number")));
```

### Assertion Polling

Activity validation can be repeated until the assertion is successful or the timeout is reached. The `forAsLongAs()` method
is used to set the timeout. The `every()` method is used to set the interval at which the assertion is checked.

The RandomNumber task is executed and the result will be checked if the random integer is greater than 5 
every second for 10 seconds:

```java
actor.attemptsTo(
  TaskReturningRandomInteger.between(1,10)
    .is(Expected.to.pass(toBeGreaterThanFive))
    .forAsLongAs(Duration.ofSeconds(10))
    .every(Duration.ofSeconds(1)));
```

The default timeout is 0 seconds so the validation is executed only once. The default retry interval is 1 second 
(so it could be omitted in the example above).

This feature is useful for checking database entries, or waiting for a specific state in the application (e.g. waiting for
a GET call to return a specific value).

___
## Retry

The `Retry` activity is used to repeat a task until it succeeds or the timeout is reached. The `Retry` activity is
useful for tasks that are not deterministic, like waiting for a specific state in the application.

Methods:

| type   | method                   | description                                                                                      |
|--------|--------------------------|--------------------------------------------------------------------------------------------------|
| static | `Retry.task( Activity )` | Accepts a task as parameter and executes it. The result is checked for conditions.               |
|        | `.until( Predicate )`    | `.until()` accepts a ``Predicate``, checking the result for a specific condition                 |
|        | `.forAsLongAs(Duration)` | Sets the timeout for which the `Retry` activity is repeated and the result is checked.           |
|        | `.every( Duration )`     | Repeat the `Retry` activity every x Seconds until the  assertion succeeds or timeout is reached. |

Defaults:

| parameter      | default value |
|----------------|---------------|
| timeout        | 5 seconds     |
| retry interval | 1 second      |


The RandomNumber task is executed and the result will be checked if the random integer is greater than 5
every second for 5 seconds.

```java
actor.attemptsTo(
  Retry.task( TaskReturningRandomInteger.between(1,10) )
  .until( randomInteger -> randomInteger > 5 )
```


The RandomNumber task is executed and the result will be checked if the random integer is greater than 5
every three seconds for the maximum time of 10 seconds.

```java
actor.attemptsTo(
  Retry.task( TaskReturningRandomInteger.between(1,10) )
  .until( randomInteger -> randomInteger > 5 )
  .forAsLongAs(Duration.ofSeconds(10))
  .every(Duration.ofSeconds(3)));
```

You could create the same behavior with the `See` activity, but the `Retry` activity was specifically designed for 
polling tasks and is therefor simpler to use.

___
## Mapping

The `Mapping` feature is used to transform the result of a task. There are circumstances where its easier to transform the
result of a task with a function then creating a new Task.

The API class contains a static method `map` that accepts a function to which the result of the preceding task is passed.

Methods:

| type   | method                                                    | description                                                                                      |
|--------|-----------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| static | `API.map(Function<IN,OUT>, mapper)`                       | Accepts a mapper function and passes the result of the preceding task to it.                     |
| static | `API.map(Function<IN,OUT> mapper, String reason)`         | Same as above but adds the `reason` to the activity log. It makes the activity log more readable |
| static | `API.mapTry(Function<IN,Try<OUT>> mapper)`                | same as above but for functions known to throw an exception.                                     |
| static | `API.mapTry(Function<IN,Try<OUT>> mapper, String reason)` | same as above but again with a reason which is used for the activity log.                        |

```java
import static com.teststeps.thekla4j.core.activities.API.map;

actor.attemptsTo(
  
  TaskReturningRandomInteger.between(1,10),
  
  map( i -> i * 2,  "multiply the integer by 2"),

  See.ifResult()
    .is(Expected.to.pass( isEven, "check if the result is even")));

```

___
## Sleep

The `Sleep` activity is used to pause the execution of the test for a specified amount of time. It was implemented to
support Development and Debugging of Tests. Its not advisable to use it in production tests.

Methods:

| type   | method                           | description                                                          |
|--------|----------------------------------|----------------------------------------------------------------------|
| static | `Sleep.forA(Duration sleepTime)` | Stop test execution for the amount of time specified                 |
|        | `.because(String reason)`        | Set a reason for sleeping. It will be taken over to the activity log |


