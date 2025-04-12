---
title: Tasks
parent: Core Features
layout: default
has_children: false
nav_order: 120
---

# Creating Custom Tasks

In the thekla4j framework, tasks are the fundamental units of work that an Actor can perform. 
There are different types of tasks, each serving a specific purpose. 
Below is a description of the different task types and a guide on how to create custom tasks.

## Task Types

### Task

A Task represents a unit of work that an Actor can perform. It can have input parameters and return a result.
Example: Authorize to a backend and returning the token.

````java
public class Authorize implements Task<User, String> {
    private final String url;
  
    public Authorize(String url) {
        this.url = url;
    }

    @Override
    public String perform(Actor actor) {
        // Perform the authorization task by sending one or multiple requests to the backend
        return "token";
    }

    public static Authorize toBackend(String url) {
        return new Authorize(url);
    }
}
````

### BasicInteraction  
A BasicInteraction is a simple task that performs an action without returning a result.
Example: Clicking a button, entering text into a field.

```java

```

### ConsumerTask  
A ConsumerTask is a task that consumes an input and performs an action without returning a result.
Example: Logging a message, updating a database record.


### SupplierTask  
A SupplierTask is a task that supplies a result without requiring any input.
Example: Generating a random number, fetching the current date and time.