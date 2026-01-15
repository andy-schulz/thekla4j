package com.teststeps.thekla4j.http.httpRequest;

import static com.teststeps.thekla4j.http.core.functions.UrlFunctions.createBody;
import static com.teststeps.thekla4j.http.core.functions.UrlFunctions.getUrl;

import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "JavaNetHttpRequest")
public class JavaNetHttpRequest implements com.teststeps.thekla4j.http.core.HttpRequest {
  private final String resource;
  private final String description;
  private final HttpOptions httpOptions;
  private final HttpClient client;
  private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();


  public static Builder on(String resource) {
    return Builder.on(resource);
  }

  private Try<HttpResult> send(HttpRequest.Builder requestBuilder) {

    String url = getUrl(httpOptions.baseUrl, httpOptions.port, resource, httpOptions.queryParameters, httpOptions.pathParameters);
    log.debug("Using url: {}", url);
    log.debug(() -> "with options: " + httpOptions.toString(1));

    URI uri = URI.create(url);
    requestBuilder.uri(uri);

    requestBuilder.timeout(httpOptions.getResponseTimeout());

    httpOptions.headers.forEach(requestBuilder::setHeader);

    HttpRequest req = requestBuilder.build();

    return Try.of(() -> client.send(req, HttpResponse.BodyHandlers.ofString()))
        .onFailure(x -> log.error(x.getMessage()))
        .map(JavaNetHttpResult::new);
  }

  @Override
  public Try<HttpResult> get() {
    log.info(() -> "Starting GET Request: " + (Objects.isNull(this.description) || this.description.isBlank() ? "'unnamed' " + this.resource : "'" +
        this.description + "' " + this.resource));
    return send(requestBuilder.GET());
  }

  @Override
  public Try<HttpResult> post() {
    log.info(() -> "Starting POST Request: " + (Objects.isNull(this.description) || this.description.isBlank() ? "'unnamed' " + this.resource : "'" +
        this.description + "' " + this.resource));
    return createBody.apply(httpOptions).flatMap(bodyContent -> send(requestBuilder.POST(bodyPublisher(bodyContent))));
  }

  @Override
  public Try<HttpResult> patch() {
    log.info(() -> "Starting PATCH Request: " + (Objects.isNull(this.description) || this.description.isBlank() ? "'unnamed' " + this.resource : "'" +
        this.description + "' " + this.resource));
    return send(requestBuilder.method("PATCH", bodyPublisher(httpOptions.body)));
  }

  @Override
  public Try<HttpResult> put() {
    log.info(() -> "Starting PUT Request: " + (Objects.isNull(this.description) || this.description.isBlank() ? "'unnamed' " + this.resource : "'" +
        this.description + "' " + this.resource));
    return send(requestBuilder.PUT(bodyPublisher(httpOptions.body)));
  }

  @Override
  public Try<HttpResult> delete() {
    log.info(() -> "Starting DELETE Request: " + (Objects.isNull(this.description) || this.description.isBlank() ? "'unnamed' " + this.resource :
        "'" +
            this.description + "' " + this.resource));
    if (httpOptions.body != null) {
      return send(requestBuilder.method("DELETE", bodyPublisher(httpOptions.body)));
    }
    return send(requestBuilder.DELETE());
  }

  @Override
  public Try<HttpResult> postFile(io.vavr.collection.List<FilePart> fileParts, List<Part> parts) {
    MultipartBodyPublisher publisher = new MultipartBodyPublisher();

    fileParts.forEach(filePart -> publisher.addPart(
      filePart.fieldName(),
      filePart.file().toPath(),
      filePart.fileName(),
      null));

    parts.forEach(part -> publisher.addPart(
      part.name(),
      part.value(),
      part.contentType().asString()));

    requestBuilder.setHeader("Content-Type", "multipart/form-data; boundary=" + publisher.getBoundary());

    return send(requestBuilder.POST(publisher.build()));
  }

  private HttpRequest.BodyPublisher bodyPublisher(String body) {
    if (body != null) {
      return HttpRequest.BodyPublishers.ofString(body);
    } else {
      return HttpRequest.BodyPublishers.noBody();
    }
  }

  private JavaNetHttpRequest(HttpClient client, String resource, String description, HttpOptions options) {
    this.client = client;
    this.resource = resource;
    this.description = description;
    this.httpOptions = options;
  }

  public static class Builder {
    private final String resource;
    private String description = "no request description provided";
    private HttpOptions httpOptions = HttpOptions.empty();

    private Builder(String resource) {
      this.resource = resource;
    }

    private static Builder on(String resource) {
      return new Builder(resource);
    }

    public Builder doing(String description) {
      this.description = description;
      return this;
    }

    public Builder using(HttpOptions httpOptions) {
      this.httpOptions = httpOptions;
      return this;
    }

    public JavaNetHttpRequest executeWith(HttpClient httpClient) {
      return new JavaNetHttpRequest(httpClient, resource, description, httpOptions);
    }
  }
}
