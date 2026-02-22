---
title: Key and Mouse Actions
parent: Browser
grand_parent: Web
layout: default
has_children: false
nav_order: 1230
---

# Key and Mouse Actions

Advanced keyboard and mouse interactions for complex user scenarios.

## Key Interactions

___

### DoKey

The `DoKey` activity allows you to perform keyboard interactions including key presses, key combinations, and key sequences. It provides a fluent API for chaining multiple key actions together.

Methods:

| type      | name                                | description                                        |
|-----------|-------------------------------------|----------------------------------------------------|
| static    | ``press( Key... keys )``            | Presses the given key / key combination.           |
| static    | ``pressAndHold( Key... keys )``     | Presses and hold the given key / key combinations  |
| static    | ``release( Key... keys )``          | Releases the given key / key combination.          |
|           | ``thenPressAndHold( Key... keys )`` | Presses and holds then given key / key combination |
|           | ``thenRelease( Key... keys )``      | Releases the given key / key combination.          |
|           | ``thenPress( Key... keys )``        | Presses the given key / key combination.           |
|           | ``thenPause(Duration duration)``    | Pauses for the specified duration.                 |
| inherited | ``retry()``                         | Retries the key action until it succeeds.          |

Returns:

- ``Either<ActivityError, Void>``

**Code:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
  }

  @Test
  void attemptsToTest() {
    actor.attemptsTo(
        DoKey.press(Key.ENTER),

        DoKey.pressAndHold(Key.CONTROL, Key.ALT),

        DoKey.release(Key.CONTROL, Key.ALT),

        DoKey.press(Key.ENTER)
            .thenPressAndHold(Key.CONTROL, Key.ALT),

        DoKey.pressAndHold(Key.CONTROL, Key.ALT)
            .thenPress(Key.ENTER)
            .thenRelease(Key.CONTROL, Key.ALT),

        DoKey.press(Key.ENTER)
            .thenPress(Key.ENTER));
  }

  @Test
  void runAsTest() {
    DoKey.press(Key.ENTER).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).thenRelease(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.press(Key.ENTER).thenPressAndHold(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.pressAndHold(Key.CONTROL, Key.ALT).thenRelease(Key.CONTROL, Key.ALT).runAs(actor);
    DoKey.press(Key.ENTER).thenPress(Key.ENTER).runAs(actor);
  }
}
```

**Common Use Cases:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element inputField = Element.found(By.css("#inputField")).called("Input Field");
  }

  @Test
  void copyPaste() {
    // Select all and copy
    actor.attemptsTo(
        Click.on(inputField),
        DoKey.pressAndHold(Key.CONTROL)
            .thenPress(Key.A)
            .thenPress(Key.C)
            .thenRelease(Key.CONTROL)
    );

    // Paste
    actor.attemptsTo(
        DoKey.pressAndHold(Key.CONTROL)
            .thenPress(Key.V)
            .thenRelease(Key.CONTROL)
    );
  }

  @Test
  void tabNavigation() {
    // Navigate with TAB and press ENTER
    actor.attemptsTo(
        DoKey.press(Key.TAB, Key.TAB, Key.TAB)
            .thenPress(Key.ENTER)
    );
  }

  @Test
  void escapeKey() {
    // Close modal with ESC
    actor.attemptsTo(
        DoKey.press(Key.ESCAPE)
    );
  }
}
```

___
___

## Mouse Interactions

___

### DoMouse

The `DoMouse` activity allows you to perform complex mouse interactions including clicks, movements, drag and drop operations, and more. It provides a fluent API for chaining multiple mouse actions together.

Methods:

