package com.teststeps.thekla4j.activityLog;

import com.teststeps.thekla4j.activityLog.data.ActivityLogNode;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogFormatter {

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
   * format the content of the node as html
   * it is the inner content of the ul/li html list
   *
   * @param node - the node which will be converted to the html list
   */
  public static String formatShortLogContentToHtml(ActivityLogNode node) {
    return "<span class=\"logMessage\"><span class = \"timestamp\">" + node.startedAt + " - </span>" +
        "<span class=\"activityName\">" + "[" + node.name + "]</span> - " +
        "<span class=\"activityDescription\"> " + (node.description.length() > 100 ? node.description.substring(0, 90) : node.description) + "</span>" +
        "</span>";
  }

  /**
   * format the given node to a static html representation
   * if the node contains sub node, they will be formatted recursively
   *
   * @param logNode the log node
   */
  public static String formatNodeToHtml(ActivityLogNode logNode) {

    Option<String> descr = Option.of(logNode.description);

    if (logNode.logType.equals(ActivityLogEntryType.Task) || logNode.logType.equals(ActivityLogEntryType.Group)) {

      String formattedText = "<li><span class=\"task %s\">%s</span>" +
          // ... in case the message is to long
          (descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") +
          (Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" : "<span class=\"label contentButton inContentButton\">In</span>") +
          (Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" : "<span class=\"label contentButton outContentButton\">Out</span>") +
          "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (descr.getOrElse("")
                                                                                                                                    .length() <= 100 ? "" : logNode.description) + "</pre></div></div>" +
          // IO Element
          "%s" +
          "<ul class=\"nested\">%s</ul>" +
          "</li>";

      return String.format(formattedText,
                           logNode.status,
                           formatShortLogContentToHtml(logNode),
                           formatIOElement(logNode.input, logNode.output),
                           logNode.activityNodes.stream().reduce(
                               "",
                               (acc, logEntry) -> acc + formatNodeToHtml(logEntry),
                               (s1, s2) -> null));

    } else if (logNode.logType == ActivityLogEntryType.Interaction) {

      String formattedText = "<li class=\"interaction %s\">%s" +
          (descr.getOrElse("").length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") +
          (Objects.isNull(logNode.input) || Objects.equals(logNode.input, "") ? "" : "<span class=\"label contentButton inContentButton\">In</span>") +
          (Objects.isNull(logNode.output) || Objects.equals(logNode.output, "") ? "" : "<span class=\"label contentButton outContentButton\">Out</span>") +
          "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (descr.getOrElse("")
                                                                                                                                    .length() <= 100 ? "" : logNode.description.replace("%", "%%")) + "</pre></div></div>" +
          // IO Element
          "%s" +
          "</li>";

      return String.format(formattedText,
                           logNode.status,
                           formatShortLogContentToHtml(logNode),
                           formatIOElement(logNode.input, logNode.output)
      );
    } else {
      throw new Error("Unknown Node Type ${logNode.logType}");
    }
  }

  /**
   * create a static html representation of the Activity node.
   * The node will be a foldable list (ul)
   *
   * @param logNode - the log node to create a html list from
   */
  static String formatLogWithHtmlTags(ActivityLogNode logNode) {
    return "<ul id = \"ActivityLog\">" + formatNodeToHtml(logNode) + "</ul>";
  }

  static String formatLogWithHtmlTags(List<ActivityLogNode> logNodes) {
    return logNodes.map(LogFormatter::formatLogWithHtmlTags)
        .collect(Collectors.joining("<br>"));
  }


  /**
   * format the node to an html tree and add the style and JS function to the html representation
   *
   * @param logNode - the log node to create a html list from
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

  public static String formatLogAsHtmlTree(List<ActivityLogNode> logNode) {

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

  private static String formatIOElement(String input, String output) {

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
            "</span>", input, output);
  }

  public static String formatLogWithPrefix(String logPrefix, int repeat, ActivityLogNode logNode) {
    return formatToText(logPrefix, repeat, logNode);
  }

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
      try (InputStreamReader isr = new InputStreamReader(is);
           BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }

  public static final String STYLE = "";

  /**
   * The style which is added to the HTML tree
   * a css file is read from the file system
   */
  static String htmlStyle = String.format("<style>${activityLogStyle.toString()}</style>", LogFormatter.STYLE);

  /**
   * the function script which is added to the HTML tree
   */
  static String functionScript =
      "<script>\n" +
          "var toggler = document.querySelectorAll(\".task\");\n" +
          "var inToggler = document.querySelectorAll(\".label.inContentButton\");\n" +
          "var outToggler = document.querySelectorAll(\".label.outContentButton\");\n" +
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
          "</script>";

  private LogFormatter() {
  }
}
