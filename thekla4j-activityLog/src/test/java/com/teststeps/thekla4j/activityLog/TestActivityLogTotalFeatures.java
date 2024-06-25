package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestActivityLogTotalFeatures {

  static ActivityLogEntry child1, child2, parent1, parent2, grandParent;


  @BeforeAll
  public static void init() {


    grandParent = new ActivityLogEntry(
      "Step",
      "step description",
      ActivityLogEntryType.Task,
      ActivityStatus.passed,
      TASK_LOG.FULL_LOG,
      null
    );

    parent1 = new ActivityLogEntry(
      "TaskONE",
      "TaskONE description",
      ActivityLogEntryType.Task,
      ActivityStatus.passed,
      TASK_LOG.FULL_LOG,
      grandParent
    );

    parent2 = new ActivityLogEntry(
      "TaskTWO",
      "TaskTWO description",
      ActivityLogEntryType.Task,
      ActivityStatus.passed,
      TASK_LOG.FULL_LOG,
      grandParent
    );

    child1 = new ActivityLogEntry(
      "TaskONE-Child",
      "TaskONE-Child description",
      ActivityLogEntryType.Interaction,
      ActivityStatus.passed,
      TASK_LOG.DEFAULT,
      parent1
    );

    child2 = new ActivityLogEntry(
      "TaskTWO-Child",
      "TaskTWO-Child description",
      ActivityLogEntryType.Interaction,
      ActivityStatus.passed,
      TASK_LOG.DEFAULT,
      parent2
    );

  }


  @Test
  public void createSingleLog() throws IOException {
    String text = LogFormatter.formatLogAsHtmlTree(grandParent.getLogTree());
    String fileName = System.getProperty("user.dir") + "/../docs/readme/activityLog.html";
    System.out.println(text);

    System.out.println(fileName);

    FileWriter fileWriter = new FileWriter(fileName);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.print(text);
    printWriter.close();
  }

}
