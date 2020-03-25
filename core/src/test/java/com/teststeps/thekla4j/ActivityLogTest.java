package com.teststeps.thekla4j;

import com.teststeps.thekla4j.activityLog.*;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActivityLogTest {
    ActivityLogEntry child1, child2, parent1, parent2, grandParent;

    @Before
    public void init() {


        grandParent = new ActivityLogEntry(
                "Grand Parent",
                "grand parent description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.running,
                null
        );

        parent1 = new ActivityLogEntry(
                "Parent-1",
                "parent description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.running,
                grandParent
        );

        parent2 = new ActivityLogEntry(
                "Parent-2",
                "parent description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.running,
                grandParent
        );

        child1 = new ActivityLogEntry(
                "Child-1",
                "child description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.running,
                parent1
        );

        child2 = new ActivityLogEntry(
                "Child-2",
                "child description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.failed,
                parent2
        );

    }

    @Test
    public void createSingleLog() {
        String text = LogFormatter.formatToText(" ", 0, child1.getLogTree());

        assertThat(text, equalTo("[✓ Child-1] - child description"));
    }

    @Test
    public void createDoubleLog() {
        String text = LogFormatter.formatToText("\t", 0, grandParent.getLogTree());

        String expected =
                "[✓ Grand Parent] - grand parent description\n" +
                        "\t[✓ Parent-1] - parent description\n" +
                        "\t\t[✓ Child-1] - child description\n" +
                        "\t[✓ Parent-2] - parent description\n" +
                        "\t\t[✗ Child-2] - child description";

        assertThat(text, equalTo(expected));
    }

    @Test
    public void createActivityLog() {
        ActivityLog log = new ActivityLog("Bernhard");
        String logText = LogFormatter.formatToText("\t", 0, log.getLogTree());
        assertThat(logText, equalTo("[✓ START] - Bernhard attempts to"));

        String logHtml = LogFormatter.formatShortLogContentToHtml(log.getLogTree());
        assertThat(logHtml, equalTo("<span class=\"logMessage\"><span class=\"activityName\">[START]</span> - <span class=\"activityDescription\"> Bernhard attempts to</span></span>"));

        String logHtmlList = LogFormatter.formatNodeToHtml(log.getLogTree());
        assertThat(logHtmlList, equalTo("<li><span class=\"task passed\"><span class=\"logMessage\"><span class=\"activityName\">[START]</span> - <span class=\"activityDescription\"> Bernhard attempts to</span></span></span><ul class=\"nested\"></ul></li>"));
    }

    @Test
    public void addChildToActivityLog() {
        ActivityLog log = new ActivityLog("Bernhard");

        final ActivityLogEntry firstActivity = log.addActivityLogEntry(
                "New Activity",
                "new Activity Name",
                ActivityLogEntryType.Task,
                ActivityStatus.passed
        );

        final ActivityLogEntry firstSubActivity = log.addActivityLogEntry(
                "New First Sub Activity",
                "New First Sub Activity description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.passed
        );

        assertThat(firstSubActivity.parent, equalTo(firstActivity));

        log.reset(firstActivity);


        final ActivityLogEntry secondActivity = log.addActivityLogEntry(
                "Second Activity",
                "Second Activity Description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.failed
        );

        assertThat(firstActivity.parent, equalTo(secondActivity.parent));

        log.reset(secondActivity);

        final ActivityLogEntry thirdActivity = log.addActivityLogEntry(
                "Third Activity",
                "Third Activity Description",
                ActivityLogEntryType.Interaction,
                ActivityStatus.passed
        );

        assertThat(firstActivity.parent, equalTo(thirdActivity.parent));

        String expectedOutput = "[✗ START] - Bernhard attempts to\n" +
                "\t[✓ New Activity] - new Activity Name\n" +
                "\t\t[✓ New First Sub Activity] - New First Sub Activity description\n" +
                "\t[✗ Second Activity] - Second Activity Description\n" +
                "\t[✓ Third Activity] - Third Activity Description";

        MatcherAssert.assertThat(LogFormatter.formatToText("\t", 0, log.getLogTree()),
                equalTo(expectedOutput));

        String logHtmlList = LogFormatter.formatNodeToHtml(log.getLogTree());
        String expectedHtmlList = "<li><span class=\"task failed\"><span class=\"logMessage\"><span class=\"activityName\">[START]</span> - <span class=\"activityDescription\"> Bernhard attempts to</span></span></span><ul class=\"nested\"><li><span class=\"task passed\"><span class=\"logMessage\"><span class=\"activityName\">[New Activity]</span> - <span class=\"activityDescription\"> new Activity Name</span></span></span><ul class=\"nested\"><li class=\"interaction passed\"><span class=\"logMessage\"><span class=\"activityName\">[New First Sub Activity]</span> - <span class=\"activityDescription\"> New First Sub Activity description</span></span></li></ul></li><li class=\"interaction failed\"><span class=\"logMessage\"><span class=\"activityName\">[Second Activity]</span> - <span class=\"activityDescription\"> Second Activity Description</span></span></li><li class=\"interaction passed\"><span class=\"logMessage\"><span class=\"activityName\">[Third Activity]</span> - <span class=\"activityDescription\"> Third Activity Description</span></span></li></ul></li>";
        assertThat(logHtmlList, equalTo(expectedHtmlList));


        String logHtmlTree = LogFormatter.formatLogAsHtmlTree(log.getLogTree());
        String encodedLogHtmlTree = LogFormatter.encodeLog(logHtmlTree);

        System.out.println(encodedLogHtmlTree);

    }

    @Test
    public void loadCss() throws IOException {
        System.out.println(LogFormatter.getResourceFileAsString("style/ActivityLog.css"));
    }
}
