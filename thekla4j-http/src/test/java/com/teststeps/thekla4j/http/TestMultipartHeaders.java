package com.teststeps.thekla4j.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import com.sun.net.httpserver.HttpServer;
import com.teststeps.thekla4j.http.httpConn.HcHttpRequest;
import com.teststeps.thekla4j.http.httpRequest.JavaNetHttpRequest;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;
import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A Content-Type set through the http options must not overwrite the multipart content type
 * (including its boundary) that postFile generates for the request.
 */
public class TestMultipartHeaders {

  private HttpServer server;
  private final AtomicReference<String> receivedContentType = new AtomicReference<>();
  private final AtomicReference<String> receivedAuthorization = new AtomicReference<>();

  @BeforeEach
  public void startServer() throws Exception {
    server = HttpServer.create(new InetSocketAddress(0), 0);
    server.createContext("/upload", exchange -> {
      receivedContentType.set(exchange.getRequestHeaders().getFirst("Content-Type"));
      receivedAuthorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
      try (InputStream body = exchange.getRequestBody()) {
        body.readAllBytes();
      }
      exchange.sendResponseHeaders(200, 0);
      exchange.close();
    });
    server.start();
  }

  @AfterEach
  public void stopServer() {
    server.stop(0);
  }

  private HttpOptions optionsWithJsonContentType() {
    return HttpOptions.empty()
        .baseUrl("http://localhost:" + server.getAddress().getPort())
        .header("Content-Type", "application/json");
  }

  private File fileToUpload() throws Exception {
    Path path = Files.createTempFile("thekla4j-upload", ".txt");
    Files.writeString(path, "content of the uploaded file");
    path.toFile().deleteOnExit();
    return path.toFile();
  }

  @Test
  public void javaNetClientKeepsTheMultipartContentTypeWhenAContentTypeOptionIsSet() throws Exception {

    JavaNetHttpRequest.on("/upload")
        .doing("upload a file")
        .using(optionsWithJsonContentType())
        .executeWith(HttpClient.newHttpClient())
        .postFile(List.of(FilePart.of(fileToUpload(), "file")), List.empty())
        .get();

    assertThat("the multipart content type is sent", receivedContentType.get(), startsWith("multipart/form-data"));
    assertThat("the multipart boundary is sent", receivedContentType.get(), containsString("boundary="));
  }

  @Test
  public void javaNetClientStillSendsTheRemainingOptionHeaders() throws Exception {

    JavaNetHttpRequest.on("/upload")
        .doing("upload a file")
        .using(optionsWithJsonContentType().header("Authorization", "Bearer token"))
        .executeWith(HttpClient.newHttpClient())
        .postFile(List.of(FilePart.of(fileToUpload(), "file")), List.empty())
        .get();

    assertThat("the multipart content type is sent", receivedContentType.get(), startsWith("multipart/form-data"));
    assertThat("other option headers are still sent", receivedAuthorization.get(), equalTo("Bearer token"));
  }

  @Test
  public void hcClientKeepsTheMultipartContentTypeWhenAContentTypeOptionIsSet() throws Exception {

    HcHttpRequest.on("/upload")
        .doing("upload a file")
        .using(optionsWithJsonContentType())
        .postFile(List.of(FilePart.of(fileToUpload(), "file")), List.<Part>empty())
        .get();

    assertThat("the multipart content type is sent", receivedContentType.get(), startsWith("multipart/form-data"));
    assertThat("the multipart boundary is sent", receivedContentType.get(), containsString("boundary="));
  }
}
