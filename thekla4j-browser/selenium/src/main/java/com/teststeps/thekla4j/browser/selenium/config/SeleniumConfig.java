package com.teststeps.thekla4j.browser.selenium.config;

import io.vavr.collection.Map;
import lombok.With;

/**
 * The Selenium configuration
 */
@With
public record SeleniumConfig(

                             /**
                              * the remote url for the selenium grid server
                              * 
                              * @param remoteUrl - the remote url for the selenium grid server
                              * @return - the remote url for the selenium grid server
                              */
                             String remoteUrl,

                             /**
                              * activate the local file detector
                              * 
                              * @param setLocalFileDetector - the local file detector
                              * @return - the local file detector
                              */
                             Boolean setLocalFileDetector,

                             /**
                              * the browserstack options
                              * 
                              * @param bStack - the browserstack options
                              * @return - the browserstack options
                              */
                             BrowsersStackOptions bStack,

                             /**
                              * the selenium options
                              * 
                              * @param seOptions - the selenium options
                              * @return - the selenium options
                              */
                             SeleniumOptions seOptions,

                             /**
                              * the capabilities
                              * 
                              * @param capabilities - the capabilities
                              * @return - the capabilities
                              */
                             Map<String, Map<String, String>> capabilities
) {
}
