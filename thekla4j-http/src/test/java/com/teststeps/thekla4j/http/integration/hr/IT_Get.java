package com.teststeps.thekla4j.http.integration.hr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_Get {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void sendJavaNetGetRequest() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on(httpBinHost + "/response-headers?Set-Cookie=test=testValue;Path=/;Domain=test-step.de/");

    tester.attemptsTo(
      Get.from(httpBin))
        .peek(r -> assertThat("number of cookies:", r.cookies().size(), equalTo(1)))
        .peek(r -> assertThat("cookie name", r.cookies().get(0).name(), equalTo("test")))
        .peek(r -> assertThat("cookie value", r.cookies().get(0).value(), equalTo("testValue")))
        .peek(r -> assertThat("cookie path", r.cookies().get(0).path(), equalTo("/")))
        .peek(r -> assertThat("cookie domain", r.cookies().get(0).domain(), equalTo("test-step.de/")))
        .getOrElseThrow(Function.identity());
  }

  @Test
  public void checkHeadersInGetResponse() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on(httpBinHost + "/response-headers?Test-Header=HeaderValue");

    String EXPECTED_HEADER_NAME = "test-header";

    tester.attemptsTo(
      Get.from(httpBin))
        .peek(r -> assertThat("test header exists:", r.headers().get(EXPECTED_HEADER_NAME).isDefined(), equalTo(true)))
        .peek(r -> assertThat("number of header values", r.headers().get(EXPECTED_HEADER_NAME).get().size(), equalTo(1)))
        .peek(r -> assertThat("header value", r.headers().get(EXPECTED_HEADER_NAME).get().head(), equalTo("HeaderValue")))
        .getOrElseThrow(Function.identity());
  }
}
