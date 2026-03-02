package com.teststeps.thekla4j.browser.playwright.integration;

import static com.teststeps.thekla4j.browser.playwright.Constants.CANVAS;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Move;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.playwright.BrowserSetup;
import com.teststeps.thekla4j.browser.playwright.helper.TestFunctions;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Draw;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import java.time.Duration;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IT_DrawTest {

  private Actor actor;

  Element header = Element.found(By.css(".headerElement"));

  Shape letterT = Shape.startingAt(StartPoint.on(5, 5))
      .moveTo(Move.right(30))
      .moveTo(Move.left(15))
      .moveTo(Move.down(40));

  Shape letterE = Shape.startingAt(StartPoint.on(40, 5))
      .moveTo(Move.right(30))
      .moveTo(Move.left(30))
      .moveTo(Move.down(20))
      .moveTo(Move.right(30))
      .moveTo(Move.left(30))
      .moveTo(Move.down(20))
      .moveTo(Move.right(30));

  Shape letterS = Shape.startingAt(StartPoint.on(80, 5))
      .moveTo(Move.right(30))
      .moveTo(Move.left(30))
      .moveTo(Move.down(20))
      .moveTo(Move.right(30))
      .moveTo(Move.down(20))
      .moveTo(Move.left(30));

  Shape letterS2 = Shape.startingAt(StartPoint.on(110, 15))
      .moveTo(Move.up(5))
      .moveTo(Move.leftUp(5))
      .moveTo(Move.left(20))
      .moveTo(Move.leftDown(5))
      .moveTo(Move.down(10))
      .moveTo(Move.rightDown(5))
      .moveTo(Move.right(20))
      .moveTo(Move.rightDown(5))
      .moveTo(Move.down(10))
      .moveTo(Move.leftDown(5))
      .moveTo(Move.left(20))
      .moveTo(Move.leftUp(5))
      .moveTo(Move.up(5));

  Shape letterT2 = Shape.startingAt(StartPoint.on(120, 5))
      .moveTo(Move.right(30))
      .moveTo(Move.left(15))
      .moveTo(Move.down(40));

  @BeforeEach
  public void initActor() {
    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(BrowserSetup.chromiumBrowser()));
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }

  @Test
  public void writingLetterOnCanvas() throws ActivityError {

    Element canvas = Element.found(By.css("#canvas"))
        .withName("Canvas");

    actor.attemptsTo(

      Navigate.to(CANVAS),

      Draw.shape(letterT).on(canvas),

      See.ifThe(Text.of(header))
          .is(Expected.to.pass(TestFunctions.coordinatesWithinOnePx.apply(20, 5, "Drawing on Canvas with Mouse. X: {X} Y: {Y}")))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void drawingMultipleShapes() throws ActivityError {

    Element canvas = Element.found(By.css("#canvas"))
        .withName("Canvas");

    actor.attemptsTo(

      Navigate.to(CANVAS),

      Draw.shape(letterT).on(canvas),
      Draw.shape(letterE).on(canvas),
      Draw.shape(letterS2).on(canvas),
      Draw.shape(letterT2).on(canvas),

      See.ifThe(Text.of(header))
          .is(Expected.to.pass(TestFunctions.coordinatesWithinOnePx.apply(135, 5, "Drawing on Canvas with Mouse. X: {X} Y: {Y}")))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }

  @Test
  public void drawingMultipleShapesAtOnce() throws ActivityError {

    Element canvas = Element.found(By.css("#canvas"))
        .withName("Canvas");

    actor.attemptsTo(

      Navigate.to(CANVAS),

      Draw.shapes(letterT, letterE, letterS, letterT2)
          .on(canvas),

      See.ifThe(Text.of(header))
          .is(Expected.to.pass(TestFunctions.coordinatesWithinOnePx.apply(135, 5, "Drawing on Canvas with Mouse. X: {X} Y: {Y}")))
          .forAsLongAs(Duration.ofSeconds(3)))

        .getOrElseThrow(Function.identity());
  }
}
