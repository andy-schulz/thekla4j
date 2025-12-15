package com.teststeps.thekla4j.http.httpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MultipartBodyPublisher {
  private final List<PartsSpecification> partsSpecificationList = new ArrayList<>();
  private final String boundary = UUID.randomUUID().toString();

  public HttpRequest.BodyPublisher build() {
    if (partsSpecificationList.isEmpty()) {
      throw new IllegalStateException("No parts specified.");
    }
    addFinalBoundaryPart();
    return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
  }

  public String getBoundary() {
    return boundary;
  }

  public MultipartBodyPublisher addPart(String name, String value, String contentType) {
    PartsSpecification newPart = new PartsSpecification();
    newPart.type = PartsSpecification.Type.STRING;
    newPart.name = name;
    newPart.value = value;
    newPart.contentType = contentType;
    partsSpecificationList.add(newPart);
    return this;
  }

  public MultipartBodyPublisher addPart(String name, Path value, String filename, String contentType) {
    PartsSpecification newPart = new PartsSpecification();
    newPart.type = PartsSpecification.Type.FILE;
    newPart.name = name;
    newPart.path = value;
    newPart.filename = filename;
    newPart.contentType = contentType;
    partsSpecificationList.add(newPart);
    return this;
  }

  private void addFinalBoundaryPart() {
    PartsSpecification newPart = new PartsSpecification();
    newPart.type = PartsSpecification.Type.FINAL_BOUNDARY;
    newPart.value = "--" + boundary + "--";
    partsSpecificationList.add(newPart);
  }

  static class PartsSpecification {
    public enum Type {
      STRING, FILE, FINAL_BOUNDARY
    }

    Type type;
    String name;
    String value;
    Path path;
    String filename;
    String contentType;
  }

  class PartsIterator implements Iterator<byte[]> {

    private final Iterator<PartsSpecification> iter;
    private InputStream currentFileInput;

    private boolean done;
    private byte[] nextByteArray;

    PartsIterator() {
      iter = partsSpecificationList.iterator();
    }

    @Override
    public boolean hasNext() {
      if (done) return false;
      if (nextByteArray != null) return true;
      try {
        nextByteArray = computeNext();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
      if (nextByteArray == null) {
        done = true;
        return false;
      }
      return true;
    }

    @Override
    public byte[] next() {
      if (!hasNext()) throw new NoSuchElementException();
      byte[] res = nextByteArray;
      nextByteArray = null;
      return res;
    }

    private byte[] computeNext() throws IOException {
      if (currentFileInput == null) {
        if (!iter.hasNext()) return null;
        PartsSpecification nextPart = iter.next();
        if (PartsSpecification.Type.STRING.equals(nextPart.type)) {
          String contentType = nextPart.contentType;
          if (contentType == null) contentType = "text/plain; charset=UTF-8";
          String part =
              "--" + boundary + "\r\n" +
                  "Content-Disposition: form-data; name=\"" + nextPart.name + "\"\r\n" +
                  "Content-Type: " + contentType + "\r\n\r\n" +
                  nextPart.value + "\r\n";
          return part.getBytes(StandardCharsets.UTF_8);
        }
        if (PartsSpecification.Type.FINAL_BOUNDARY.equals(nextPart.type)) {
          return nextPart.value.getBytes(StandardCharsets.UTF_8);
        }
        if (PartsSpecification.Type.FILE.equals(nextPart.type)) {
          Path path = nextPart.path;
          String filename = nextPart.filename;
          String contentType = nextPart.contentType;
          if (contentType == null) {
            contentType = Files.probeContentType(path);
          }
          if (contentType == null) contentType = "application/octet-stream";

          String partHeader =
              "--" + boundary + "\r\n" +
                  "Content-Disposition: form-data; name=\"" + nextPart.name + "\"; filename=\"" + filename + "\"\r\n" +
                  "Content-Type: " + contentType + "\r\n\r\n";
          currentFileInput = Files.newInputStream(path);
          return partHeader.getBytes(StandardCharsets.UTF_8);
        }
      } else {
        byte[] buf = new byte[8192];
        int r = currentFileInput.read(buf);
        if (r > 0) {
          if (r < buf.length) {
            return Arrays.copyOf(buf, r);
          } else {
            return buf;
          }
        } else {
          currentFileInput.close();
          currentFileInput = null;
          return "\r\n".getBytes(StandardCharsets.UTF_8);
        }
      }
      return null;
    }
  }
}
