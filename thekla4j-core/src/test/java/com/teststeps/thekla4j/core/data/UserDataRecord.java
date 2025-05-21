package com.teststeps.thekla4j.core.data;

public record UserDataRecord(
                             String name,
                             Integer age,
                             Boolean hasBike
) {

  public static UserDataRecord standard() {
    return new UserDataRecord("TesterRecord", 99, true);
  }
}
