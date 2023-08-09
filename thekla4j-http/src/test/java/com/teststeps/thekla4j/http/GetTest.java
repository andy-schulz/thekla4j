package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.core.Cookie;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.ContentType;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import com.teststeps.thekla4j.http.spp.activities.Post;
import io.vavr.collection.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Log4j2(topic = "GetTest")
public class GetTest {

  @Test
  public void sendGetRequest() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on("http://localhost:8888/response-headers?Set-Cookie=test=testValue;Path=/;Domain=test-step.de/");

    tester.attemptsTo(
            Get.from(httpBin))
        .peek(r -> assertThat("number of cookies:", r.cookies().size(), equalTo(1)))
        .peek(r -> assertThat("cookie name", r.cookies().get(0).name, equalTo("test")))
        .peek(r -> assertThat("cookie value", r.cookies().get(0).value, equalTo("testValue")))
        .peek(r -> assertThat("cookie path", r.cookies().get(0).path, equalTo("/")))
        .peek(r -> assertThat("cookie domain", r.cookies().get(0).domain, equalTo("test-step.de/")))
        .getOrElseThrow(Function.identity());
  }

  @Test
  public void sendGetRequestWithCookies() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));

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
        .on("http://localhost:8888/post")
        .withOptions(HttpOptions.empty().cookies(List.of(replacedCookie)));

    tester.attemptsTo(
            Post.to(postRequest).options(opts))
        .peek(r -> assertThat("request used Cookies", r.response(),
            containsString("\"Cookie\": \"JSESSION=DSKDNFKDDFJKNVJVN;JSESSION2=1231234523465235656\"")
        ))
        .getOrElseThrow(Function.identity());
  }

  @Test
  public void setContentType() {

    assertThat(ContentType.APPLICATION_X_WWW_FORM_URLENCODED.asString, equalTo("application/x-www-form-urlencoded"));
  }
}
