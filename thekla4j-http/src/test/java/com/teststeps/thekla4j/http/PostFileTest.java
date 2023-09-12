package com.teststeps.thekla4j.http;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Post;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.function.Function;

public class PostFileTest {

  @Test
  public void postFile() throws URISyntaxException, ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl("http://localhost:8888")
            .formParameter("name", "tester")
            .formParameter("age", "99"));

    File file = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());

    tester
        .attemptsTo(
            Post.file(file, "fileForms").to(request))
        .peek(System.out::println)
        .getOrElseThrow(Function.identity());
  }
}
