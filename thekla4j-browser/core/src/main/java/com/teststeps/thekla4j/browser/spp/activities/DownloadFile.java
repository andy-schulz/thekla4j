package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.activities.SupplierTask;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;

import java.io.File;
import java.time.Duration;

@Action("Download file @{fileName}")
public class DownloadFile extends SupplierTask<File> {
  private final BasicInteraction fileNameActivity;
  private String fileName = "";
  private Duration timeout = Duration.ofSeconds(10);
  private Duration pollingInterval = Duration.ofMillis(500);


  @Override
  protected Either<ActivityError, File> performAs(Actor actor) {

    return actor.attemptsTo(fileNameActivity)
      .map(__ -> BrowseTheWeb.as(actor)
          .flatMap(browser -> browser.getDownloadedFile(fileName, timeout, pollingInterval)))
      .peekLeft(System.out::println)
      .flatMap(ActivityError.toEither("Error downloading file: " + fileName));
  }

  public static DownloadFile by(BasicInteraction activity) {
    return new DownloadFile(activity);
  }

  public DownloadFile named(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public DownloadFile forAsLongAs(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  public DownloadFile every(Duration pollingInterval) {
    this.pollingInterval = pollingInterval;
    return this;
  }

  private DownloadFile(BasicInteraction fileNameActivity) {
    this.fileNameActivity = fileNameActivity;
  }
}
