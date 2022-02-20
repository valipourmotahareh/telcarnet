package com.thimat.sockettelkarnet.rest;

/**
 * contain client information such as BASE_URL || client information
 */
public class ClientConfigs {
    public static final String REST_API_BASE_URL ="http://telcarnet.com/webservice2/";
    public static final String REST_API_KEY ="YH56_IK25_LM75KJ_14KJFG";
    public static final String updateURL = REST_API_BASE_URL
            + "AndroidAPP/Telcarnet.apk";
    public static final String versionURL = REST_API_BASE_URL
            + "AndroidAPP/Version.txt";
    public static int timeout=100;
    public static String MacAddress="AC:0D:1B:3D:87:CB";
}
