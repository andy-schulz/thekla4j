package com.teststeps.thekla4j.browser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.core.Element;
import com.teststeps.thekla4j.browser.core.locator.By;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.browser.spp.activities.Click;
import com.teststeps.thekla4j.browser.spp.activities.DownloadFile;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import io.vavr.control.Try;
import java.io.File;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestDownload {

  private Actor actor;
  private final Element element = Element.found(By.css("testElement"));


  @Mock
  Browser chromeMock;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    actor = Actor.named("Test Actor").whoCan(BrowseTheWeb.with(chromeMock));

  }

  @AfterEach
  public void tearDown() throws InterruptedException {
    // actor.cleansStage();
    Thread.sleep(10);
  }

  @Test
  public void downloadFile() throws ActivityError {

    File file = new File("", "");

    when(chromeMock.clickOn(element)).thenReturn(Try.of(() -> null));

    when(chromeMock.getDownloadedFile(any(String.class),
      any(Duration.class),
      any(Duration.class)))
        .thenReturn(Try.of(() -> file));

    File resultFile = DownloadFile.by(Click.on(element)).runAs(Performer.of(actor));

    verify(chromeMock, times(1)).clickOn(element);
    verify(chromeMock, times(1))
        .getDownloadedFile(any(String.class),
          any(Duration.class),
          any(Duration.class));

    assertThat("returned file is correct", file.equals(resultFile), equalTo(true));

  }
}
