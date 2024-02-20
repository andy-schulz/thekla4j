package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import com.teststeps.thekla4j.activityLog.data.LogAttachmentType;
import com.teststeps.thekla4j.activityLog.data.NodeAttachment;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.vavr.API.*;


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
        logNode.name,
        logNode.description.replace("\n", "\n" + new String(new char[indents.length() + 7 + logNode.name.length()]).replace("\0", " ")),
        logNode.activityNodes.stream().reduce(
            "",
            (acc, logEntry) -> acc + "\n" + LogFormatter.formatToText(logPrefix, repeat + 1, logEntry),
            (s1, s2) -> null)
                        );
  }

  /**
   * format the content of the node as html it is the inner content of the ul/li html list
   *
   * @param node - the node which will be converted to the html list
   * @return the html representation of the node content
   */
  private static String formatShortLogContentToHtml(ActivityLogNode node) {
    return "<span class=\"logMessage\"><span class = \"timestamp\">" + node.startedAt + " - </span>" +
        "<span class=\"activityName\">" + "[" + node.name + "]</span> - " +
        "<span class=\"activityDescription\"> " + (node.description.length() > 100 ? node.description.substring(0, 90) :
        node.description) + "</span>" +
        "</span>";
  }

  /**
   * format the given node to a static html representation if the node contains sub node, they will be formatted recursively
   *
   * @param logNode the log node
   * @return the html representation of the node
   */
  private static String formatNodeToHtml(ActivityLogNode logNode) {

    Option<String> descr = Option.of(logNode.description);

    if (logNode.logType.equals(ActivityLogEntryType.Task) || logNode.logType.equals(ActivityLogEntryType.Group)) {

      String formattedText = "<li><span class=\"task %s\">%s</span>" +
          // ... in case the message is to long
          (descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") +
          (Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" :
              "<span class=\"label contentButton inContentButton\">In</span>") +
          (Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" :
              "<span class=\"label contentButton outContentButton\">Out</span>") +
          (Objects.isNull(logNode.attachments) || logNode.attachments.isEmpty() ? "" :
              "<span class=\"label contentButton attachmentContentButton\">Attachment</span>") +
          "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (descr.getOrElse("")
          .length() <= 100 ? "" : logNode.description) + "</pre></div></div>" +
          // IO Element
          "%s" +
          "<ul class=\"nested\">%s</ul>" +
          "</li>";

      return String.format(formattedText,
          logNode.status,
          formatShortLogContentToHtml(logNode),
          formatIOElement(logNode.input, logNode.output, Objects.isNull(logNode.attachments) ? List.empty() : List.ofAll(logNode.attachments)),
          logNode.activityNodes.stream().reduce(
              "",
              (acc, logEntry) -> acc + formatNodeToHtml(logEntry),
              (s1, s2) -> null));

    } else if (logNode.logType == ActivityLogEntryType.Interaction) {

      String formattedText = "<li class=\"interaction %s\">%s" +
          (descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") +
          (Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" :
              "<span class=\"label contentButton inContentButton\">In</span>") +
          (Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" :
              "<span class=\"label contentButton outContentButton\">Out</span>") +
          (Objects.isNull(logNode.attachments) || logNode.attachments.isEmpty() ? "" :
              "<span class=\"label contentButton attachmentContentButton\">Attachment</span>") +
          "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (descr.getOrElse("")
          .length() <= 100 ? "" : logNode.description.replace("%", "%%")) + "</pre></div></div>" +
          // IO Element
          "%s" +
          "</li>";

      return String.format(formattedText,
          logNode.status,
          formatShortLogContentToHtml(logNode),
          formatIOElement(logNode.input, logNode.output, Objects.isNull(logNode.attachments) ? List.empty() : List.ofAll(logNode.attachments)));
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


  /**
   * format the node to an html tree and add the style and JS function to the html representation
   *
   * @param logNode - the log node to create a html list from
   * @return the html representation of the node
   */
  public static String formatLogAsHtmlTree(ActivityLogNode logNode) {

    // eslint-disable-next-line @typescript-eslint/no-use-before-define
    String formattedText = "<style>\n%s\n</style> " +
        "\n\n%s " +
        "\n\n%s";
    String returnText = "";
    try {
      returnText = String.format(formattedText,
          LogFormatter.getResourceFileAsString("style/ActivityLog.css"),
          formatLogWithHtmlTags(logNode),
          functionScript);

    } catch (IOException e) {
      e.printStackTrace();
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

    // eslint-disable-next-line @typescript-eslint/no-use-before-define
    String formattedText = "<style>\n%s\n</style> " +
        "\n\n%s " +
        "\n\n%s";
    String returnText = "";
    try {
      returnText = String.format(formattedText,
          LogFormatter.getResourceFileAsString("style/ActivityLog.css"),
          formatLogWithHtmlTags(logNodes),
          functionScript);

    } catch (IOException e) {
      e.printStackTrace();
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
  private static String formatIOElement(String input, String output, List<NodeAttachment> attachments) {

    return (Objects.isNull(input) || Objects.equals(input, "")) &&
        (Objects.isNull(output) || Objects.equals(output, "")) ? "" : String.format(
        "<span class=\"with options\">\n" +
            "                <div class=\"inInfo\">\n" +
            "                    <div class=\"ioContent\"><div class=\"infoHeader\">Input</div>" +
            "                        <div class=\"infoMessage\"><pre>%s</pre></div>\n" +
            "                    </div>\n" +
            "                </div>" +
            "                <div class=\"outInfo\">\n" +
            "                    <div class=\"ioContent\"><div class=\"infoHeader\">Output</div>" +
            "                        <div class=\"infoMessage\"><pre>%s</pre></div>\n" +
            "                    </div>\n" +
            "                </div>" +
            "                <div class=\"attachmentInfo\">\n" +
            "                    <div class=\"ioContent\"><div class=\"infoHeader\">Attachment</div>" +
            "                        <div class=\"infoMessage attachmentContainer\">%s</div>\n" +
            "                    </div>\n" +
            "                </div>" +
            "</span>", input, output, formatAttachments(attachments));
  }

  private static String formatAttachments(List<NodeAttachment> attachments) {
    return String.format("<div class=\"attachments\">%s</div>", attachments.map(LogFormatter::formatAttachment)
        .collect(Collectors.joining("<br>")));
  }

  private static String formatAttachment(NodeAttachment attachment) {
    return Match(attachment.type()).of(
        Case($(LogAttachmentType.TEXT_PLAIN), () -> formatTextAttachment(attachment.content())),
        Case($(LogAttachmentType.IMAGE_PNG), () -> fileToBase64String(attachment)),
        Case($(LogAttachmentType.IMAGE_BASE64), () -> formatPngBase64FileAttachment(attachment.content())),
        Case($(), () -> "<div class=\"attachment\"><pre>" + attachment.content() + "</pre></div>")
                                      );
  }

  private static String formatTextAttachment(String attachment) {
    return "<div class=\"attachment\"><pre>" + attachment + "</pre></div>";
  }

  private static String formatPngBase64FileAttachment(String base64Attachment) {
    return "<div class=\"attachment\"><img src=\"data:image/png;base64," + base64Attachment + "\"/></div>";
  }

  private static String fileToBase64String(NodeAttachment attachment) {

    return

        Try.of(() -> new File(attachment.content()))
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
          InputStreamReader isr = new InputStreamReader(is);
          BufferedReader reader = new BufferedReader(isr)
      ) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }

  /**
   * The style which is applied to the HTML tree
   */
  private static final String STYLE = "";

  /**
   * The style which is added to the HTML tree a css file is read from the file system
   */
  private static String htmlStyle = String.format("<style>${activityLogStyle.toString()}</style>", LogFormatter.STYLE);

  /**
   * the function script which is added to the HTML tree
   */
  private static String functionScript =
      "<script>\n" +
          "var toggler = document.querySelectorAll(\".task\");\n" +
          "var inToggler = document.querySelectorAll(\".label.inContentButton\");\n" +
          "var outToggler = document.querySelectorAll(\".label.outContentButton\");\n" +
          "var attachmentToggler = document.querySelectorAll(\".label.attachmentContentButton\");\n" +
          "var descToggler = document.querySelectorAll(\".ellipses\");\n" +
          "var i;\n" +
          "\n" +
          "for (i = 0; i < toggler.length; i++) {\n" +
          "  toggler[i].addEventListener(\"click\", function() {\n" +
          "    this.parentElement.querySelector(\".nested\").classList.toggle(\"active\");\n" +
          "    this.classList.toggle(\"task-open\");\n" +
          "  });\n" +
          "}\n" +
          "\n" +
          "for (i = 0; i < inToggler.length; i++) {\n" +
          "    inToggler[i].addEventListener(\"click\", function() {\n" +
          "        this.parentElement.querySelector(\".inInfo\").classList.toggle(\"inActive\");\n" +
          "        this.classList.toggle(\"active\");\n" +
          "    });\n" +
          "}\n" +
          "\n" +
          "for (i = 0; i < outToggler.length; i++) {\n" +
          "    outToggler[i].addEventListener(\"click\", function() {\n" +
          "        this.parentElement.querySelector(\".outInfo\").classList.toggle(\"outActive\");\n" +
          "        this.classList.toggle(\"active\");\n" +
          "    });\n" +
          "}\n" +
          "for (i = 0; i < descToggler.length; i++) {\n" +
          "    descToggler[i].addEventListener(\"click\", function() {\n" +
          "        this.parentElement.querySelector(\".longDescription\").classList.toggle(\"descriptionActive\");\n" +
          "        this.classList.toggle(\"active\");\n" +
          "    });\n" +
          "}\n" +
          "for (i = 0; i < attachmentToggler.length; i++) {\n" +
          "    attachmentToggler[i].addEventListener(\"click\", function() {\n" +
          "        this.parentElement.querySelector(\".attachmentInfo\").classList.toggle(\"attachmentActive\");\n" +
          "        this.classList.toggle(\"active\");\n" +
          "    });\n" +
          "}\n" +
          "</script>";

  private LogFormatter() {
  }
}
