package org.eega.server.common;

import org.eega.server.exceptions.InvalidRequestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.eega.server.common.Constants.GET_REQUEST_ALLOWED_MSG;
import static org.eega.server.common.Constants.INVALID_REQUEST_MSG;
import static org.eega.server.common.Constants.NO_HEADER_FOUND_MSG;
import static org.junit.Assert.*;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
public class RequestValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void validateRequestBasicInfoThrowsInvalidRequest() throws Exception {
        String []basicInfos = {};
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(INVALID_REQUEST_MSG);
        RequestValidator.validateRequestBasicInfo(basicInfos);
    }

    @Test
    public void validateRequestBasicInfoThrowsMethodNotAllowed() throws Exception {
        String []basicInfos = {"POST", "/test", "HTTP/1.1"};
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(GET_REQUEST_ALLOWED_MSG);
        RequestValidator.validateRequestBasicInfo(basicInfos);
    }

    @Test
    public void validateRequestBasicInfoNoException() throws Exception {
        String []basicInfos = {"GET", "/test", "HTTP/1.1"};
        RequestValidator.validateRequestBasicInfo(basicInfos);
    }

    @Test
    public void validateRequestDataThrowsNoHeaderFound() throws Exception {
        List<String> requestData = new ArrayList<>();
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(NO_HEADER_FOUND_MSG);
        RequestValidator.validateRequestData(requestData);
    }

    @Test
    public void validateRequestDataNoException() throws Exception {
        List<String> requestData = new ArrayList<>();
        requestData.add("GET /test HTTP/1.1");
        RequestValidator.validateRequestData(requestData);
    }

}