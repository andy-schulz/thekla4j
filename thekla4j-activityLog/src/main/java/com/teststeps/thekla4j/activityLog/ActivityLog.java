package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.annotations.TASK_LOG;

import java.util.Arrays;
import java.util.List;


public interface ActivityLog {

    ActivityLogEntry addActivityLogEntry(
        String activityName,
        String activityDescription,
        ActivityLogEntryType activityType,
        TASK_LOG activityLogType,
        ActivityStatus activityStatus);

    ActivityLogEntry addGroup(
        String activityName,
        String activityDescription);

    void reset(ActivityLogEntry entry);

    String getStructuredLog(String logPrefix);

    String getEncodedStructuredLog(String logPrefix);

    String getStructuredHtmlLog();
    String getStructuredHtmlLogWithoutIO();


    String getEncodedStructuredHtmlLog();
    String getEncodedStructuredHtmlLogWithoutIO();

}
