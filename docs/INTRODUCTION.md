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