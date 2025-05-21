package com.teststeps.thekla4j.utils.terminal;

import io.vavr.Function2;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormattedOutput {

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_BLACK = "\u001B[30m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_WHITE = "\u001B[37m";

  private static final String BOLD = "\u001B[1m";
  private static final String ITALIC = "\u001B[3m";
  private static final String UNDERLINE = "\u001B[4m";

  private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
  private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
  private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
  private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
  private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
  private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


  private static final Function2<String, String, String> addColorMultiline =
      (text, color) -> Stream.of(text.split("\n"))
          .map(t -> color + t + ANSI_RESET)
          .collect(Collectors.joining("\n"));

  /**
   * Add color BLACK to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String BLACK(String text) {
    return addColorMultiline.apply(text, ANSI_BLACK);
  }

  /**
   * Add color RED to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String RED(String text) {
    return addColorMultiline.apply(text, ANSI_RED);
  }

  /**
   * Add color GREEN to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String GREEN(String text) {
    return addColorMultiline.apply(text, ANSI_GREEN);
  }

  /**
   * Add color YELLOW to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String YELLOW(String text) {
    return addColorMultiline.apply(text, ANSI_YELLOW);
  }

  /**
   * Add color BLUE to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String BLUE(String text) {
    return addColorMultiline.apply(text, ANSI_BLUE);
  }

  /**
   * Add color PURPLE to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String PURPLE(String text) {
    return addColorMultiline.apply(text, ANSI_PURPLE);
  }

  /**
   * Add color CYAN to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String CYAN(String text) {
    return addColorMultiline.apply(text, ANSI_CYAN);
  }

  /**
   * Add color WHITE to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String WHITE(String text) {
    return addColorMultiline.apply(text, ANSI_WHITE);
  }

  /**
   * make the text BOLD
   *
   * @param text text to format
   * @return formatted text
   */
  public static String BOLD(String text) {
    return addColorMultiline.apply(text, BOLD);
  }

  /**
   * make the text ITALIC
   *
   * @param text text to format
   * @return formatted text
   */
  public static String ITALIC(String text) {
    return addColorMultiline.apply(text, ITALIC);
  }

  /**
   * make the text UNDERLINE
   *
   * @param text text to format
   * @return formatted text
   */
  public static String UNDERLINE(String text) {
    return addColorMultiline.apply(text, UNDERLINE);
  }

  /**
   * Add color BLACK_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String BLACK_BG(String text) {
    return addColorMultiline.apply(text, ANSI_BLACK_BACKGROUND);
  }

  /**
   * Add color RED_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String RED_BG(String text) {
    return addColorMultiline.apply(text, ANSI_RED_BACKGROUND);
  }

  /**
   * Add color GREEN_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String GREEN_BG(String text) {
    return addColorMultiline.apply(text, ANSI_GREEN_BACKGROUND);
  }

  /**
   * Add color YELLOW_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String YELLOW_BG(String text) {
    return addColorMultiline.apply(text, ANSI_YELLOW_BACKGROUND);
  }

  /**
   * Add color BLUE_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String BLUE_BG(String text) {
    return addColorMultiline.apply(text, ANSI_BLUE_BACKGROUND);
  }

  /**
   * Add color PURPLE_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String PURPLE_BG(String text) {
    return addColorMultiline.apply(text, ANSI_PURPLE_BACKGROUND);
  }

  /**
   * Add color CYAN_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String CYAN_BG(String text) {
    return addColorMultiline.apply(text, ANSI_CYAN_BACKGROUND);
  }

  /**
   * Add color WHITE_BACKGROUND to the text
   *
   * @param text text to color
   * @return colored text
   */
  public static String WHITE_BG(String text) {
    return addColorMultiline.apply(text, ANSI_WHITE_BACKGROUND);
  }

  private FormattedOutput() {
  }
}
