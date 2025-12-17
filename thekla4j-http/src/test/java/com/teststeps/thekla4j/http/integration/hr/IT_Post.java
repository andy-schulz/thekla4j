package com.teststeps.thekla4j.http.integration.hr;

import static com.teststeps.thekla4j.http.spp.ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
import static com.teststeps.thekla4j.http.spp.HttpHeaderType.CONTENT_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.commons.Cookie;
import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Post;
import io.vavr.collection.List;
import io.vavr.control.Either;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Log4j2(topic = "GetTest")
public class IT_Post {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void sendPostRequestWithCookies() throws ActivityError {
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
        .on(httpBinHost + "/post")
        .withOptions(HttpOptions.empty().cookies(List.of(replacedCookie)));

    tester.attemptsTo(
      Post.to(postRequest).options(opts))
        .peek(r -> assertThat("request used Cookies", r.response(),
          containsString("\"Cookie\": \"JSESSION=DSKDNFKDDFJKNVJVN;JSESSION2=1231234523465235656\"")))
        .getOrElseThrow(Function.identity());
  }

  @Test
  public void sendPostRequestXWwwUrlEncoded() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    String expectedFormParameter = "\"form\": {\n" +
        "    \"specialChars\": \"!@#$%^&*()_+{}|:<>?-=[]\\\\;',./`~\", \n" +
        "    \"username\": \"test.User\"";

    HttpOptions formOptions = HttpOptions.empty()
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
        .formParameter("username", "test.User")
        .formParameter("specialChars", "!@#$%^&*()_+{}|:<>?-=[]\\;',./`~");

    Request postRequest = Request
        .on(httpBinHost + "/post");

    tester.attemptsTo(
      Post.to(postRequest).options(formOptions))
        .peek(r -> assertThat("Content-Type is x-www-form-urlencoded", r.response(),
          containsString("\"Content-Type\": \"application/x-www-form-urlencoded\"")))
        .peek(r -> assertThat("form parameters are sent correctly", r.response(),
          containsString(expectedFormParameter)))
        .peek(System.out::println)
        .getOrElseThrow(Function.identity());
  }


  @Test
  public void sendPostRequestXWwwUrlEncodedWithoutFormParameter() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(JavaNetHttpClient.using(HttpOptions.empty())));

    String expectedErrorMessage = "Error sending HTTP request\n" +
        "Content-Type is set to 'x-www-form-urlencoded' but Form Parameters are missing";

    HttpOptions formOptions = HttpOptions.empty()
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED);

    Request postRequest = Request
        .on(httpBinHost + "/post");

    Either<ActivityError, HttpResult> res = tester.attemptsTo(
      Post.to(postRequest).options(formOptions))
        .peek(System.out::println);

    System.out.println(res);

    assertThat("Post request failed", res.isLeft(), equalTo(true));
    assertThat("Error message is correct", res.getLeft().getMessage(), equalTo(expectedErrorMessage));
  }
}
