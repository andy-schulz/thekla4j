package com.teststeps.thekla4j.activityLog;

import static io.vavr.API.*;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;


/**
 * A class to format the activity log to a text or html representation
 */
@Log4j2(topic = "ActivityLogFormatter")
public class LogFormatter {

  /**
   * format the content of the node as text
   *
   * @param logPrefix - the prefix which will be added to the log
   * @param repeat    - the number of times the prefix will be repeated
   * @param logNode   - the node which will be converted to the text representation
   * @return the text representation of the node
   */
  public static String formatToText(String logPrefix, int repeat, ActivityLogNode logNode) {
    String formattedText = "%s[%s %s] - %s%s";
    String indents = new String(new char[repeat]).replace("\0", logPrefix);

    return String.format(formattedText,
      indents,
      logNode.status == ActivityStatus.failed ? "✗" : "✓",
//      logNode.status == ActivityStatus.failed ? "x" : "OK",
      logNode.name,
      logNode.description.replace("\n", "\n" + new String(new char[indents.length() + 7 + logNode.name.length()]).replace("\0", " ")),
      logNode.activityNodes.stream()
          .reduce(
            "",
            (acc, logEntry) -> acc + "\n" + LogFormatter.formatToText(logPrefix, repeat + 1, logEntry),
            (s1, s2) -> null));
  }

  private static String formatDuration(Duration duration) {
    return String.format("%s.%02d",
      duration.toSeconds(),
      duration.toMillisPart());
  }

  /**
   * format the content of the node as html it is the inner content of the ul/li html list
   *
   * @param node - the node which will be converted to the html list
   * @return the html representation of the node content
   */
  protected static String formatShortLogContentToHtml(ActivityLogNode node) {
    return """
        <span class="logMessage">
          <span class="timestamp">{$$_NODE_STARTED_AT} - {$$_NODE_ENDED_AT}</span>
          <span class="duration">{$$_NODE_DURATION} sec - </span>
          <span><span class="activityName">[{$$_NODE_NAME}]</span> - </span>
          <span class="activityDescription">{$$_NODE_DESCRIPTION}</span>
        </span>"""
        .replace("{$$_NODE_STARTED_AT}", node.startedAt)
        .replace("{$$_NODE_ENDED_AT}", node.endedAt)
        .replace("{$$_NODE_DURATION}", formatDuration(node.duration))
        .replace("{$$_NODE_NAME}", node.name)
        .replace("{$$_NODE_DESCRIPTION}", (node.description.length() > 100 ? node.description.substring(0, 90) : node.description));
  }

