package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.assertions.Expected;
import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.activities.See;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.tasks.StaticStringTask;
import io.vavr.collection.List;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.teststeps.thekla4j.core.activities.API.map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SeeTest {

  @Test
  public void simpleSeeAction() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> func = x -> x.equals("TestDate");
    Predicate<String> func2 = x -> x.equals("TestData");

    tester.attemptsTo(
        See.<Void,String>ifThe(StaticStringTask.with("TestData"))
            .is(Expected.to.pass(func, "predicate one"))
            .is(Expected.to.pass(func2, "predicate two"))
            .is(Expected.to.pass(func2, "predicate three")),

        map(x -> x)
    );
//          .getOrElseThrow(Function.identity());

    String log = tester.activityLog.getStructuredHtmlLog();
    String log2 = tester.activityLog.toJson();

    System.out.println(log2);

    PrintWriter out = new PrintWriter("log.html");
    out.println(log);
    out.close();

  }

  public void passThrough() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    Predicate<String> func = x -> x.equals("TestDate");
    Predicate<String> func2 = x -> x.equals("TestData");

    tester.attemptsTo(

        See.ifThe(StaticStringTask.with("TestData"))
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

  @Test
  public void testListAssertion() throws ActivityError, FileNotFoundException {
    Actor tester = Actor.named("Tester");

    tester.attemptsTo_(
        See.<List<String>>ifResult()
            .is(Expected.to.pass(x -> x.length() > 1, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals("test"), "predicate two"))
    ).apply(List.of("test"));

  }

  @Test
  public void testValueList() throws ActivityError {
    Actor tester = Actor.named("Tester");
    Object val = tester.attemptsTo(
        See.ifValue(List.of(1,2,3))
            .is(Expected.to.pass(x -> x.length() == 3, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals(1), "predicate two"))
    ).getOrElseThrow(Function.identity());
  }

  @Test
  public void testErrorValueList() {
    Actor tester = Actor.named("Tester");
    Either<ActivityError, Void> val = tester.attemptsTo(
        See.ifValue(List.of(1,2,3))
            .is(Expected.to.pass(x -> x.length() == 2, "predicate one"))
            .is(Expected.to.pass(x -> x.head().equals(2), "predicate two"))
    );

    assertThat("Either should contain an Error", val.isLeft());
    assertThat("", val.getLeft().getMessage(),
        equalTo("\nexpect predicate 'predicate one' to pass on \nList(1, 2, 3)\n\nexpect predicate 'predicate two' to pass on \nList(1, 2, 3)\n"));
  }

}
