---
title: Home
layout: default
nav_order: 1
---

This is a *bare-minimum* template to create a Jekyll site that uses the [Just the Docs] theme. You can easily set the created site to be published on [GitHub Pages] – the [README] file explains how to do that, along with other details.

## The Screenplay Pattern
<!--
@startuml screenplayPattern
!theme toy
skinparam nodesep 30
skinparam ranksep 30

together {
    actor #SlateGray "Actor" as actor
    agent Ability
    agent Client
}


rectangle Activities as "      Activities       " {
    
}

rectangle Act as "      Activities are       " {
    collections #White Tasks
    collections #White Interactions
}

agent Application

actor -r[thickness=2]-> Ability: "has"
Ability -r-> Client: "uses"
actor -d-> Activities: "executes"
Activities --[norank]> Ability: "uses"

Activities -d-> Application: "invokes"

Activities -[hidden]r-> Act: "executes"

Tasks -d-> Interactions: "consist of"
Tasks -> Tasks: "consist of"

@enduml
-->


![](readme/firstDiagram.svg)