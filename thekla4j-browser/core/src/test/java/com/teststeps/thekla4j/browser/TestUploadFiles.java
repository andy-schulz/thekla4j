package com.teststeps.thekla4j.browser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.SetUpload;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.nio.file.Path;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestUploadFiles {
  private Actor actor;

  @Mock
  Browser chromeMock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    Thread.sleep(10);
  }

  @Test
  public void testNavigateBack() throws ActivityError {

    Path testPath = Path.of("src/test/resources/test.txt");
    Element element = Element.found(By.css("testElement"));

    when(chromeMock.setUploadFiles(List.of(testPath), element))
        .thenReturn(Try.of(() -> null));

    actor = Actor.named("Test Actor")
        .whoCan(BrowseTheWeb.with(chromeMock));

    actor.attemptsTo(

      SetUpload.file(testPath).to(element))

        .getOrElseThrow(Function.identity());

    verify(chromeMock, times(1)).setUploadFiles(List.of(testPath), element);

  }

}
