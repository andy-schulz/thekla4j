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

  private final Try<Actions> selActionDriver;

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActionDriver> keyDown(Key key) {
    return selActionDriver.map(a -> a.keyDown(Keys.valueOf(key.name())))
        .mapTry(__ -> this);
//    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActionDriver> keyUp(Key key) {
    return selActionDriver.map(a -> a.keyUp(Keys.valueOf(key.name())))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActionDriver> keyPress(Key key) {
//    this.selActionDriver = selActionDriver.map(a -> {
//      a.sendKeys(Keys.valueOf(key.name()));
//      return a;
//    });

    return selActionDriver.map(a -> a.sendKeys(Keys.valueOf(key.name())))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActionDriver> keyPress(CharSequence keys) {
    return selActionDriver.map(a -> a.sendKeys(keys))
        .map(__ -> this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Try<KeyActionDriver> pause(Duration duration) {
    return selActionDriver.map(a -> a.pause(duration))
        .map(__ -> this);
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