| type      | name                                              | description                                                       |
|-----------|---------------------------------------------------|-------------------------------------------------------------------|
| static    | ``click()``                                       | Clicks at the current mouse position.                             |
| static    | ``click(Element element)``                        | Clicks on the specified element.                                  |
| static    | ``doubleClick()``                                 | Double-clicks at the current mouse position.                      |
| static    | ``doubleClick(Element element)``                  | Double-clicks on the specified element.                           |
| static    | ``contextClick()``                                | Context-clicks (right-click) at the current mouse position.       |
| static    | ``contextClick(Element element)``                 | Context-clicks (right-click) on the specified element.            |
| static    | ``clickAndHold()``                                | Clicks and holds at the current mouse position.                   |
| static    | ``clickAndHold(Element element)``                 | Clicks and holds on the specified element.                        |
| static    | ``release()``                                     | Releases the mouse button at the current position.                |
| static    | ``release(Element element)``                      | Releases the mouse button on the specified element.               |
| static    | ``moveTo(Element element)``                       | Moves the mouse to the center of the specified element.           |
| static    | ``moveTo(Element element, int x, int y)``         | Moves the mouse to the specified element with offset.             |
| static    | ``moveByOffset(int x, int y)``                    | Moves the mouse by the specified offset from current position.    |
| static    | ``dragAndDrop(Element source, Element target)``   | Drags the source element and drops it on the target element.      |
|           | ``thenPause(Duration duration)``                  | Pauses for the specified duration.                                |
|           | ``thenClick()``                                   | Then clicks at the current mouse position.                        |
|           | ``thenClick(Element element)``                    | Then clicks on the specified element.                             |
|           | ``thenDoubleClick()``                             | Then double-clicks at the current mouse position.                 |
|           | ``thenDoubleClick(Element element)``              | Then double-clicks on the specified element.                      |
|           | ``thenContextClick()``                            | Then context-clicks at the current mouse position.                |
|           | ``thenContextClick(Element element)``             | Then context-clicks on the specified element.                     |
|           | ``thenClickAndHold()``                            | Then clicks and holds at the current mouse position.              |
|           | ``thenClickAndHold(Element element)``             | Then clicks and holds on the specified element.                   |
|           | ``thenRelease()``                                 | Then releases the mouse button at the current position.           |
|           | ``thenRelease(Element element)``                  | Then releases the mouse button on the specified element.          |
|           | ``thenMoveTo(Element element)``                   | Then moves the mouse to the specified element.                    |
|           | ``thenMoveTo(Element element, int x, int y)``     | Then moves the mouse to the specified element with offset.        |
|           | ``thenMoveByOffset(int x, int y)``                | Then moves the mouse by offset from current position.             |
| inherited | ``retry()``                                       | Retries the mouse action until it succeeds.                       |

Returns:

- ``Either<ActivityError, Void>``

**Simple Click Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element button = Element.found(By.css("#submitButton")).called("Submit Button");
  }

  @Test
  void clickExamples() {
    // Simple click
    actor.attemptsTo(
        DoMouse.click(button)
    );

    // Double click
    actor.attemptsTo(
        DoMouse.doubleClick(button)
    );

    // Context click (right-click)
    actor.attemptsTo(
        DoMouse.contextClick(button)
    );
  }

  @Test
  void runAsTest() {
    DoMouse.click(button).runAs(actor);
    DoMouse.doubleClick(button).runAs(actor);
    DoMouse.contextClick(button).runAs(actor);
  }
}
```

**Drag and Drop Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element draggable = Element.found(By.css("#draggable")).called("Draggable Item");
    Element dropzone = Element.found(By.css("#dropzone")).called("Drop Zone");
  }

  @Test
  void simpleDragAndDrop() {
    // Simple drag and drop
    actor.attemptsTo(
        DoMouse.dragAndDrop(draggable, dropzone)
    );
  }

  @Test
  void customDragAndDrop() {
    // Custom drag and drop with pauses
    actor.attemptsTo(
        DoMouse.clickAndHold(draggable)
            .thenPause(Duration.ofMillis(300))
            .thenMoveTo(dropzone)
            .thenPause(Duration.ofMillis(100))
            .thenRelease()
    );
  }
}
```

**Angular CDK Drag and Drop Example:**

