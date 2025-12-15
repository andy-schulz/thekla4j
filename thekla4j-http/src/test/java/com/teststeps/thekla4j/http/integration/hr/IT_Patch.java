package com.teststeps.thekla4j.http.integration.hr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Patch;
import io.vavr.collection.List;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Log4j2(topic = "GetTest")
public class IT_Patch {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void sendPatchRequestWithCookies() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    Cookie replacedCookie = Cookie.empty()
        .withName("JSESSION")
        .withValue("w45324523452345");

    Cookie cookie = Cookie.empty()

        .withName("JSESSION")
        .withValue("DSKDNFKDDFJKNVJVN");

    Cookie cookie2 = Cookie.empty()
        .withName("JSESSION2")
        .withValue("1231234523465235656");

    HttpOptions opts = HttpOptions.empty()
        .cookies(List.of(cookie, cookie2));

    Request postRequest = Request
        .on(httpBinHost + "/patch")
        .withOptions(HttpOptions.empty().cookies(List.of(replacedCookie)));

    tester.attemptsTo(
      Patch.to(postRequest).options(opts))
        .peek(r -> assertThat("request used Cookies", r.response(),
          containsString("\"Cookie\": \"JSESSION=DSKDNFKDDFJKNVJVN;JSESSION2=1231234523465235656\"")))
        .getOrElseThrow(Function.identity());
  }
}
