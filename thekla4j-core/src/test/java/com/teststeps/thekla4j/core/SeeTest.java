package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.activities.Task;
import com.teststeps.thekla4j.core.base.persona.Actor;
import io.vavr.control.Either;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.function.Predicate;

public class SeeTest {

  public static class TestData extends Task<Void, String> {

    private String testString;

    @Override
    protected Either<ActivityError, String> performAs(Actor actor, Void result) {
      return Either.right(this.testString);
    }

    public static TestData with(String testString) {
      return new TestData(testString);
    }

    private TestData(String testString) {
      this.testString = testString;
    }
  }

  @Test
  public void simpleSeeAction() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> func = x -> x.equals("TestDate");
    Predicate<String> func2 = x -> x.equals("TestData");

    tester.attemptsTo(
        See.ifThe(TestData.with("TestData"))
           .is(Expected.to.pass(func, "predicate one"))
           .is(Expected.to.pass(func2, "predicate two"))
           .is(Expected.to.pass(func2, "predicate three"))
    );
//          .getOrElseThrow(Function.identity());

    String log = tester.activityLog.getStructuredHtmlLog();
    String log2 = tester.activityLog.toJson();

    System.out.println(log2);

    PrintWriter out = new PrintWriter("log.html");
    out.println(log);
    out.close();

  }

}
