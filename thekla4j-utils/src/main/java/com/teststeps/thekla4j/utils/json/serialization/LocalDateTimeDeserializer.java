package com.teststeps.thekla4j.utils.json.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
    String dateAsString = parser.getText();

    if (dateAsString == null) {
      throw new IOException("OffsetDateTime argument is null.");
    }
    return LocalDateTime.parse(dateAsString, DATE_TIME_FORMATTER);
  }
}
