package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.ContentType;
import com.teststeps.thekla4j.http.spp.HttpHeaderType;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Post;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RedirectTests {

  @Test
  public void setRedirectInOptions() {

    HttpOptions options = HttpOptions.empty()
        .followRedirects(false);

    assertThat(options.followRedirects, equalTo(false));

  }

  @Test
  public void defaultRedirectIsTrue() {

    HttpOptions options = HttpOptions.empty();

    assertThat(options.followRedirects, equalTo(true));

  }

  @Test
  public void mergeTrueOnFalseShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsTrue.mergeOnTopOf(redirectOptsFalse);

    assertThat(mergeTrueOnTopOfFalse.followRedirects, equalTo(false));


  }

  @Test
  public void mergeFalseOnTrueShouldStayFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty()
        .followRedirects(false);

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions mergeTrueOnTopOfFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsTrue);

    assertThat(mergeTrueOnTopOfFalse.followRedirects, equalTo(false));
  }

  @Test
  public void mergeTrueOnTrueShouldBeTrue() {

    HttpOptions redirectOptsTrue = HttpOptions.empty();

    HttpOptions redirectOptsTrue1 = HttpOptions.empty();

    HttpOptions mergeTrueOnTrue = redirectOptsTrue.mergeOnTopOf(redirectOptsTrue1);

    assertThat(mergeTrueOnTrue.followRedirects, equalTo(true));
  }

  @Test
  public void mergeFalseOnFalseShouldBeFalse() {

    HttpOptions redirectOptsFalse = HttpOptions.empty().followRedirects(false);

    HttpOptions redirectOptsFalse1 = HttpOptions.empty().followRedirects(false);

    HttpOptions mergeFalseOnFalse = redirectOptsFalse.mergeOnTopOf(redirectOptsFalse1);

    assertThat(mergeFalseOnFalse.followRedirects, equalTo(false));
  }


  @Test
  public void redirectPostRequest() throws URISyntaxException, ActivityError {

    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(
            HttpOptions.empty())));


    Request request = Request.on("/redirect-to")
        .withOptions(HttpOptions.empty().baseUrl("http://localhost:8888")
            .header(HttpHeaderType.CONTENT_TYPE, ContentType.APPLICATION_X_WWW_FORM_URLENCODED.asString)
            .formParameter("name", "tester")
            .formParameter("age", "99"));

    tester
        .attemptsTo(
            Post.to(request))
        .peek(System.out::println)
        .getOrElseThrow(Function.identity());
  }
}
