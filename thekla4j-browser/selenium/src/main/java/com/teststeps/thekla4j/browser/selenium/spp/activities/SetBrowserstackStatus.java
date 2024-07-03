package com.teststeps.thekla4j.browser.selenium.spp.activities;

import com.teststeps.thekla4j.activityLog.annotations.Action;
import com.teststeps.thekla4j.browser.core.Browser;
import com.teststeps.thekla4j.browser.selenium.config.SeleniumConfig;
import com.teststeps.thekla4j.browser.spp.abilities.BrowseTheWeb;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.activities.Interaction;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.utils.json.JSON;
import io.vavr.Function1;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.teststeps.thekla4j.browser.selenium.functions.ConfigFunctions.loadSeleniumConfig;

/**
 * Set the status of the current Browserstack session to failed
 */
@Log4j2(topic = "Set Browserstack Status")
@AllArgsConstructor
@Action("set the status of the current Browserstack session to failed")
public class SetBrowserstackStatus extends Interaction<Void, String> {

  private Status status;

  private Boolean mapFailure;

  @Override
  protected Either<ActivityError, String> performAs(Actor actor, Void result) {

    Try<Option<SeleniumConfig>> config = loadSeleniumConfig.apply();

    if (config.isFailure() && config.get().isEmpty()) {
      log.debug("Could not load Selenium configuration, cannot set status on Browserstack");
      return Either.left(ActivityError.with(config.getCause()));
    }

    SeleniumConfig conf = config.get().get();

    if (Objects.isNull(conf.bStack()) ||
      Objects.isNull(conf.bStack().userName()) ||
      Objects.isNull(conf.bStack().accessKey()) ||
      conf.bStack().userName().isEmpty() ||
      conf.bStack().accessKey().isEmpty()) {
      log.debug("No username or access key found in selenium config, cannot set status on Browserstack");
      return Either.left(ActivityError.with(new RuntimeException("No username or access key found in selenium config, cannot set status on Browserstack")));

    }

    Either<ActivityError, String> out =
      BrowseTheWeb.as(actor)
      .flatMap(Browser::getSessionId)
      .flatMap(sid -> sendStatus(sid, status, conf.bStack().userName(), conf.bStack().accessKey()))
      .transform(ActivityError.toEither("Could not set status on Browserstack"));

    return mapFailure && out.isLeft() ?
      Either.right("Failed to set status on Browserstack: " + out.getLeft().getMessage()) :
      out;
  }

  private Try<String> sendStatus(String sessionId, Status stat, String username, String accessKey) {

    if (sessionId == null || sessionId.isEmpty()) {
      log.warn("No session id found, cannot set status on Browserstack");
      return Try.failure(new RuntimeException("No session id found, cannot set status on Browserstack"));
    }

    if (username == null || username.isEmpty() || accessKey == null || accessKey.isEmpty()) {
      log.warn("No username or access key found, cannot set status on Browserstack");
      return Try.failure(new RuntimeException("No username or access key found, cannot set status on Browserstack"));
    }

    HttpRequest request = new HttpRequest(HttpMethod.PUT, "/automate/sessions/" + sessionId + ".json");
    request.setHeader("Content-Type", "application/json");
    request.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + accessKey).getBytes()));

    return Try.of(() -> new ByteArrayInputStream(JSON.jStringify(stat).getBytes()))
      .map(is -> request.setContent(() -> is))
      .flatMap(req -> Try.of(() -> HttpClient.Factory.createDefault().createClient(new URL("https://api.browserstack.com")))
        .map(client -> client.execute(req)))
      .flatMap(checkStatus)
      .flatMap(getResponseContent)
      .onFailure(log::warn)
      .onSuccess(text -> log.info("Set test cases status on Browserstack: " + stat.status.toUpperCase() + "\n" + text));
  }

  // Function to check status of response returning response
  private final Function1<HttpResponse, Try<String>> getResponseContent =
    response -> Try.of(() -> new String(response.getContent().get().readAllBytes(), StandardCharsets.UTF_8))
      .onFailure(log::warn);
  private final Function1<HttpResponse, Try<HttpResponse>> checkStatus =
    response -> response.getStatus() < 400 ?
      Try.success(response) :
      Try.failure(new RuntimeException("Failed to set status on Browserstack: " + response.getStatus() + "\n\n" +
        getResponseContent.apply(response).getOrElse("No response content")));

  /**
   * Set the status to failed
   * @return the activity
   */
  public static SetBrowserstackStatus toFailed(String reason) {
    return new SetBrowserstackStatus( Status.failed(reason), true);
  }

  public static SetBrowserstackStatus toPassed() {
    return new SetBrowserstackStatus( Status.passed(), true);
  }

  /**
   * Set the status to failed with a reason
   * @param reason the reason for the failure
   * @return the activity
   */
  public SetBrowserstackStatus withReason(String reason) {
    this.status = status.withReason(reason);
    return this;
  }

  public SetBrowserstackStatus throwOnError() {
    this.mapFailure = false;
    return this;
  }

  @AllArgsConstructor
  @With
  private static class Status {
    public String status;
    public String reason;

    public static Status failed(String reason) {
      return new Status("failed", reason);
    }

    public static Status passed() {
      return new Status("passed", "");
    }
  }
}
