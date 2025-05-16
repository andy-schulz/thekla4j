package com.teststeps.thekla4j.browser.spp.activities.keyActions;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.activityLog.annotations.Called;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;

/**
 * Perform key actions
 */
@Action("perform key actions @{actions}")
public class DoKey extends BasicInteraction {


  @Called(name = "actions")
  private List<KeyAction> actions;

  @Override
  protected Either<ActivityError, Void> performAs(Actor actor) {
    return BrowseTheWeb.as(actor)
      .flatMap(Browser::executeKeyActions)
      .peek(ka -> actions.forEach(a -> a.performKeyAction(ka)))
      .flatMap(KeyActions::perform)
      .transform(ActivityError.toEither("Error while executing key actions"))
      .map(__ -> null);
  }

  /**
   * Presses the given keys
   *
   * @param keys the keys to press
   * @return the DoKeys task
   */
  public static DoKey press(Key... keys) {
    return new DoKey(List.of(keys).map(Press::new));
  }

  /**
   * Presses the given key sequence
   *
   * @param sequence the key sequence to press
   * @return the DoKeys task
   */
  public static DoKey press(CharSequence sequence) {
    return new DoKey(List.of(new PressSequence(sequence)));
  }

  /**
   * Presses and holds the given keys
   *
   * @param keys the keys to press and hold
   * @return the DoKeys task
   */
  public static DoKey pressAndHold(Key... keys) {

    return new DoKey(List.of(keys).map(Down::new));
  }

  /**
   * Presses and holds the given keys
   *
   * @param keys the keys to press and hold
   * @return the DoKeys task
   */
  public static DoKey release(Key... keys) {

    return new DoKey(List.of(keys).map(Up::new));
  }

  /**
   * Presses the given keys (and holds them down)
   *
   * @param keys the keys to press
   * @return the DoKeys task
   */
  public DoKey thenPressAndHold(Key... keys) {
    this.actions = actions.appendAll(List.of(keys).map(Down::new));
    return this;
  }

  /**
   * Presses the given keys (and releases them)
   *
   * @param keys the keys to press
   * @return the DoKeys task
   */
  public DoKey thenPress(Key... keys) {
    this.actions = actions.appendAll(List.of(keys).map(Press::new));
    return this;
  }

  /**
   * Releases the given keys
   *
   * @param keys the keys to release
   * @return the DoKeys task
   */
  public DoKey thenRelease(Key... keys) {
    this.actions = actions.appendAll(List.of(keys).map(Up::new));
    return this;
  }

  private DoKey(List<KeyAction> actions) {
    this.actions = actions;
  }


}
