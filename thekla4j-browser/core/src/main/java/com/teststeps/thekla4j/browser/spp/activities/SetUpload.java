package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

/**
 * Set the upload files to the file input
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Action("set upload")
public class SetUpload {

  /**
   * Set the upload files to the file input
   *
   * @param absolutFilePath - the absolute path of the file to upload
   * @return - the task to set the upload files to the file input
   */
  public static SetUploadFiles file(Path absolutFilePath) {
    return new SetUploadFiles(List.of(absolutFilePath));
  }

  /**
   * Set the upload files to the file input
   *
   * @param absolutFilePaths - the absolute paths of the files to upload
   * @return - the task to set the upload files to the file input
   */
  public static SetUploadFiles files(Path... absolutFilePaths) {
    return new SetUploadFiles(List.of(absolutFilePaths));
  }


  /**
   * Set the upload files to the file input
   */
  @Action("set upload files")
  public static class SetUploadFiles extends BasicInteraction {

    private final List<Path> absolutFilePaths;
    private Element targetFileUploadInput;


    @Override
    protected Either<ActivityError, Void> performAs(Actor actor) {

      return BrowseTheWeb.as(actor)
        .flatMap(browser -> browser.setUploadFiles(absolutFilePaths, targetFileUploadInput))
        .transform(ActivityError.toEither("Error while trying to set the files %s to upload to the file input: %s"
          .formatted(absolutFilePaths, targetFileUploadInput)))
        .map(__ -> null);
    }

    /**
     * Set the upload files to the file input
     *
     * @param absolutFilePaths - the absolute paths of the files to upload
     */
    public SetUploadFiles(List<Path> absolutFilePaths) {
      this.absolutFilePaths = absolutFilePaths;
    }

    /**
     * Set the upload files to the file input
     *
     * @param targetFileUploadInput - the file input element to set the files to
     * @return - the task to set the upload files to the file input
     */
    public SetUploadFiles to(Element targetFileUploadInput) {
      this.targetFileUploadInput = targetFileUploadInput;
      return this;
    }
  }
}
