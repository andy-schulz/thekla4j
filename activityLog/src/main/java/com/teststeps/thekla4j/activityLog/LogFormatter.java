package com.teststeps.thekla4j.activityLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.stream.Collectors;

public class LogFormatter {

    public static String formatToText(String logPrefix, int repeat, ActivityLogNode logNode) {
        String formattedText = "%s[%s %s] - %s%s";
        String text = String.format(formattedText,
                new String(new char[repeat]).replace("\0", logPrefix),
                logNode.status == ActivityStatus.failed ? "✗" : "✓",
                logNode.name,
                logNode.description,
                logNode.activityNodes.stream().reduce(
                        "",
                        (acc, logEntry) -> acc + "\n" + LogFormatter.formatToText(logPrefix, repeat + 1, logEntry),
                        (s1, s2) -> null)
        );
        return text;
    }

    /**
     * format the content of the node as html
     * it is the inner content of the ul/li html list
     *
     * @param node - the node which will be converted to the html list
     */
    public static String formatShortLogContentToHtml(ActivityLogNode node) {
        return "<span class=\"logMessage\">" +
                "<span class=\"activityName\">[" + node.name + "]</span> - " +
                "<span class=\"activityDescription\"> " + (node.description.length() > 100 ? node.description.substring(0, 90) : node.description) + "</span>" +
                "</span>";
    }

    /**
     * format the given node to a static html representation
     * if the node contains sub node, they will be formatted recursively
     *
     * @param logNode
     */
    public static String formatNodeToHtml(ActivityLogNode logNode) {
        if (logNode.logType == ActivityLogEntryType.Task) {

            String formattedText = "<li><span class=\"task %s\">%s</span>" +
                    (logNode.description.length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") + // ... in case the message is to long
                    "%s" + // IO Element
                    "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (logNode.description.length() <= 100 ? "" : logNode.description) + "</pre></div></div>" +
                    "<ul class=\"nested\">%s</ul>" +
                    "</li>";

            return String.format(formattedText,
                    logNode.status,
                    formatShortLogContentToHtml(logNode),
                    formatIOElement(logNode.input),
                    logNode.activityNodes.stream().reduce(
                            "",
                            (acc, logEntry) -> acc + formatNodeToHtml(logEntry),
                            (s1, s2) -> null)
            );

        } else if (logNode.logType == ActivityLogEntryType.Interaction) {
            String formattedText = "<li class=\"interaction %s\">%s" +
                    (logNode.description.length() > 100 ? "<span class=\"ellipses contentButton\">...</span>" : "") +
                    "<div class=\"longDescription\"><div class=\"infoHeader\">Full Description</div><div class=\"infoMessage\"><pre>" + (logNode.description.length() <= 100 ? "" : logNode.description) + "</pre></div></div>" +
                    "</li>";

            return String.format(formattedText,
                    logNode.status,
                    formatShortLogContentToHtml(logNode)
            );
        } else {
            throw new Error("Unknown Node Type ${logNode.logType}");
        }
    }

/**
 * enclose the given text with an html tag and add an inline style to the element
 * @param text - the text to be enclosed
 * @param tag - the tag enclosing the text
 * @param style - the inline style which will be added to the tag
 */
//    export const encloseInTag=(text:string,tag:string,style?:string):string=>
//
//    {
//        return `<$ {
//        tag
//    } $ {
//        style ? `style = "${style}"` : ``}>$ {
//        text
//    }</$ {
//        tag
//    }>`
//    }
//
//    ;

    /**
     * create a static html representation of the Activity node.
     * The node will be a foldable list (ul)
     *
     * @param logNode - the log node to create a html list from
     */
    static String formatLogWithHtmlTags(ActivityLogNode logNode) {
        return "<ul id = \"ActivityLog\">" + formatNodeToHtml(logNode) + "</ul>";
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

    private static String formatIOElement(String input) {

        return input.equals("") ? "" : String.format(
                "<span class=\"with options\"><span class=\"label contentButton\">IO</span>\n" +
                "                <div class=\"ioInfo\">\n" +
                "                    <div class=\"ioContent\"><div class=\"infoHeader\">Input</div>" +
                "                        <div class=\"infoMessage\"><pre>%s</pre></div>\n" +
                "                    </div>\n" +
                "                </div>" +
                "</span>", input);
    }

    public static String formatLogWithPrefix(String logPrefix, int repeat, ActivityLogNode logNode) {
        return formatToText(logPrefix, repeat, logNode);
    }

    public static String encodeLog(String source) {
        String encodedString = Base64.getEncoder().encodeToString(source.getBytes());

        return encodedString;
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
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    //    public static InputStream style = getResourceFileAsString("ActivityLog.css");
    public static String style = "";
//        static File file = new File(Class.getClassLoader().getResource("ActivityLog.css").getFile());
//        String activityLogStyle=file.toString();

    /**
     * The style which is added to the HTML tree
     * a css file is read from the file system
     */
    static String htmlStyle = String.format("<style>${activityLogStyle.toString()}</style>", LogFormatter.style);

    /**
     * the function script which is added to the HTML tree
     */
    static String functionScript =
            "<script>\n" +
            "var toggler = document.querySelectorAll(\".task\");\n" +
            "var ioToggler = document.querySelectorAll(\".with.options .label\");\n" +
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
            "for (i = 0; i < ioToggler.length; i++) {\n" +
            "    ioToggler[i].addEventListener(\"click\", function() {\n" +
            "        this.parentElement.querySelector(\".ioInfo\").classList.toggle(\"ioActive\");\n" +
            "    });\n" +
            "}\n" +
            "for (i = 0; i < descToggler.length; i++) {\n" +
            "    descToggler[i].addEventListener(\"click\", function() {\n" +
            "        this.parentElement.querySelector(\".longDescription\").classList.toggle(\"descriptionActive\");\n" +
            "    });\n" +
            "}\n" +
            "</script>";
}
