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
import javax.naming.directory.InvalidSearchFilterException;

Actor actor = Actor.named("John")
  .whoCan(BrowseTheWeb.with(CLIENT));

actor.attemptsTo(
  Navigate.to("www.google.com"),
  Enter.text("thekla4j"),into(SearchField),
  Click.on(SearchButton),
  See.ifThe(Text.of(SearchResult))
  .is(Expected.to.be.equal("thekla4j")));
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



## Abilities

## Task Creation

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