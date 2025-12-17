package com.teststeps.thekla4j.http.httpRequest;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "MultipartBodyPublisher")
public class MultipartBodyPublisher {

  private List<Part> parts = List.empty();
  private final String boundary = UUID.randomUUID().toString().replace("-", "");

  public HttpRequest.BodyPublisher build() {
    if (parts.isEmpty()) {
      throw new IllegalStateException("No parts specified.");
    }

    // File parts must come last according to RFC 7578
    Tuple2<List<Part>, List<Part>> partitioned = parts.partition(p -> p instanceof FilePart);
    List<Part> allParts = partitioned._2.appendAll(partitioned._1).append(new FinalBoundaryPart());

    Iterable<byte[]> byteParts = allParts.map(part -> part.toBytes(boundary));

    log.trace(() -> "Building MultipartBodyPublisher with " + (allParts.size() - 1) + " parts" + allParts.map(p -> p.toString(boundary)).mkString());

    return HttpRequest.BodyPublishers.ofByteArrays(byteParts);
  }

  public String getBoundary() {
    return boundary;
  }

  public MultipartBodyPublisher addPart(String name, String value, String contentType) {
    parts = parts.append(new StringPart(name, value, contentType));
    return this;
  }

  public MultipartBodyPublisher addPart(String name, Path value, String filename, String contentType) {
    parts = parts.append(new FilePart(name, value, filename, contentType));
    return this;
  }

  interface Part {
    byte[] toBytes(String boundary);

    String toString(String boundary);
  }

  static class StringPart implements Part {
    private final String name;
    private final String value;
    private final String contentType;

    public StringPart(String name, String value, String contentType) {
      this.name = name;
      this.value = value;
      this.contentType = contentType != null ? contentType : "text/plain; charset=UTF-8";
    }

    @Override
    public byte[] toBytes(String boundary) {
      return toString(boundary).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String toString(String boundary) {
      return "--" + boundary + "\r\n" +
          "Content-Disposition: form-data; name=\"" + name + "\"\r\n" +
          "Content-Type: " + contentType + "\r\n\r\n" +
          value + "\r\n";
    }
  }

  static class FilePart implements Part {
    private final String name;
    private final Path path;
    private final String filename;
    private String contentType;

    public FilePart(String name, Path path, String filename, String contentType) {
      this.name = name;
      this.path = path;
      this.filename = filename;
      this.contentType = contentType;
    }

    private String getHeader(String boundary) throws IOException {

      if (contentType == null) {
        contentType = Files.probeContentType(path);
      }
      if (contentType == null) contentType = "application/octet-stream";

      return "--" + boundary + "\r\n" +
          "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n" +
          "Content-Type: " + contentType + "\r\n" +
          "Content-Transfer-Encoding: binary\r\n\r\n";
    }

    @Override
    public byte[] toBytes(String boundary) {
      try {

        String header = getHeader(boundary);

        byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
        byte[] fileBytes = Files.readAllBytes(path);
        byte[] newLineBytes = "\r\n".getBytes(StandardCharsets.UTF_8);

        byte[] result = new byte[headerBytes.length + fileBytes.length + newLineBytes.length];
        System.arraycopy(headerBytes, 0, result, 0, headerBytes.length);
        System.arraycopy(fileBytes, 0, result, headerBytes.length, fileBytes.length);
        System.arraycopy(newLineBytes, 0, result, headerBytes.length + fileBytes.length, newLineBytes.length);

        return result;
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public String toString(String boundary) {
      try {
        return getHeader(boundary) +
            "[file content of " + Files.size(path) + " bytes]\r\n";
      } catch (IOException e) {
        return "error reading file part header: " + e.getMessage();
      }
    }
  }

  static class FinalBoundaryPart implements Part {
    @Override
    public byte[] toBytes(String boundary) {
      return toString(boundary).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String toString(String boundary) {
      return "--" + boundary + "--";
    }
  }
}
