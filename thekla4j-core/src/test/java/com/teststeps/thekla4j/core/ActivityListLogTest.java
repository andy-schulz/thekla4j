package com.teststeps.thekla4j.core;

import com.teststeps.thekla4j.activityLog.*;
import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;
import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import io.vavr.collection.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActivityListLogTest {
    static ActivityLogEntry childA1, childA2, parentA1, parentA2, grandParentA;
    static ActivityLogEntry childB1, childB2, parentB1, parentB2, grandParentB;
    @BeforeAll
    public static void init() {


        grandParentA = new ActivityLogEntry(
                "Grand Parent",
                "grand parent description",
                ActivityLogEntryType.Task,
                ActivityStatus.running,
                TASK_LOG.FULL_LOG,
                null
        );

        parentA1 = new ActivityLogEntry(
                "Parent-1",
                "parent description",
                ActivityLogEntryType.Task,
                ActivityStatus.running,
                TASK_LOG.FULL_LOG,
            grandParentA
        );

        parentA2 = new ActivityLogEntry(
                "Parent-2",
                "parent description",
                ActivityLogEntryType.Task,
                ActivityStatus.running,
                TASK_LOG.FULL_LOG,
            grandParentA
        );

        childA1 = new ActivityLogEntry(
                "Child-1",
                "child description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.running,
                TASK_LOG.DEFAULT,
            parentA1
        );

        childA2 = new ActivityLogEntry(
                "Child-2",
                "child description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.failed,
                TASK_LOG.DEFAULT,
            parentA2
        );



        grandParentB = new ActivityLogEntry(
            "Grand Parent B",
            "grand parent B description",
            ActivityLogEntryType.Task,
            ActivityStatus.failed,
            TASK_LOG.FULL_LOG,
            null
        );

        parentB1 = new ActivityLogEntry(
            "Parent-B1",
            "parent B description",
            ActivityLogEntryType.Task,
            ActivityStatus.failed,
            TASK_LOG.FULL_LOG,
            grandParentB
        );

        parentB2 = new ActivityLogEntry(
            "Parent-B2",
            "parent B description",
            ActivityLogEntryType.Task,
            ActivityStatus.passed,
            TASK_LOG.FULL_LOG,
            grandParentB
        );

        childB1 = new ActivityLogEntry(
            "Child-B1",
            "child B description",
            ActivityLogEntryType.Interaction,
            ActivityStatus.failed,
            TASK_LOG.DEFAULT,
            parentB1
        );

        childB2 = new ActivityLogEntry(
            "Child-B2",
            "child B description",
            ActivityLogEntryType.Interaction,
            ActivityStatus.passed,
            TASK_LOG.DEFAULT,
            parentB2
        );

    }

    @Test
    public void createSingleHtmlLog() {
        List<ActivityLogNode> logNodes = List.of(grandParentA.getLogTree(), grandParentB.getLogTree());

        String text = LogFormatter.formatLogAsHtmlTree(logNodes);

        System.out.println(text);

    }
}
