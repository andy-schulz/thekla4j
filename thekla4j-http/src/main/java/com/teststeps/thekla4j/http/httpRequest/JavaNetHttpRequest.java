package com.teststeps.thekla4j.http.httpRequest;

import com.teststeps.thekla4j.http.core.HttpResult;
import com.teststeps.thekla4j.http.error.RequestError;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import com.teststeps.thekla4j.utils.url.UrlHelper;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

  private Either<Throwable, HttpResult> send(HttpRequest.Builder requestBuilder) {
    URI uri = uri(httpOptions.baseUrl, resource, httpOptions.queryParameters);

    log.info(() -> "Starting Request: " + this.description + " to URI: " + uri);

    requestBuilder.uri(uri);

    requestBuilder.timeout(httpOptions.getResponseTimeout());

    httpOptions.headers.forEach(requestBuilder::setHeader);

    HttpRequest req = requestBuilder.build();


    return Try.of(() -> client.send(req, HttpResponse.BodyHandlers.ofString()))
        .onFailure(x -> log.error(x.getMessage()))
        .transform(RequestError.toEither(() -> "Error sending HTTP request to " + resource))
        .map(JavaNetHttpResult::new)
        .map(r -> (HttpResult) r)
        .mapLeft(x -> x);
  }

  @Override
  public Either<Throwable, HttpResult> get() {

    return send(requestBuilder.GET());
  }

  @Override
  public Either<Throwable, HttpResult> post() {
    return send(requestBuilder.POST(bodyPublisher(httpOptions.body)));
  }

  @Override
  public Either<Throwable, HttpResult> patch() {
    return send(requestBuilder.method("PATCH", bodyPublisher(httpOptions.body)));
  }

  @Override
  public Either<Throwable, HttpResult> put() {
    return send(requestBuilder.PUT(bodyPublisher(httpOptions.body)));
  }

  @Override
  public Either<Throwable, HttpResult> delete() {
    if (httpOptions.body != null) {
      return send(requestBuilder.method("DELETE", bodyPublisher(httpOptions.body)));
    }
    return send(requestBuilder.DELETE());
  }

  @Override
  public Either<Throwable, HttpResult> postFile(io.vavr.collection.List<FilePart> fileParts, List<Part> parts) {
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


  private URI uri(String baseUrl, String path, java.util.Map<String, String> queryParams) {
    return URI.create(baseUrl + path + UrlHelper.buildQueryString.apply(queryParams));
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
