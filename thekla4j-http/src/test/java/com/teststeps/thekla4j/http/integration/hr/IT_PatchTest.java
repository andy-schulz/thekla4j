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
import com.teststeps.thekla4j.http.spp.activities.Patch;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_PatchTest {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void patchRequest() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Request request = Request.on("/patch")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost).body("test body"));

    HttpResult result = tester
        .attemptsTo(
          Patch.to(request))
        .getOrElseThrow(Function.identity());

    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains data", result.response(), containsString("\"data\": \"test body\""));
  }
}
