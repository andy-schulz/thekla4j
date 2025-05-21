package com.teststeps.thekla4j.utils.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      throw new IOException("LocalDateTime argument is null.");
    }
    gen.writeString(DATE_TIME_FORMATTER.format(value));
  }
}
