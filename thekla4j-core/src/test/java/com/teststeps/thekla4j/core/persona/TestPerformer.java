package com.teststeps.thekla4j.core.persona;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.teststeps.thekla4j.commons.error.ActivityError;
import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.core.base.persona.Performer;
import com.teststeps.thekla4j.core.tasks.AddNumber;
import com.teststeps.thekla4j.core.tasks.FailingConsumerTask;
import com.teststeps.thekla4j.core.tasks.FailingSupplierTask;
import com.teststeps.thekla4j.core.tasks.SupplyNumber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestPerformer {

  static Actor actor;
  static Performer performer;

  @BeforeAll
  public static void setup() {
    actor = Actor.named("Test Actor");
    performer = Performer.of(actor);
  }

  //*******************************************/
  //*** Test Performer.attemptsTo() SUCCESS ***
  //******************************************/

  @Test
  public void testPerformerActor() {
    assertThat("check correct actor name", performer.actor(), equalTo(actor));
  }

  @Test
  public void testPerformerSingleTask() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(SupplyNumber.supplyNumber(2)),
      equalTo(2));
  }

  @Test
  public void testPerformerTwoTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3)),
      equalTo(5));
  }

  @Test
  public void testPerformerThreeTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4)),
      equalTo(9));
  }

  @Test
  public void testPerformerFourTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5)),
      equalTo(14));
  }

  @Test
  public void testPerformerFiveTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6)),
      equalTo(20));
  }

  @Test
  public void testPerformerSixTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7)),
      equalTo(27));
  }

  @Test
  public void testPerformerSevenTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8)),
      equalTo(35));
  }

  @Test
  public void testPerformerEightTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9)),
      equalTo(44));
  }

  @Test
  public void testPerformerNineTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10)),
      equalTo(54));
  }

  @Test
  public void testPerformerTenTasks() throws ActivityError {
    assertThat("check correct output",
      performer.attemptsTo(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10),
        AddNumber.of(11)),
      equalTo(65));
  }

  //*******************************************/
  //*** Test Performer.attemptsTo() FAILED ***
  //******************************************/

  @Test
  public void testPerformerThrowsSingleTask() {

    ActivityError error = assertThrows(ActivityError.class, () -> performer.attemptsTo(FailingSupplierTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7777"));
  }

  @Test
  public void testPerformerThrowsTwoTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 1"));
  }

  @Test
  public void testPerformerThrowsThreeTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 2"));
  }

  @Test
  public void testPerformerThrowsFourTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 4"));
  }

  @Test
  public void testPerformerThrowsFiveTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7"));
  }

  @Test
  public void testPerformerThrowsSixTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 11"));
  }

  @Test
  public void testPerformerThrowsSevenTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 16"));
  }

  @Test
  public void testPerformerThrowsEightTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 22"));
  }

  @Test
  public void testPerformerThrowsNineTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 29"));
  }

  @Test
  public void testPerformerThrowsTenTasks() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        FailingConsumerTask.start()));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 37"));
  }

  //*******************************************/
  //*** Test Performer.attemptsTo_() SUCCESS ***
  //******************************************/


  @Test
  public void testPerformerSingleTaskSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(AddNumber.of(2)).using(1),
      equalTo(3));
  }

  @Test
  public void testPerformerTwoTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2)).using(1),
      equalTo(4));
  }

  @Test
  public void testPerformerThreeTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3)).using(1),
      equalTo(7));
  }

  @Test
  public void testPerformerFourTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4)).using(1),
      equalTo(11));
  }

  @Test
  public void testPerformerFiveTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5)).using(1),
      equalTo(16));
  }

  @Test
  public void testPerformerSixTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6)).using(1),
      equalTo(22));
  }

  @Test
  public void testPerformerSevenTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7)).using(1),
      equalTo(29));
  }

  @Test
  public void testPerformerEightTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8)).using(1),
      equalTo(37));
  }

  @Test
  public void testPerformerNineTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9)).using(1),
      equalTo(46));
  }

  @Test
  public void testPerformerTenTasksSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10)).using(1),
      equalTo(56));
  }

  //*******************************************/
  //*** Test Performer.attemptsTo() FAILED ***
  //******************************************/


  @Test
  public void testPerformerThrowsSingleTaskSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class, () -> performer.attemptsTo_(FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 1"));
  }

  @Test
  public void testPerformerThrowsTwoTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 2"));
  }

  @Test
  public void testPerformerThrowsThreeTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 4"));
  }

  @Test
  public void testPerformerThrowsFourTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7"));
  }

  @Test
  public void testPerformerThrowsFiveTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 11"));
  }

  @Test
  public void testPerformerThrowsSixTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 16"));
  }

  @Test
  public void testPerformerThrowsSevenTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 22"));
  }

  @Test
  public void testPerformerThrowsEightTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 29"));
  }

  @Test
  public void testPerformerThrowsNineTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 37"));
  }

  @Test
  public void testPerformerThrowsTenTasksSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        FailingConsumerTask.start()).using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 46"));
  }

  //*******************************************/
  //*** Test Performer.attemptsTo$() SUCCESS ***
  //******************************************/

  @Test
  public void testPerformerSingleTaskUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(SupplyNumber.supplyNumber(2), "group", "step name"),
      equalTo(2));
  }

  @Test
  public void testPerformerTwoTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3), "group", "step name"),
      equalTo(5));
  }

  @Test
  public void testPerformerThreeTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4), "group", "step name"),
      equalTo(9));
  }

  @Test
  public void testPerformerFourTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5), "group", "step name"),
      equalTo(14));
  }

  @Test
  public void testPerformerFiveTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6), "group", "step name"),
      equalTo(20));
  }

  @Test
  public void testPerformerSixTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7), "group", "step name"),
      equalTo(27));
  }

  @Test
  public void testPerformerSevenTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8), "group", "step name"),
      equalTo(35));
  }

  @Test
  public void testPerformerEightTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9), "group", "step name"),
      equalTo(44));
  }

  @Test
  public void testPerformerNineTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10), "group", "step name"),
      equalTo(54));
  }

  @Test
  public void testPerformerTenTasksUsingGroup() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$(
        SupplyNumber.supplyNumber(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10),
        AddNumber.of(11), "group", "step name"),
      equalTo(65));
  }

  //*******************************************/
  //*** Test Performer.attemptsTo$() FAILED ***
  //*******************************************/

  @Test
  public void testPerformerThrowsSingleTaskUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(FailingSupplierTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7777"));
  }

  @Test
  public void testPerformerThrowsTwoTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 1"));
  }

  @Test
  public void testPerformerThrowsThreeTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 2"));
  }

  @Test
  public void testPerformerThrowsFourTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 4"));
  }

  @Test
  public void testPerformerThrowsFiveTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7"));
  }

  @Test
  public void testPerformerThrowsSixTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 11"));
  }

  @Test
  public void testPerformerThrowsSevenTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 16"));
  }

  @Test
  public void testPerformerThrowsEightTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 22"));
  }

  @Test
  public void testPerformerThrowsNineTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 29"));
  }

  @Test
  public void testPerformerThrowsTenTasksUsingGroup() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$(
        SupplyNumber.supplyNumber(1),
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        FailingConsumerTask.start(), "group", "step name"));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 37"));
  }

  //*********************************************/
  //*** Test Performer.attemptsTo$_() SUCCESS ***
  //*********************************************/

  @Test
  public void testPerformerSingleTaskUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(AddNumber.of(2), "group", "step name").using(1),
      equalTo(3));
  }

  @Test
  public void testPerformerTwoTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2), "group", "step name").using(1),
      equalTo(4));
  }

  @Test
  public void testPerformerThreeTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3), "group", "step name").using(1),
      equalTo(7));
  }

  @Test
  public void testPerformerFourTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4), "group", "step name").using(1),
      equalTo(11));
  }

  @Test
  public void testPerformerFiveTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5), "group", "step name").using(1),
      equalTo(16));
  }

  @Test
  public void testPerformerSixTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6), "group", "step name").using(1),
      equalTo(22));
  }

  @Test
  public void testPerformerSevenTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7), "group", "step name").using(1),
      equalTo(29));
  }

  @Test
  public void testPerformerEightTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8), "group", "step name").using(1),
      equalTo(37));
  }

  @Test
  public void testPerformerNineTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9), "group", "step name").using(1),
      equalTo(46));
  }

  @Test
  public void testPerformerTenTasksUsingGroupSuppliesData() throws ActivityError {

    assertThat("check correct output",
      performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        AddNumber.of(10), "group", "step name").using(1),
      equalTo(56));
  }

  //*********************************************/
  //*** Test Performer.attemptsTo$_() FAILED ***
  //*********************************************/

  @Test
  public void testPerformerThrowsSingleTaskUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 1"));
  }

  @Test
  public void testPerformerThrowsTwoTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 2"));
  }

  @Test
  public void testPerformerThrowsThreeTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 4"));
  }

  @Test
  public void testPerformerThrowsFourTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 7"));
  }

  @Test
  public void testPerformerThrowsFiveTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 11"));
  }

  @Test
  public void testPerformerThrowsSixTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 16"));
  }

  @Test
  public void testPerformerThrowsSevenTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 22"));
  }

  @Test
  public void testPerformerThrowsEightTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 29"));
  }

  @Test
  public void testPerformerThrowsNineTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 37"));
  }

  @Test
  public void testPerformerThrowsTenTasksUsingGroupSuppliesData() {

    ActivityError error = assertThrows(ActivityError.class,
      () -> performer.attemptsTo$_(
        AddNumber.of(1),
        AddNumber.of(2),
        AddNumber.of(3),
        AddNumber.of(4),
        AddNumber.of(5),
        AddNumber.of(6),
        AddNumber.of(7),
        AddNumber.of(8),
        AddNumber.of(9),
        FailingConsumerTask.start(), "group", "step name").using(1));

    assertThat("check correct error message", error.getMessage(),
      equalTo("This task always fails with input: 46"));
  }


}
