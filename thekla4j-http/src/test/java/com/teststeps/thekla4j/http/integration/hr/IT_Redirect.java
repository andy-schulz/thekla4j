package com.teststeps.thekla4j.http.integration.hr;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_Redirect {

  private static String httpBinHost;
  private static HttpOptions baseOptions;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
    baseOptions = HttpOptions.empty().baseUrl(httpBinHost);
  }

  @Test
  public void redirectRequest() {

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/redirect-to")
        .called("Redirect Request")
        .withOptions(baseOptions
            .queryParameter("url", "http://localhost:3001/status/418"));

    Either<ActivityError, HttpResult> res = tester
        .attemptsTo(
          Get.from(request));

    assertThat("either is right", res.isRight());
    assertThat("status code is 418", res.get().statusCode() == 418);
    assertThat("redirected to", res.get().response(), containsString("teapot"));

  }

  @Test
  public void dontRedirectRequest() {

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request request = Request.on("/redirect-to")
        .called("Redirect Request")
        .withOptions(baseOptions
            .queryParameter("url", "http://localhost:3001/status/418")
            .followRedirects(false));

    Either<ActivityError, HttpResult> res = tester
        .attemptsTo(
          Get.from(request));

    assertThat("either is right", res.isRight());
    assertThat("status code is 302", res.get().statusCode() == 302);
  }
}
