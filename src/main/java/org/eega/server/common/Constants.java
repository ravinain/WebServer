package org.eega.server.common;

/**
 * Created by Ravi Nain on 6/10/2017.
 * All Common constants.
 */

public final class Constants {
    /**
     * Utility class doesn't need to be instantiated.
     */
    private Constants() {}

    public static final String SPACE = " ";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String NO_HEADER_FOUND_MSG = "Invalid Request, no headers found!";
    public static final String INVALID_REQUEST_MSG = "Invalid Request!";
    public static final String RESOURCE_NOT_FOUND = "Resource does not found";
    public static final String RESOURCE_AND_PARAMETER_SEP = "?";
    public static final String RESOURCE_AND_PARAMETER_SEP_EXP = "\\?";
    public static final String KEY_VALUE_SEP = "=";
    public static final String PARAMETER_SEP = ";";
    public static final String GET_REQUEST_ALLOWED_MSG = "Only GET requests are allowed!";
    public static final String NEXT_LINE = "\r\n";
    public static final String HTTP_PROTOCOL = "HTTP/1.0";
}
