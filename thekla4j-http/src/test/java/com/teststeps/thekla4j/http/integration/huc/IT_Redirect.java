package com.teststeps.thekla4j.http.integration.huc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import io.vavr.control.Either;
import java.net.URISyntaxException;
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
  public void redirectRequest() throws URISyntaxException, ActivityError {

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(
          HttpOptions.empty())));


    Request request = Request.on("/redirect-to")
        .withOptions(baseOptions
            .queryParameter("url", "http://localhost:3001/cookies"));

    Either<ActivityError, HttpResult> res = tester
        .attemptsTo(
          Get.from(request));

    assertThat("either is right", res.isRight());
    assertThat("redirected to", res.get().response(), containsString("cookies"));

  }
}
