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
import com.teststeps.thekla4j.http.spp.activities.Put;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class IT_PutTest {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void putRequest() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));

    Request request = Request.on("/put")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost).body("test body"));

    HttpResult result = tester
        .attemptsTo(
          Put.to(request))
        .getOrElseThrow(Function.identity());

    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains data", result.response(), containsString("\"data\": \"test body\""));
  }
}
