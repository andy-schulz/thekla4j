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
import lombok.extern.log4j.Log4j2;

/**
 * Download a file with a given name from the browser's download folder
 */
@Log4j2(topic = "DownloadFile")
@Action("Download file @{fileName}")
public class DownloadFile extends SupplierTask<File> {
  private final BasicInteraction fileNameActivity;
  private String fileName = "";
  private Duration timeout = Duration.ofSeconds(10);
  private Duration pollingInterval = Duration.ofMillis(500);


  /**
   * Download a file with the given name from the browser's download folder
   *
   * @param actor - the actor performing the activity
   * @return - Either an ActivityError or the downloaded File
   */
  @Override
  protected Either<ActivityError, File> performAs(Actor actor) {

    return actor.attemptsTo(fileNameActivity)
        .map(__ -> BrowseTheWeb.as(actor)
            .onSuccess(b -> log.info(() -> "Downloading file '%s' for as long as %s with polling interval %s"
                .formatted(fileName, timeout, pollingInterval)))
            .flatMap(browser -> browser.getDownloadedFile(fileName, timeout, pollingInterval)))
        .flatMap(ActivityError.toEither("Error downloading file: " + fileName));
  }

  /**
   * Create a new DownloadFile activity
   *
   * @param activity - the activity that provides the file name to download
   * @return - a new DownloadFile activity
   */
  public static DownloadFile by(BasicInteraction activity) {
    return new DownloadFile(activity);
  }

  /**
   * Select the name of the file to download
   *
   * @param fileName - the name of the file to download
   * @return - the current DownloadFile activity
   */
  public DownloadFile named(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * Set the maximum time to wait for the file to be downloaded
   *
   * @param timeout - the maximum time to wait for the file to be downloaded
   * @return - the current DownloadFile activity
   */
  public DownloadFile forAsLongAs(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Set the polling interval to check for the file to be downloaded
   *
   * @param pollingInterval - the polling interval to check for the file to be downloaded
   * @return - the current DownloadFile activity
   */
  public DownloadFile every(Duration pollingInterval) {
    this.pollingInterval = pollingInterval;
    return this;
  }

  private DownloadFile(BasicInteraction fileNameActivity) {
    this.fileNameActivity = fileNameActivity;
  }
}
