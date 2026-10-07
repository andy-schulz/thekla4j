package com.teststeps.thekla4j.browser.selenium;

public class Constants {
  /**
   * The url of the frameworktester of global_resources/docker-compose.yml.
   * <p>
   * A locally started browser reaches it as localhost. A browser running inside a grid container does not, so a run
   * against the grid has to pass the url of the host:
   * <p>
   * -Dthekla4j.test.appUrl=http://host.docker.internal:3000
   */
  public static String FRAMEWORKTESTER = System.getProperty("thekla4j.test.appUrl", "http://localhost:3000");
  public static String ELEMENT_STATES = FRAMEWORKTESTER + "/elementStates/";
  public static String FRAMES = FRAMEWORKTESTER + "/frames/";
  public static String CANVAS = FRAMEWORKTESTER + "/canvas/";
  public static String TABLE = FRAMEWORKTESTER + "/table/";
  public static String DOWNLOAD = FRAMEWORKTESTER + "/download/";
  public static String DRAG_AND_DROP = FRAMEWORKTESTER + "/dragndrop/";
}
