---
title: Core Features
layout: default
has_children: true
nav_order: 100
---

# Core Features

## The Actor

The actor is the main entry point for all interactions with the system under test. It represents the user that
interacts with the system. The interactions are performed using task.

An actor is created using the `Actor` class by passing a name and a list of abilities.
The abilities are using libraries and frameworks to send http requests, perform actions on a browser or accessing
information in a database.

```java
import java.lang.annotation.Target;

class Test {

    @Test
    void test() {
        Actor actor = Actor.named("John")
        .whoCan(BrowseTheWeb.with(CLIENT));

      actor.attemptsTo(
          Navigate.to("www.google.com"),
          Enter.text("thekla4j"),into(SearchField),
          Click.on(SearchButton),
          See.ifThe(Text.of(SearchResult))
             .is(Expected.to.be.equal("thekla4j")));
    }
}

```

### Actor Methods

To execute tasks, the actor has the following methods:

| Method                                                                 | Description                                                                       |
|------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `named( String name )`                                                 | Sets the abilities for the actor.                                                 |
| `getName()`                                                            | Returns the name of the actor.                                                    |
| `whoCan( Ability... abilities )`                                       | Sets the abilities for the actor.                                                 |
| `attemptsTo( Task... tasks )`                                          | Executes the given tasks in order.                                                |
| `withAbilityTo( Class<Ability abilityClass> )`                         | Returns the ability of the given class.                                           |
| `cleansStage()`                                                        | Cleans all abilities with its clients                                             |
| `attemptsTo(Task... tasks)`                                            | Executes the given tasks in order.                                                |
| `attemptsTo_(Task... tasks)`                                           | Returns a `Function<I,O>` accepting a parameter which is passed to the first task |
| `attemptsTo$(Task... tasks, String stepName, String stepDescription)`  | Executes the given tasks in order and logs the step.                              |
| `attemptsTo$_(Task... tasks, String stepName, String stepDescription)` | Returns a `Function<I,O>` accepting a parameter which is passed to the first task |

## Performer Decorator

The `Performer` decorator extends the functionality of the `Actor` class by allowing it to perform activities and handle
`ActivityError` exceptions.
With the `Performer` decorator, the user dont have to handle The `Either<ActivityError, O>` result of the attemptsTo
method.

This decorator provides methods to execute multiple activities in sequence, with each method returning the result of the
final activity or throwing an `ActivityError` in case of failure.

### Performer Methods

The `Performer` interface provides the following methods:

### Performer Methods

The `Performer` interface provides the following methods:

| Method                                                                 | Description                                                                       |
|------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `of(Actor actor)`                                                      | Creates a new `Performer` instance for the given `Actor`.                         |
| `attemptsTo(Task... tasks)`                                            | Executes the given tasks in order.                                                |
| `attemptsTo_(Task... tasks)`                                           | Returns a `Function<I,O>` accepting a parameter which is passed to the first task |
| `attemptsTo$(Task... tasks, String stepName, String stepDescription)`  | Executes the given tasks in order and logs the step.                              |
| `attemptsTo$_(Task... tasks, String stepName, String stepDescription)` | Returns a `Function<I,O>` accepting a parameter which is passed to the first task |

### Example Usage

```java
class Test {
 
    @Test
    void test() {
        Actor actor = Actor.named("John")
        .whoCan(BrowseTheWeb.with(CLIENT));
        
        Performer performer = Performer.of(actor);
        
        try{
        
        String result = performer.attemptsTo(
            Navigate.to("www.google.com"),
            Enter.text("thekla4j").into(SearchField),
            Click.on(SearchButton),
            See.ifThe(Text.of(SearchResult)).is(Expected.to.equal("thekla4j"))
        );
        
        System.out.println("Result: " + result);
        
        } catch (ActivityError e){
          e.printStackTrace();
        }
    }
}
```

## Abilities

## Creating Custom Tasks

In the thekla4j framework, tasks are the fundamental units of work that an Actor can perform.
There are different types of tasks, each serving a specific purpose.
Below is a description of the different task types and a guide on how to create custom tasks.

### Task Types

To create a custom task, you have the possibility to .

#### Task

A Task represents a unit of work that an Actor can perform. It can have input parameters and return a result.
Example: Authorize to a backend and returning the token.

````java
public class Authorize implements Task<User, String> {
    private final String url;
  
    public Authorize(String url) {
        this.url = url;
    }

    @Override
    public Either<ActivityError, String> performAs(Actor actor) {
        // Perform the authorization task by sending one or multiple requests to the backend
        return Either.right("token");
    }

    public static Authorize toBackend(String url) {
        return new Authorize(url);
    }
}
````

#### BasicInteraction
A BasicInteraction is a simple task that performs an action without returning a result.
Example: a sequence of activities on a UI

```java
class UploadAFile implements BasicInteraction {
    private final String filePath;
  
    public UploadAFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Either<ActivityError, Void> performAs(Actor actor) {
        // Perform the upload task by clicking on the upload button and selecting the file
      return actor.atttemptsTo(
          Click.on(UploadButton),
          Enter.theValue(filePath).into(UploadField)
      );
    }

    public static UploadAFile from(String filePath) {
        return new UploadAFile(filePath);
    }
}
```

#### ConsumerTask
A ConsumerTask is a task that consumes an input and performs an action without returning a result.
Example: Logging a message, updating a database record.

```java
class WriteSystemLog extends ConsumerTask<String> {
  
    SystemLog log = new SystemLog();
  
    @Override
    protected Either<ActivityError, Void> performAs(Actor actor, String message) {
      
        if(log.isWritable()){
            log.write(message);
            return Either.right(null);
        }
        
        return Either.left(new ActivityError("System log is not writable"));
        
    }

    public static LogMessage ofIncomingMessage() {
        return new LogMessage();
    }
}
```

#### SupplierTask
A SupplierTask is a task that supplies a result without requiring any input.
Example: Generating a random number, fetching the current date and time.

```java
class GetCurrentTime extends SupplierTask<Long> {
    @Override
    protected Either<ActivityError, Long> performAs(Actor actor) {
        // Return the current time
        return Either.right(System.currentTimeMillis());
    }

    public static GetCurrentTime now() {
        return new GetCurrentTime();
    }
}
```

### Retrying Interactions

All task types support a `retry()` mechanism, but the signature differs slightly depending on whether the task returns a value or not.

#### BasicInteraction and ConsumerTask
For tasks that do not return a value (`BasicInteraction` and `ConsumerTask`), the `retry()` method takes no arguments. The task is retried until it succeeds (i.e., does not return an error) or the timeout is reached.

```java
// BasicInteraction
actor.attemptsTo(
    Click.on(SubmitButton).retry()
);

// ConsumerTask
actor.attemptsTo(
    WriteSystemLog.ofIncomingMessage().retry()
);
```

#### SupplierTask and Task
For tasks that return a value (`SupplierTask` and `Task`), the `retry()` method requires a `Predicate` to determine if the result is acceptable. The task is retried until the predicate returns `true` or the timeout is reached.

```java
// SupplierTask
actor.attemptsTo(
    GetCurrentTime.now().retry(time -> time > expectedTime)
);

// Task
actor.attemptsTo(
    Authorize.toBackend(url).retry(token -> token != null && !token.isEmpty())
);
```

The default retry duration is 5 seconds for all task types. You can customize the duration using `.forAsLongAs(Duration)` on the returned `Retry` object.

## Task Execution

## Passing Values to and between Tasks

## The Activity Log

## Actors World

# Functional Concepts

## Either

## Try

## Option

## Lists

## Maps / HashMaps