package com.teststeps.thekla4j.browser.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Workflow;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.BasicInteraction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
@Workflow("set upload")
public class SetUpload {

  public static SetUploadFiles file(Path absolutFilePath) {
    return new SetUploadFiles(List.of(absolutFilePath));
  }

  public static SetUploadFiles files(Path... absolutFilePaths) {
    return new SetUploadFiles(List.of(absolutFilePaths));
  }


  @Workflow("set upload files")
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

    public SetUploadFiles(List<Path> absolutFilePaths) {
      this.absolutFilePaths = absolutFilePaths;
    }

    public SetUploadFiles to(Element targetFileUploadInput) {
      this.targetFileUploadInput = targetFileUploadInput;
      return this;
    }
  }


}