  /**
   * format the given node to a static html representation if the node contains sub node, they will be formatted
   * recursively
   *
   * @param logNode the log node
   * @return the html representation of the node
   */
  private static String formatNodeToHtml(ActivityLogNode logNode) {

    Option<String> descr = Option.of(logNode.description);

    if (logNode.logType.equals(ActivityLogEntryType.Task) || logNode.logType.equals(ActivityLogEntryType.Group)) {

      return """
          <li><span class="task {$$_TASK_STATUS}">{$$_LOG_SHORT_CONTENT}</span>
          {$$_EXPAND_DESCRIPTION_BUTTON}
          {$$_IN_BUTTON}
          {$$_OUT_BUTTON}
          {$$_ATTACHMENT_BUTTON}
          {$$_VIDEO_ATTACHMENT_BUTTON}
          <div class="longDescription"><div class="infoHeader">Full Description</div><div class="infoMessage"><pre>{$$_DESCRIPTION}</pre></div></div>
          {$$_IO_CONTENT}
          <ul class="nested">{$$_SUBNOTES}</ul>
          </li>
          """
          .replace("{$$_TASK_STATUS}", logNode.status.toString())
          .replace("{$$_LOG_SHORT_CONTENT}", formatShortLogContentToHtml(logNode))
          .replace("{$$_EXPAND_DESCRIPTION_BUTTON}", descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "")

          .replace("{$$_IN_BUTTON}",
            Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" :
                "<span class=\"label contentButton inContentButton\"><i class=\"fa fa-hand-o-right iconButton\"></i>In</span>")

          .replace("{$$_OUT_BUTTON}", Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" :
              "<span class=\"label contentButton outContentButton\"><i class=\"fa fa-hand-o-left iconButton\"></i>Out</span>")

          .replace("{$$_ATTACHMENT_BUTTON}", Objects.isNull(logNode.attachments) || logNode.attachments.isEmpty() ? "" :
              "<span class=\"label contentButton attachmentContentButton\"><i class=\"fa fa-file-photo-o iconButton\"></i>Attachment</span>")

          .replace("{$$_VIDEO_ATTACHMENT_BUTTON}", Objects.isNull(logNode.videoAttachments) || logNode.videoAttachments.isEmpty() ? "" :
              "<span class=\"label videoContentButton contentButton videoButton\" ><i class=\"fa fa-video-camera videoButton\"></i></span>")

          .replace("{$$_DESCRIPTION}", descr.getOrElse("").length() <= 100 ? "" : logNode.description)
          .replace("{$$_IO_CONTENT}",
            formatIOElement(
              logNode.input, logNode.output,
              Objects.isNull(logNode.attachments) ? List.empty() : List.ofAll(logNode.attachments),
              Objects.isNull(logNode.videoAttachments) ? List.empty() : List.ofAll(logNode.videoAttachments)))

          .replace("{$$_SUBNOTES}", logNode.activityNodes.stream().reduce("", (acc, logEntry) -> acc + formatNodeToHtml(logEntry), (s1, s2) -> null));

    } else if (logNode.logType == ActivityLogEntryType.Interaction) {

      return """
          <li class="interaction {$$_TASK_STATUS}">{$$_LOG_SHORT_CONTENT}
            {$$_EXPAND_DESCRIPTION_BUTTON}
            {$$_IN_BUTTON}
            {$$_OUT_BUTTON}
            {$$_ATTACHMENT_BUTTON}
            {$$_VIDEO_ATTACHMENT_BUTTON}
          <div class="longDescription"><div class="infoHeader">Full Description</div><div class="infoMessage"><pre>{$$_DESCRIPTION}</pre></div></div>
          {$$_IO_CONTENT}
          </li>
          """
          .replace("{$$_TASK_STATUS}", logNode.status.toString())
          .replace("{$$_LOG_SHORT_CONTENT}", formatShortLogContentToHtml(logNode))
          .replace("{$$_EXPAND_DESCRIPTION_BUTTON}", descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "")

          .replace("{$$_IN_BUTTON}",
            Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" :
                """
                      <span class="label contentButton inContentButton"><i class="fa fa-hand-o-right iconButton"></i>In</span>
                    """)

          .replace("{$$_OUT_BUTTON}", Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" :
              "<span class=\"label contentButton outContentButton\"><i class=\"fa fa-hand-o-left iconButton\"></i>Out</span>")

          .replace("{$$_ATTACHMENT_BUTTON}", Objects.isNull(logNode.attachments) || logNode.attachments.isEmpty() ? "" :
              "<span class=\"label contentButton attachmentContentButton\"><i class=\"fa fa-file-photo-o iconButton\"></i>Attachment</span>")

          .replace("{$$_VIDEO_ATTACHMENT_BUTTON}", Objects.isNull(logNode.videoAttachments) || logNode.videoAttachments.isEmpty() ? "" :
              "<span class=\"label attachmentContentButton contentButton videoButton\" ><i class=\"fa fa-video-camera videoButton\"></i></span>")

          .replace("{$$_DESCRIPTION}", descr.getOrElse("").length() <= 100 ? "" : logNode.description.replace("%", "%%"))
          .replace("{$$_IO_CONTENT}",
            formatIOElement(
              logNode.input, logNode.output,
              Objects.isNull(logNode.attachments) ? List.empty() : List.ofAll(logNode.attachments),
              Objects.isNull(logNode.videoAttachments) ? List.empty() : List.ofAll(logNode.videoAttachments)));

    } else {
      throw new Error("Unknown Node Type ${logNode.logType}");
    }
  }