Angular CDK requires a specific sequence of mouse actions with proper timing and threshold movement:

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    
    Element sourceItem = Element.found(By.css("[data-id='item-2']")).called("Source Item");
    Element targetItem = Element.found(By.css("[data-id='item-0']")).called("Target Item");
    Element dragHandle = Element.found(By.css("[data-id='item-2'] .drag-handle")).called("Drag Handle");
  }

  @Test
  void angularCdkDragAndDrop() {
    // Calculate offset between source and target
    int offsetX = 0;   // Replace with actual calculation
    int offsetY = -94; // Replace with actual calculation

    actor.attemptsTo(
        DoMouse.clickAndHold(dragHandle)
            .thenPause(Duration.ofMillis(400))           // Wait for Angular CDK to initialize
            .thenMoveByOffset(0, 15)                     // Exceed drag threshold (5-10px)
            .thenPause(Duration.ofMillis(300))           // Let Angular CDK activate drag mode
            .thenMoveByOffset(offsetX, offsetY - 15)     // Move to target (subtract threshold)
            .thenPause(Duration.ofMillis(100))           // Wait before release
            .thenRelease()                               // Drop the element
    );
  }
}
```

**Mouse Movement Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element menuItem = Element.found(By.css("#menuItem")).called("Menu Item");
    Element subMenuItem = Element.found(By.css("#subMenuItem")).called("Sub Menu Item");
  }

  @Test
  void hoverAndClick() {
    // Hover over menu to reveal submenu, then click submenu item
    actor.attemptsTo(
        DoMouse.moveTo(menuItem)
            .thenPause(Duration.ofMillis(500))  // Wait for submenu to appear
            .thenClick(subMenuItem)
    );
  }

  @Test
  void preciseMovement() {
    // Move to element with offset
    actor.attemptsTo(
        DoMouse.moveTo(menuItem, 10, 20)  // 10px right, 20px down from center
            .thenClick()
    );

    // Move by offset from current position
    actor.attemptsTo(
        DoMouse.moveByOffset(50, 0)  // Move 50px to the right
            .thenClick()
    );
  }
}
```

**Complex Chaining Example:**

```java
class TestSuite {

  @BeforeAll
  static void setup() {
    Actor actor = Actor.named("Test User").whoCan(BrowseTheWeb.with(Selenium.browser()));
    Element canvas = Element.found(By.css("#drawingCanvas")).called("Canvas");
  }

  @Test
  void drawOnCanvas() {
    // Draw a square on a canvas
    actor.attemptsTo(
        DoMouse.moveTo(canvas, -50, -50)  // Top-left corner
            .thenClickAndHold()             // Start drawing
            .thenMoveByOffset(100, 0)       // Right
            .thenMoveByOffset(0, 100)       // Down
            .thenMoveByOffset(-100, 0)      // Left
            .thenMoveByOffset(0, -100)      // Up
            .thenRelease()                  // Stop drawing
    );
  }
}
```

**Key Points:**

1. **Fluent API**: Chain multiple mouse actions using `then...()` methods
2. **Element-based**: Works with `Element` objects for browser-independence
3. **Timing Control**: Use `thenPause()` for precise timing between actions
4. **Offset Support**: Both element-relative and position-relative offsets
5. **Angular CDK Support**: Proper sequencing for Angular CDK drag operations

**Tips for Angular CDK Drag and Drop:**

- Always use `clickAndHold()` on the drag handle, not the item itself
- Include a `pause` after `clickAndHold` (300-500ms) to let Angular CDK initialize
- Move at least 5-15px to exceed the drag threshold
- Add another `pause` (200-400ms) after threshold movement to activate drag mode
- Use `moveByOffset()` for precise control over drag distance
- Include a final `pause` (50-100ms) before `release()`

**Understanding the Drag Threshold:**

The drag threshold is a minimum distance (typically 5-10 pixels) that the mouse must move after `mousedown` before Angular CDK recognizes it as a drag operation. This prevents accidental drags from simple clicks.

```java
// Without threshold movement - won't work:
DoMouse.clickAndHold(dragHandle)
    .thenMoveTo(target)  // Angular CDK not activated yet!
    .thenRelease();

// With threshold movement - works:
DoMouse.clickAndHold(dragHandle)
    .thenPause(Duration.ofMillis(400))    // Let Angular CDK prepare
    .thenMoveByOffset(0, 15)              // Exceed threshold - activates drag!
    .thenPause(Duration.ofMillis(300))    // Let drag mode activate
    .thenMoveTo(target)                   // Now the drag works
    .thenRelease();
```

