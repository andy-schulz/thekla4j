package com.teststeps.thekla4j.browser.selenium.config;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.With;

/**
 * The Selenium configuration
 */
@With
public record SeleniumGridConfig(

                                 /**
                                  * the remote url for the selenium grid server
                                  * 
                                  * @param remoteUrl - the remote url for the selenium grid server
                                  * @return - the remote url for the selenium grid server
                                  */
                                 String remoteUrl,

                                 /**
                                  * the capabilities
                                  * 
                                  * @param capabilities - the capabilities
                                  * @return - the capabilities
                                  */
                                 Map<String, Map<String, String>> capabilities
) {

  public static SeleniumGridConfig of(String remoteUrl) {
    return new SeleniumGridConfig(
                                  remoteUrl,
                                  HashMap.empty());
  }


}