  /**
   * create a static html representation of the Activity node. The node will be a foldable list (ul)
   *
   * @param logNode - the log node to create a html list from
   * @return the html representation of the node
   */
  private static String formatLogWithHtmlTags(ActivityLogNode logNode) {
    return "<ul id = \"ActivityLog\">" + formatNodeToHtml(logNode) + "</ul>";
  }

  private static String formatLogWithHtmlTags(List<ActivityLogNode> logNodes) {
    return logNodes.map(LogFormatter::formatLogWithHtmlTags)
        .collect(Collectors.joining("<br>"));
  }

  private static String legend = """
        <div class="legend">
            <input type="checkbox" id="toggleTimeSpan">
            <span id="timespanToggleDescription">switch visibility of timespan on or off</span>
        </div>
      """;

  /**
   * format the node to an html tree and add the style and JS function to the html representation
   *
   * @param logNode - the log node to create a html list from
   * @return the html representation of the node
   */
  public static String formatLogAsHtmlTree(ActivityLogNode logNode) {
    String formattedText = """
        <style>
        {$$_STYLE}
        </style>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        {$$_LEGEND}

        {$$_HTML_TAGS}

        {$$_SCRIPT}
        """;
    String returnText = "";
    try {
      returnText = formattedText
          .replace("{$$_STYLE}", Objects.requireNonNull(LogFormatter.getResourceFileAsString("style/ActivityLog.css")))
          .replace("{$$_LEGEND}", legend)
          .replace("{$$_HTML_TAGS}", formatLogWithHtmlTags(logNode))
          .replace("{$$_SCRIPT}", functionScript);
    } catch (Exception e) {
      log.error(e);
    }
    return returnText;
  }

  /**
   * format a list of nodes to an html tree and add the style and JS function to the html representation
   *
   * @param logNodes - the list of nodes to create an html list from
   * @return the html representation of the node list
   */
  public static String formatLogAsHtmlTree(List<ActivityLogNode> logNodes) {
    String formattedText = """
        <style>
        {$$_STYLE}
        </style>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        {$$_LEGEND}

        {$$_HTML_TAGS}

        {$$_SCRIPT}
        """;
    String returnText = "";
    try {
      returnText = formattedText
          .replace("{$$_STYLE}", Objects.requireNonNull(LogFormatter.getResourceFileAsString("style/ActivityLog.css")))
          .replace("{$$_LEGEND}", legend)
          .replace("{$$_HTML_TAGS}", formatLogWithHtmlTags(logNodes))
          .replace("{$$_SCRIPT}", functionScript);
    } catch (Exception e) {
      log.error(e);
    }
    return returnText;
  }

  /**
   * format the input and output of the activity node to html
   *
   * @param input  - the input of the activity
   * @param output - the output of the activity
   * @return the html representation of the input and output of an activity
   */
  private static String formatIOElement(String input, String output, List<NodeAttachment> attachments, List<NodeAttachment> videoAttachments) {

    return """
        <span class="with options">
          {$$_INPUT}
          {$$_OUTPUT}
          {$$_ATTACHMENT}
          {$$_VIDEO_ATTACHMENT}
        </span>
        """
        .replace("{$$_INPUT}", addInput(input))
        .replace("{$$_OUTPUT}", addOutput(output))
        .replace("{$$_ATTACHMENT}", addAttachments(attachments))
        .replace("{$$_VIDEO_ATTACHMENT}", addVideoAttachments(videoAttachments));
  }

