package com.teststeps.thekla4j.http.integration;

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
import com.teststeps.thekla4j.http.spp.activities.Post;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import java.io.File;
import java.net.URISyntaxException;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IT_PostFileTest {

  private static String httpBinHost;

  @BeforeAll
  public static void setup() {
    httpBinHost = "http://localhost:3001";
  }

  @Test
  public void postFile() throws URISyntaxException, ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));

    File file = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());

    HttpResult result = tester
        .attemptsTo(
          Post.file(file, "fileForms").to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString("\"files\": {\"fileForms\": \"My Post test file\"}"));
  }

  @Test
  public void postTwoFile() throws URISyntaxException, ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));

    File file1 = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());
    File file2 = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());

    HttpResult result = tester
        .attemptsTo(
          Post.file(file1, "file1")
              .add(FilePart.of(file2, "file2"))
              .to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString(
      "\"files\": {\"file1\": \"My Post test file\",\"file2\": \"My Post test file\"}"));
  }

  @Test
  public void postPart() throws URISyntaxException, ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));


    HttpResult result = tester
        .attemptsTo(
          Post.part(Part.of("variableName", "variableValue")).to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString(
      "\"files\": {},\"form\": {\"variableName\": \"variableValue\"}"));
  }

  @Test
  public void postTwoParts() throws ActivityError {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));


    HttpResult result = tester
        .attemptsTo(
          Post
              .part(Part.of("variableName", "variableValue"))
              .add(Part.of("secondName", "secondValue"))
              .to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString(
      "\"form\": {\"secondName\": \"secondValue\",\"variableName\": \"variableValue\"}"));
  }

  @Test
  public void postPartAndFilePart() throws ActivityError, URISyntaxException {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));

    File file1 = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());

    HttpResult result = tester
        .attemptsTo(
          Post
              .part(Part.of("variableName", "variableValue"))
              .add(FilePart.of(file1, "fileFiledName"))
              .to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString(
      "\"files\": {\"fileFiledName\": \"My Post test file\"},\"form\": {\"variableName\": \"variableValue\"}"));
  }

  @Test
  public void postFilePartAndPart() throws ActivityError, URISyntaxException {
    Actor tester = Actor.named("Tester")
        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));


    Request request = Request.on("/post")
        .withOptions(HttpOptions.empty().baseUrl(httpBinHost));

    File file1 = new File(this.getClass().getClassLoader().getResource("testFile.txt").toURI());

    HttpResult result = tester
        .attemptsTo(
          Post
              .filePart(FilePart.of(file1, "fileFiledName"))
              .add(Part.of("variableName", "variableValue"))
              .to(request))
        .getOrElseThrow(Function.identity());


    assertThat("status code is 200", result.statusCode(), equalTo(200));
    assertThat("response body contains file name", result.response(), containsString(
      "\"files\": {\"fileFiledName\": \"My Post test file\"},\"form\": {\"variableName\": \"variableValue\"}"));
  }
}
