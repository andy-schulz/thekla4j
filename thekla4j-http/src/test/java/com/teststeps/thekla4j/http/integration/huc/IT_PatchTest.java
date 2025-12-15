package com.teststeps.thekla4j.http.integration.huc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Patch;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_PatchTest {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void patchRequest() {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));

    Request request = Request.on("/patch")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost).body("test body"));

    Either<ActivityError, HttpResult> result = tester
        .attemptsTo(
          Patch.to(request));

    assertThat("result is left", result.isLeft(), equalTo(true));
    assertThat("result has error message", result.getLeft().getMessage(),
      containsString("PATCH method is not implemented in HcHttpClient. Please use the JavaNetHttpClient implementation."));

  }
}