  /**
   * format the input of the activity node to html
   *
   * @param input - the input of the activity
   * @return the html representation of the input of an activity
   */
  private static String addInput(String input) {
    return input == null || input.isEmpty() ? "" : """
        <div class="inInfo">
          <div class="ioContent">
            <div class="infoHeader">Input</div>
            <div class="infoMessage">
              <pre>{$$_INPUT}</pre>
            </div>
          </div>
        </div>
        """
        .replace("{$$_INPUT}", input);
  }

  /**
   * format all attachments as list
   *
   * @param attachments - the list of attachments to format
   * @return the html representation of the attachments
   */
  private static String addAttachments(List<NodeAttachment> attachments) {
    return attachments == null || attachments.isEmpty() ? "" : """
        <div class="attachmentInfo">
          <div class="ioContent">
            <div class="infoHeader">Attachment</div>
              <div class="infoMessage attachmentContainer">
                <div class="attachments">{$$_ATTACHMENTS}</div>
              </div>
          </div>
        </div>
        """
        .replace("{$$_ATTACHMENTS}", attachments
            .map(LogFormatter::formatAttachment)
            .collect(Collectors.joining("<br>")));
  }

  /**
   * format the output of the activity node to html
   *
   * @param output - the output of the activity
   * @return the html representation of the output of an activity
   */
  private static String addOutput(String output) {
    return output == null || output.isEmpty() ? "" : """
        <div class="outInfo">
          <div class="ioContent">
            <div class="infoHeader">Output</div>
            <div class="infoMessage">
              <pre>{$$_OUTPUT}</pre>
            </div>
          </div>
        </div>
        """
        .replace("{$$_OUTPUT}", output);
  }

  /**
   * format the video attachments to the html representation
   *
   * @param attachments - the list of attachments to format
   * @return the html representation of the attachments
   */
  private static String addVideoAttachments(List<NodeAttachment> attachments) {
    return attachments == null || attachments.isEmpty() ? "" :
        """
              <div class="videoInfo">
                <div class="ioContent">
                  <div class="infoHeader">Video</div>
                  <div class="infoMessage">
                    {$$_VIDEO_ATTACHMENT}
                  </div>
                </div>
              </div>
            """.replace("{$$_VIDEO_ATTACHMENT}", formatVideoAttachments(attachments));

  }

  /**
   * format all attachments as list
   *
   * @param attachments - the list of attachments to format
   * @return the html representation of the attachments
   */

  private static String formatVideoAttachments(List<NodeAttachment> attachments) {
    return attachments.map(attachment -> """
          <video class="video" width="820" controls>
            <source src="{$$_VIDEO_LINK}" type="{$$_VIDEO_MIME}">
          </video>
        """
        .replace("{$$_VIDEO_LINK}", attachment.content())
        .replace("{$$_VIDEO_MIME}", attachment.type().mime()))
        .collect(Collectors.joining("<br>"));
  }


  /**
   * format the attachment to the html representation
   *
   * @param attachment - the attachment to format
   * @return the html representation of the attachment
   */
  private static String formatAttachment(NodeAttachment attachment) {
    return Match(attachment.type()).of(
      Case($(LogAttachmentType.TEXT_PLAIN), () -> formatTextAttachment(attachment.content())),
      Case($(LogAttachmentType.IMAGE_PNG), () -> fileToBase64String(attachment)),
      Case($(LogAttachmentType.IMAGE_BASE64), () -> formatPngBase64FileAttachment(attachment.content())),
      Case($(), () -> "<div class=\"attachment\"><pre>" + attachment.content() + "</pre></div>"));
  }


  /**
   * format the text attachment to the html representation
   *
   * @param attachment - the attachment to format
   * @return the html representation of the attachment
   */
  private static String formatTextAttachment(String attachment) {
    return """
        <div class="attachment">
            <pre>{$$_ATTACHMENT}</pre>
        </div>
        """.replace("{$$_ATTACHMENT}", attachment);
  }

