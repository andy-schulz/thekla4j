---
title: Drag and Drop
parent: Browser
grand_parent: Web
layout: default
has_children: false
nav_order: 1220
---

# Drag and Drop Activity


## Overview

The `Drag` activity allows you to drag and drop elements in the browser using the Screenplay Pattern.

## Usage

### Basic Usage

```java
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.activities.Drag;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.selenium.Selenium;
import com.teststeps.thekla4j.core.base.persona.Actor;

class DragAndDropExample {
    
    public static void main(String[] args) {
        // Setup actor with browser capability
        Actor actor = Actor.named("Test User")
            .whoCan(BrowseTheWeb.with(Selenium.browser()));
        
        // Define elements
        Element draggableElement = Element.found(By.css("#draggable"))
            .withName("draggable item");
        
        Element dropZone = Element.found(By.css("#dropzone"))
            .withName("drop zone");
        
        // Perform drag and drop
        actor.attemptsTo(
            Drag.element(draggableElement).to(dropZone)
        );
    }
}
```

### With Retry

```java
// Drag and drop with automatic retry on failure
actor.attemptsTo(
    Drag.element(draggableElement).to(dropZone).retry()
);
```

### Using runAs

```java
// Direct execution with runAs
Drag.element(draggableElement).to(dropZone).runAs(actor);
```

## Implementation Details

The `Drag` activity is implemented using Selenium's `Actions` class with the `dragAndDrop()` method. It supports:

- **Selenium**: Full drag and drop support using WebDriver Actions API
- **Appium**: Mobile drag and drop support (delegates to Selenium implementation)
- **Frame Support**: Both source and target elements should be in the same frame context

## Method Chain

1. `Drag.element(sourceElement)` - Creates a new Drag activity with the source element
2. `.to(targetElement)` - Specifies the target element where the source should be dropped
3. `.runAs(actor)` or via `actor.attemptsTo()` - Executes the drag and drop action

## Error Handling

If the target element is not specified, an `ActivityError` is returned with the message:
```
No target element specified. Did you set the target element with Drag.element(SOURCE).to(TARGET)?
```

## Example Test

```java
import org.junit.jupiter.api.Test;

class DragAndDropTest {
    
    @Test
    void shouldDragElementToDropZone() {
        Actor user = Actor.named("User")
            .whoCan(BrowseTheWeb.with(Selenium.browser()));
        
        Element item = Element.found(By.id("item1"));
        Element target = Element.found(By.id("target-zone"));
        
        user.attemptsTo(
            Navigate.to("https://example.com/drag-drop"),
            Drag.element(item).to(target)
        );
        
        // Verify the item was moved
        user.attemptsTo(
            Ensure.that(Text.of(target)).contains("item1")
        );
    }
}
```

## Browser Support

| Browser Implementation | Support Status |
|------------------------|----------------|
| Selenium (Chrome)      | ✅ Supported    |
| Selenium (Firefox)     | ✅ Supported    |
| Selenium (Edge)        | ✅ Supported    |
| Selenium (Safari)      | ✅ Supported    |
| Appium (Mobile)        | ✅ Supported    |

## Related Activities

- [Click](./browser_activities.md#click) - Click on an element
- [DoubleClick](./browser_activities.md#doubleclick) - Double click on an element
- [Draw](./browser_activities.md#draw) - Draw shapes on elements
