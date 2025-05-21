package com.teststeps.thekla4j.http.httpConn;

import com.teststeps.thekla4j.http.spp.multipart.FilePart;
import com.teststeps.thekla4j.http.spp.multipart.Part;
import com.teststeps.thekla4j.utils.vavr.LiftTry;
import io.vavr.Function4;
import io.vavr.Function6;
import io.vavr.Function8;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLConnection;

public class MultipartFunctions {

  protected static final Function4<List<Part>, String, String, StringWriter, Try<Void>> appendParts =
      (parts, boundary, line, writer) -> parts
          .zipWithIndex()
          .map(partTuple -> MultipartFunctions.appendPart.apply(partTuple._1, partTuple._2, parts.length(), boundary, line, writer))
          .transform(LiftTry.fromList())
          .map(__ -> null);

  protected static final Function6<Part, Integer, Integer, String, String, StringWriter, Try<Void>> appendPart =
      (part, index, length, boundary, line, writer) -> Try.of(() -> {
        writer
            .append("Content-Disposition: form-data; name=\"")
            .append(part.name())
            .append("\"")
            .append(line)
            .append("Content-Type: ")
            .append(part.contentType().asString())
            .append(line)
            .append(line)
            .append(part.value())
            .append(line)
            .append("--")
            .append(boundary);

        if (index + 1 < length) {
          writer.append(line);
        }

        return null;
      });

  protected static final Function6<List<FilePart>, String, String, PrintWriter, OutputStream, StringWriter, Try<Void>> appendFileParts =
      (fileParts, boundary, line, printWriter, outputStream, logWriter) -> fileParts
          .zipWithIndex()
          .map(
            filePartTuple -> MultipartFunctions.appendFilePart.apply(filePartTuple._1, filePartTuple._2, fileParts.length(), boundary, line,
              printWriter,
              outputStream, logWriter))
          .transform(LiftTry.fromList())
          .map(__ -> null);

  protected static final Function8<FilePart, Integer, Integer, String, String, PrintWriter, OutputStream, StringWriter, Try<Void>> appendFilePart =
      (filePart, index, length, boundary, line, printWriter, outputStream, logWriter) -> {
        StringWriter sw = new StringWriter();

        sw.append("Content-Disposition: form-data; name=\"" + filePart.fieldName() + "\"; filename=\"" + filePart.file().getName() + "\"")
            .append(line);
        sw.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(filePart.file().getName())).append(line);
        sw.append("Content-Transfer-Encoding: binary").append(line);

        sw.append(line);

        logWriter.append(sw.toString());

        printWriter.append(sw.toString());
        printWriter.flush();

        return Try.run(() -> {

          FileInputStream inputStream = new FileInputStream(filePart.file());
          byte[] buffer = new byte[4096];
          int bytesRead = -1;
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            logWriter.append("Writing " + bytesRead + " bytes");
          }
          outputStream.flush();
          inputStream.close();
          logWriter.append(line + "--" + boundary);
          printWriter.append(line).append("--").append(boundary);

          if (index + 1 < length) {
            logWriter.append(line);
            printWriter.append(line);
          }

          printWriter.flush();

        }).onFailure(Throwable::printStackTrace);
      };
}
