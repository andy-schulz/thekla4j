package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActions;
import io.vavr.control.Try;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

class SeleniumKeyAction implements KeyActions {

  private Try<Actions> selAction;

  @Override
  public KeyActions keyDown(Key key) {
    this.selAction = selAction.map(a -> a.keyDown(Keys.valueOf(key.name())));
    return this;
  }

  @Override
  public KeyActions keyUp(Key key) {
    this.selAction = selAction.map(a -> a.keyUp(Keys.valueOf(key.name())));
    return this;
  }

  @Override
  public KeyActions keyPress(Key key) {
    this.selAction = selAction.map(a -> a.sendKeys(Keys.valueOf(key.name())));
    return this;
  }

  @Override
  public Try<Void> perform() {
    return selAction.map(a -> {
      a.perform();
      return null;
    });
  }

  public SeleniumKeyAction(Actions actions) {
    this.selAction = Try.success(actions);
  }
}
