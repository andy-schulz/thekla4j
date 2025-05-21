package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.browser.spp.activities.keyActions.Key;
import com.teststeps.thekla4j.browser.spp.activities.keyActions.KeyActionDriver;
import io.vavr.control.Try;
import java.time.Duration;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

/**
 * A key action for Selenium
 */
class SeleniumKeyActionDriver implements KeyActionDriver {

  private Try<Actions> selActionDriver;

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActionDriver keyDown(Key key) {
    this.selActionDriver = selActionDriver.map(a -> a.keyDown(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActionDriver keyUp(Key key) {
    this.selActionDriver = selActionDriver.map(a -> a.keyUp(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActionDriver keyPress(Key key) {
    this.selActionDriver = selActionDriver.map(a -> a.sendKeys(Keys.valueOf(key.name())));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActionDriver keyPress(CharSequence keys) {
    this.selActionDriver = selActionDriver.map(a -> a.sendKeys(keys));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyActionDriver pause(Duration duration) {
    this.selActionDriver = selActionDriver.map(a -> a.pause(duration));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<Void> perform() {
    return selActionDriver.map(a -> {
      a.perform();
      return null;
    });
  }

  SeleniumKeyActionDriver(Actions actions) {
    this.selActionDriver = Try.success(actions);
  }
}
