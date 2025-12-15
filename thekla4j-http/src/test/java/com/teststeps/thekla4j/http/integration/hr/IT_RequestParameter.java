package com.teststeps.thekla4j.http.integration.hr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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

public class IT_RequestParameter {

  private static String httpBinHost;
  private static String httpsBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
    httpsBinHost = "https://localhost:3002";
  }

  @Test
  public void requestTimesOut() throws ActivityError {

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on(httpBinHost + "/get");

    HttpOptions options = HttpOptions.empty().responseTimeout(1);

    Either<ActivityError, HttpResult> res = tester.attemptsTo(
      Get.from(httpBin).options(options));

    assertThat("response is timeout", res.isLeft(), equalTo(true));
    assertThat("error is timeout", res.getLeft().getMessage(), containsString("HTTP connect timed out"));

  }

  @Test
  public void requestSSLHostFailsWithSSLCheckingOn() {
    // if the test fails make sure the ssl container is running
    // see /resources/ssl/START_SSL_CONTAINER.md for more information

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on(httpsBinHost + "/get");

    HttpOptions options = HttpOptions.empty();

    Either<ActivityError, HttpResult> res = tester.attemptsTo(
      Get.from(httpBin).options(options));

    assertThat("response returned sec error", res.isLeft(), equalTo(true));
    assertThat("error is timeout", res.getLeft().getMessage(),
      containsString(
        "PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target"));
  }

  @Test
  public void requestSSLHostFailsWithSSLCheckingOff() {
    // if the test fails make sure the ssl container is running
    // see /resources/ssl/START_SSL_CONTAINER.md for more information

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on(httpsBinHost + "/get");

    HttpOptions options = HttpOptions.empty().disableSSLCertificateValidation(true);

    Either<ActivityError, HttpResult> res = tester.attemptsTo(
      Get.from(httpBin).options(options));

    assertThat("response is success", res.isRight(), equalTo(true));
    assertThat("status code is 200", res.get().statusCode(), equalTo(200));
  }
}