  /**
   * format the base64 encoded png file to the html representation
   *
   * @param base64Attachment - the base64 encoded png file
   * @return the html representation of the attachment
   */
  private static String formatPngBase64FileAttachment(String base64Attachment) {
    return """
        <div class="attachment">
            <img src="data:image/png;base64,{$$_BASE64_ATTACHMENT}
            "/>
        </div>
        """.replace("{$$_BASE64_ATTACHMENT}", base64Attachment);
  }

  /**
   * convert the file to a base64 encoded string
   *
   * @param attachment - the attachment to convert
   * @return the base64 encoded string
   */
  private static String fileToBase64String(NodeAttachment attachment) {

    return Try.of(() -> new File(attachment.content()))
        .map(File::toPath)
        .mapTry(Files::readAllBytes)
        .map(Base64.getEncoder()::encodeToString)
        .map(LogFormatter::formatPngBase64FileAttachment)
        .onFailure(log::error)
        .getOrElse("");
  }

  /**
   * prefix each line of the text log with the given prefix
   *
   * @param logPrefix - the prefix to add to each line
   * @param repeat    - the number of times the prefix will be repeated
   * @param logNode   - the node to convert to text
   * @return the text representation of the node
   */
  public static String formatLogWithPrefix(String logPrefix, int repeat, ActivityLogNode logNode) {
    return formatToText(logPrefix, repeat, logNode);
  }

  /**
   * encode the given source to a base64 string
   *
   * @param source - the source to encode
   * @return the base64 encoded string
   */
  public static String encodeLog(String source) {
    return Base64.getEncoder().encodeToString(source.getBytes());
  }


  /**
   * Reads given resource file as a string.
   *
   * @param fileName path to the resource file
   * @return the file's contents
   * @throws IOException if read fails for any reason
   */
  public static String getResourceFileAsString(String fileName) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
      if (is == null)
        return null;
      try (
           InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)
      ) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }

  /**
   * the function script which is added to the HTML tree
   */
  private static final String functionScript =
      """
            <script>
            var toggler = document.querySelectorAll(".task");
            var inToggler = document.querySelectorAll(".label.inContentButton");
            var outToggler = document.querySelectorAll(".label.outContentButton");
            var attachmentToggler = document.querySelectorAll(".label.attachmentContentButton");
            var videoToggler = document.querySelectorAll(".label.videoContentButton");

            var descToggler = document.querySelectorAll(".ellipses");
            var i;

            for (i = 0; i < toggler.length; i++) {
              toggler[i].addEventListener("click", function() {
                this.parentElement.querySelector(".nested").classList.toggle("active");
                this.classList.toggle("task-open");
              });
            }

            for (i = 0; i < inToggler.length; i++) {
                inToggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".inInfo").classList.toggle("inActive");
                    this.classList.toggle("active");
                });
            }

            for (i = 0; i < outToggler.length; i++) {
                outToggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".outInfo").classList.toggle("outActive");
                    this.classList.toggle("active");
                });
            }
            for (i = 0; i < descToggler.length; i++) {
                descToggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".longDescription").classList.toggle("descriptionActive");
                    this.classList.toggle("active");
                });
            }
            for (i = 0; i < attachmentToggler.length; i++) {
                attachmentToggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".attachmentInfo").classList.toggle("attachmentActive");
                    this.classList.toggle("active");
                });
            }
            for (i = 0; i < videoToggler.length; i++) {
                videoToggler[i].addEventListener("click", function() {
                    this.parentElement.querySelector(".videoInfo").classList.toggle("videoActive");
                    this.classList.toggle("active");
                });
            }

            var toggleButton = document.getElementById("toggleTimeSpan");
              toggleButton.addEventListener("change", function() {
                  var timestamps = document.querySelectorAll(".timestamp");
                  timestamps.forEach(function(timestamp) {
                      if (toggleButton.checked) {
                          timestamp.style.display = "inline-block";
                      } else {
                          timestamp.style.display = "none";
                      }
                  });
              });
            </script>
          """;

  private LogFormatter() {
  }
}
