package com.teststeps.thekla4j.browser.selenium;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.drawing.Move;
import com.teststeps.thekla4j.browser.core.drawing.Shape;
import com.teststeps.thekla4j.browser.core.drawing.StartPoint;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Draw;
import com.teststeps.thekla4j.browser.spp.activities.Navigate;
import com.teststeps.thekla4j.browser.spp.activities.Text;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

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

  Shape letterT2 = Shape.startingAt(StartPoint.on(120, 5))
    .moveTo(Move.right(30))
    .moveTo(Move.left(15))
    .moveTo(Move.down(40));

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
    actor.cleansStage();
  }


  @Test
  public void writingLetterOnCanvas() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element canvas = Element.found(By.css("#canvas"))
      .withName("Canvas");

    String url = "http://localhost:3000/canvas";


    actor.attemptsTo(

        Navigate.to(url),

        Draw.shape(letterT).on(canvas),

        See.ifThe(Text.of(header))
          .is(Expected.to.equal("Drawing on Canvas with Mouse. X: 20 Y: 5")))


      .getOrElseThrow(Function.identity());
  }

  @Test
  public void drawingMultipleShapes() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element canvas = Element.found(By.css("#canvas"))
      .withName("Canvas");

    String url = "http://localhost:3000/canvas";


    actor.attemptsTo(

        Navigate.to(url),

        Draw.shape(letterT).on(canvas),
        Draw.shape(letterE).on(canvas),
        Draw.shape(letterS).on(canvas),
        Draw.shape(letterT2).on(canvas),

        See.ifThe(Text.of(header))
          .is(Expected.to.equal("Drawing on Canvas with Mouse. X: 135 Y: 5")))


      .getOrElseThrow(Function.identity());
  }

  @Test
  public void drawingMultipleShapesAtOnce() throws ActivityError {

    actor = Actor.named("Test Actor")
      .whoCan(BrowseTheWeb.with(ChromeBrowser.with()));

    Element canvas = Element.found(By.css("#canvas"))
      .withName("Canvas");

    String url = "http://localhost:3000/canvas";


    actor.attemptsTo(

      Navigate.to(url),

      Draw.shapes(letterT, letterE, letterS, letterT2)
        .on(canvas),

      See.ifThe(Text.of(header))
        .is(Expected.to.equal("Drawing on Canvas with Mouse. X: 135 Y: 5")))

      .getOrElseThrow(Function.identity());
  }
}
