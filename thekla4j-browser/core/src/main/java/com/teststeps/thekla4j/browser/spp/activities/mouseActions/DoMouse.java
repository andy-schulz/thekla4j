package com.teststeps.thekla4j.browser.spp.activities.mouseActions;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;

/**
 * Perform mouse actions
 */
@Log4j2(topic = "MouseAction")
@Action("perform mouse actions @{actions}")
public class DoMouse extends BasicInteraction {

  @Called(name = "actions")
  private List<MouseAction> actions;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
        .onSuccess(__ -> log.info(() -> "Performing mouse actions: %s".formatted(actions.mkString(", "))))
        .flatMap(b -> b.executeMouseActions(actions))
        .transform(ActivityError.toEither("Error while executing mouse actions"))
        .map(__ -> null);
  }

  /**
   * Click at the current mouse position
   *
   * @return the DoMouse task
   */
  public static DoMouse click() {
    return new DoMouse(List.of(new Click()));
  }

  /**
   * Click on an element
   *
   * @param element the element to click
   * @return the DoMouse task
   */
  public static DoMouse click(Element element) {
    return new DoMouse(List.of(new Click(element)));
  }

  /**
   * Double click at the current mouse position
   *
   * @return the DoMouse task
   */
  public static DoMouse doubleClick() {
    return new DoMouse(List.of(new DoubleClick()));
  }

  /**
   * Double click on an element
   *
   * @param element the element to double click
   * @return the DoMouse task
   */
  public static DoMouse doubleClick(Element element) {
    return new DoMouse(List.of(new DoubleClick(element)));
  }

  /**
   * Context click (right click) at the current mouse position
   *
   * @return the DoMouse task
   */
  public static DoMouse contextClick() {
    return new DoMouse(List.of(new ContextClick()));
  }

  /**
   * Context click (right click) on an element
   *
   * @param element the element to context click
   * @return the DoMouse task
   */
  public static DoMouse contextClick(Element element) {
    return new DoMouse(List.of(new ContextClick(element)));
  }

  /**
   * Click and hold at the current mouse position
   *
   * @return the DoMouse task
   */
  public static DoMouse clickAndHold() {
    return new DoMouse(List.of(new ClickAndHold()));
  }

  /**
   * Click and hold on an element
   *
   * @param element the element to click and hold
   * @return the DoMouse task
   */
  public static DoMouse clickAndHold(Element element) {
    return new DoMouse(List.of(new ClickAndHold(element)));
  }

  /**
   * Release the mouse button at the current position
   *
   * @return the DoMouse task
   */
  public static DoMouse release() {
    return new DoMouse(List.of(new Release()));
  }

  /**
   * Release the mouse button on an element
   *
   * @param element the element to release on
   * @return the DoMouse task
   */
  public static DoMouse release(Element element) {
    return new DoMouse(List.of(new Release(element)));
  }

  /**
   * Move to an element
   *
   * @param element the element to move to
   * @return the DoMouse task
   */
  public static DoMouse moveTo(Element element) {
    return new DoMouse(List.of(new MoveToElement(element)));
  }

  /**
   * Move to an element with offset
   *
   * @param element the element to move to
   * @param xOffset the x offset
   * @param yOffset the y offset
   * @return the DoMouse task
   */
  public static DoMouse moveTo(Element element, int xOffset, int yOffset) {
    return new DoMouse(List.of(new MoveToElement(element, xOffset, yOffset)));
  }

  /**
   * Move by offset from the current position
   *
   * @param xOffset the x offset
   * @param yOffset the y offset
   * @return the DoMouse task
   */
  public static DoMouse moveByOffset(int xOffset, int yOffset) {
    return new DoMouse(List.of(new MoveByOffset(xOffset, yOffset)));
  }

  /**
   * Drag and drop from source to target element
   *
   * @param source the source element
   * @param target the target element
   * @return the DoMouse task
   */
  public static DoMouse dragAndDrop(Element source, Element target) {
    return new DoMouse(List.of(
      new ClickAndHold(source),
      new MoveToElement(target),
      new Release()));
  }

  // Fluent API methods

  /**
   * Then pause for the given duration
   *
   * @param duration the duration to pause
   * @return the DoMouse task
   */
  public DoMouse thenPause(Duration duration) {
    this.actions = actions.append(new Pause(duration));
    return this;
  }

  /**
   * Then click at the current mouse position
   *
   * @return the DoMouse task
   */
  public DoMouse thenClick() {
    this.actions = actions.append(new Click());
    return this;
  }

  /**
   * Then click on an element
   *
   * @param element the element to click
   * @return the DoMouse task
   */
  public DoMouse thenClick(Element element) {
    this.actions = actions.append(new Click(element));
    return this;
  }

  /**
   * Then double click at the current mouse position
   *
   * @return the DoMouse task
   */
  public DoMouse thenDoubleClick() {
    this.actions = actions.append(new DoubleClick());
    return this;
  }

  /**
   * Then double click on an element
   *
   * @param element the element to double click
   * @return the DoMouse task
   */
  public DoMouse thenDoubleClick(Element element) {
    this.actions = actions.append(new DoubleClick(element));
    return this;
  }

  /**
   * Then context click (right click) at the current mouse position
   *
   * @return the DoMouse task
   */
  public DoMouse thenContextClick() {
    this.actions = actions.append(new ContextClick());
    return this;
  }

  /**
   * Then context click (right click) on an element
   *
   * @param element the element to context click
   * @return the DoMouse task
   */
  public DoMouse thenContextClick(Element element) {
    this.actions = actions.append(new ContextClick(element));
    return this;
  }

  /**
   * Then click and hold at the current mouse position
   *
   * @return the DoMouse task
   */
  public DoMouse thenClickAndHold() {
    this.actions = actions.append(new ClickAndHold());
    return this;
  }

  /**
   * Then click and hold on an element
   *
   * @param element the element to click and hold
   * @return the DoMouse task
   */
  public DoMouse thenClickAndHold(Element element) {
    this.actions = actions.append(new ClickAndHold(element));
    return this;
  }

  /**
   * Then release the mouse button at the current position
   *
   * @return the DoMouse task
   */
  public DoMouse thenRelease() {
    this.actions = actions.append(new Release());
    return this;
  }

  /**
   * Then release the mouse button on an element
   *
   * @param element the element to release on
   * @return the DoMouse task
   */
  public DoMouse thenRelease(Element element) {
    this.actions = actions.append(new Release(element));
    return this;
  }

  /**
   * Then move to an element
   *
   * @param element the element to move to
   * @return the DoMouse task
   */
  public DoMouse thenMoveTo(Element element) {
    this.actions = actions.append(new MoveToElement(element));
    return this;
  }

  /**
   * Then move to an element with offset
   *
   * @param element the element to move to
   * @param xOffset the x offset
   * @param yOffset the y offset
   * @return the DoMouse task
   */
  public DoMouse thenMoveTo(Element element, int xOffset, int yOffset) {
    this.actions = actions.append(new MoveToElement(element, xOffset, yOffset));
    return this;
  }

  /**
   * Then move by offset from the current position
   *
   * @param xOffset the x offset
   * @param yOffset the y offset
   * @return the DoMouse task
   */
  public DoMouse thenMoveByOffset(int xOffset, int yOffset) {
    this.actions = actions.append(new MoveByOffset(xOffset, yOffset));
    return this;
  }

  private DoMouse(List<MouseAction> actions) {
    this.actions = actions;
  }
}
