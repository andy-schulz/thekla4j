package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.core.activities.Sleep;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.ActorsWorld;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WorldTest {

  @Test
  void instantiateWorld() {

    Actor actor = Actor.named("tester")
        .withWorld(TestData.empty());

    DataRetriever<Actor, TestData> world = a -> (TestData)a.getWorld();

    actor
        .attemptsTo_(
            Sleep.<User>forA(Duration.ofSeconds(1)))
        .apply(User.empty().withUserName("MyUser"))
        .peek(user -> world.of(actor).setUser(user));

    assertThat(world.of(actor).user.userName, equalTo("MyUser"));

  }

  public interface DataRetriever<T, R> {
    R of(T test);
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  @ToString
  public static class TestData implements ActorsWorld {
    public User user;

    public static TestData empty() {
      return new TestData(
          null);
    }
  }

  @AllArgsConstructor
  @With
  @ToString
  public static class User {
    public String userName;
    public String password;

    public static User empty() {
      return new User(
          "",
          ""
      );
    }
  }
}
