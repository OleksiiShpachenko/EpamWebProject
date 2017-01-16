/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shpach.tutor.manager;

import java.util.ResourceBundle;

/**
 *
 * @author KMM
 */
public class Config {

    private static Config instance;
    private ResourceBundle resource;
    public static final String DATASOURCE="DATASOURCE";
    private static final String BUNDLE_NAME = "com.shpach.tutor.manager.config";
    public static final String DRIVER = "DRIVER";
    public static final String URL = "URL";
    public static final String MAIN = "MAIN";
    public static final String ERROR = "ERROR";
    public static final String LOGIN = "LOGIN";
    public static final String INDEX = "INDEX";
    public static final String AUTO_SUBMIT_LOGIN="AUTO_SUBMIT_LOGIN";
    public static final String TUTOR_TESTS="TUTOR_TESTS";
    public static final String TUTOR_CATEGORIES="TUTOR_CATEGORIES";
    public static final String TUTOR_COMMUNITIES="TUTOR_COMMUNITIES";
    public static final String TUTOR_QUESTIONS="TUTOR_QUESTIONS";

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            instance.resource = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) resource.getObject(key);
    }
}
