package org.eega.server.common;

import org.eega.server.domain.HttpRequest;
import org.eega.server.domain.HttpResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Response template for different type of request.
 */
public class ResponseTemplate {

    /**
     * Template to prepare response for downloading file content.
     * @param request HttpRequest
     * @param filePath Input resource path
     * @return response data
     * @throws IOException
     */
    public static String responseBodyForFileContent(final HttpRequest request, final String filePath) throws IOException {
        ResponseBuilder responseBuilder = new ResponseBuilder(request);
        return responseBuilder.appendFileContent(filePath).build();
    }

    /**
     * Template to prepare response for showing file list.
     * @param request HttpRequest
     * @param filePath Input file resource location.
     * @return this
     * @throws IOException
     */
    public static String responseBodyForListNames(final HttpRequest request, final String filePath) throws IOException {
        ResponseBuilder responseBuilder = new ResponseBuilder(request);
        return responseBuilder.appendBodyHeader()
                .appendParentInList().appendDirectories(filePath)
                .appendFiles(filePath).appendFooter().build();
    }

    /**
     * Generate complete response including basic info, header and respone body.
     * @param response HttpResponse
     * @param headers headers
     * @return complete response.
     */
    public static String completeResponse(final HttpResponse response, final Map<String, String> headers) {
        ResponseBuilder responseBuilder = new ResponseBuilder(response);
        return responseBuilder.appendBasicInfo()
                .appendDefaultHeaders(headers)
                .appendUserHeaders(headers)
                .appendResponseBody().build();
    }


}
