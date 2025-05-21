package com.teststeps.thekla4j.core.data;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UserDataClass {

  public String name;


  public static UserDataClass standard() {
    return new UserDataClass("TesterClass");
  }
}
