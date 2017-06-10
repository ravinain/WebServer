package org.eega.server.common;

import org.eega.server.exceptions.InvalidRequestException;

import java.io.File;
import java.util.List;

import static org.eega.server.common.Constants.NO_HEADER_FOUND_MSG;
import static org.eega.server.common.Constants.RESOURCE_NOT_FOUND;
import static org.eega.server.common.ResponseCode.BAD_REQUEST;
import static org.eega.server.common.ResponseCode.NOT_FOUND;

/**
 * Created by Ravi Nain on 6/10/2017.
 * All request validators.
 */
public final class RequestValidator {

    /**
     * Utility class doesn't need to instantiated.
     */
    private RequestValidator() {}

    /**
     * Validate request basic info, which is first line of request.
     * @param basicInfoA Basic info.
     * @throws InvalidRequestException If invalid info then throws an exception.
     */
    public static void validateRequestBasicInfo(final String []basicInfoA) throws InvalidRequestException {
        if (basicInfoA.length != 3) {
            throw new InvalidRequestException(ResponseCode.BAD_REQUEST, Constants.INVALID_REQUEST_MSG);
        }
        if (!basicInfoA[0].equals(RequestMethod.GET.name())) {
            throw new InvalidRequestException(ResponseCode.METHOD_NOT_ALLOWED, Constants.GET_REQUEST_ALLOWED_MSG);
        }
    }

    /**
     * Validate input request data. It shouldn't be empty.
     * @param requestData Input request data.
     * @throws InvalidRequestException If data is empty then throws and exception.
     */
    public static void validateRequestData(final List<String> requestData) throws InvalidRequestException {
        if (requestData.size() <= 0) {
            throw new InvalidRequestException(BAD_REQUEST, NO_HEADER_FOUND_MSG);
        }
    }

    /**
     * Validate whether input requested resource exists or not.
     * @param resourcePath Input resource path.
     * @throws InvalidRequestException If resource doesn't exists then throws an exception.
     */
    public static void validateResource(final String resourcePath) throws InvalidRequestException {
        if(!FileUtils.isFileExists(resourcePath)) {
            throw new InvalidRequestException(NOT_FOUND, RESOURCE_NOT_FOUND);
        }
    }
}
