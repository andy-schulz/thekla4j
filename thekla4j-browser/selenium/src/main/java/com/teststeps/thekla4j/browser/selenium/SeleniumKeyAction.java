package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import io.vavr.control.Try;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * A key action for Selenium
 */
class SeleniumKeyAction implements KeyActions {

  private Try<Actions> selAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActions keyDown(Key key) {
    this.selAction = selAction.map(a -> a.keyDown(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActions keyUp(Key key) {
    this.selAction = selAction.map(a -> a.keyUp(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActions keyPress(Key key) {
    this.selAction = selAction.map(a -> a.sendKeys(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActions keyPress(CharSequence keys) {
    this.selAction = selAction.map(a -> a.sendKeys(keys));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> perform() {
    return selAction.map(a -> {
      a.perform();
      return null;
    });
  }

  SeleniumKeyAction(Actions actions) {
    this.selAction = Try.success(actions);
  }
}
