---
title: Thekla4j
layout: default
nav_order: 1
---

# Thekla4j



## The Screenplay Pattern

```mermaid
stateDiagram
    Actor --> Activities: executes
    Activities --> Application: invokes
    Actor --> Ability: uses
    Ability --> Client: uses
    
    Activities --> Ability: uses
    
```