package org.eega.server.services;

import org.eega.server.common.*;
import org.eega.server.config.ApplicationProperties;
import org.eega.server.domain.Context;
import org.eega.server.domain.HttpRequest;
import org.eega.server.domain.HttpResponse;
import org.eega.server.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.eega.server.common.Constants.*;
import static org.eega.server.common.ResponseCode.*;

/**
 * Created by Ravi Nain on 6/10/2017.
 * Request and Response Processor service
 */
@Service
public class RequestResponseProcessor {

    @Autowired
    private Context context;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationProperties properties;

    private static final Logger LOGGER = Logger.getLogger(RequestResponseProcessor.class.getName());

    /**
     * Process http request
     * @param reader BufferedReader
     * @throws IOException
     * @throws InvalidRequestException
     */
    public void processRequest(final BufferedReader reader) throws IOException, InvalidRequestException {
        LOGGER.entering(this.getClass().getName(), "processRequest");
        resetContext();
        List<String> requestData = convertRequestDataToList(reader);
        fillHttpRequest(requestData);
        fillHttpResponse();
        LOGGER.exiting(this.getClass().getName(), "processRequest");
    }

    /**
     * Fill Http Request object from input request data.
     * @param requestData
     * @throws InvalidRequestException
     * @throws UnsupportedEncodingException
     */
    public void fillHttpRequest(final List<String> requestData) throws InvalidRequestException, UnsupportedEncodingException {
        HttpRequest request = context.getRequest();
        RequestValidator.validateRequestData(requestData);
        prepareRequestBasicInfo(requestData.get(0), request);
        Map<String, String> headers = requestData.stream()
                .filter(data -> data.contains(HEADER_SEPARATOR))
                .collect(Collectors.toMap(entry -> entry.split(HEADER_SEPARATOR)[0]
                        , entry -> entry.split(HEADER_SEPARATOR)[1]));
        request.setHeaders(headers);
    }

    /**
     * Prepare request basic info
     * @param basicInfo
     * @param request
     * @throws InvalidRequestException
     * @throws UnsupportedEncodingException
     */
    public void prepareRequestBasicInfo(final String basicInfo, final HttpRequest request) throws InvalidRequestException, UnsupportedEncodingException {
        final String[] infoA = basicInfo.split(SPACE);
        RequestValidator.validateRequestBasicInfo(infoA);

        request.setMethod(infoA[0]);

        if (infoA[1].contains(RESOURCE_AND_PARAMETER_SEP)) {
            String[] resourceInfoA = infoA[1].split(RESOURCE_AND_PARAMETER_SEP_EXP);
            Map<String, String> parameters = Arrays.stream(resourceInfoA[1].split(PARAMETER_SEP))
                    .filter(data -> data.contains(KEY_VALUE_SEP))
                    .collect(Collectors.toMap(entry -> entry.split(KEY_VALUE_SEP)[0]
                            , entry -> entry.split(KEY_VALUE_SEP)[1]));

            request.setResource(URLDecoder.decode(resourceInfoA[0], properties.getCharacterEncoding()).toString());
            request.setRequestParameters(parameters);
        } else {
            request.setResource(URLDecoder.decode(infoA[1], properties.getCharacterEncoding()).toString());
        }
        request.setProtocol(infoA[2]);
    }


    /**
     * Fill Http response
     * @throws IOException
     * @throws InvalidRequestException
     */
    public void fillHttpResponse() throws IOException, InvalidRequestException {
        HttpRequest request = context.getRequest();
        RequestValidator.validateResource(properties.getRootDirectory() + request.getResource());
        String responseBody = FileUtils.isRegularFile(properties.getRootDirectory() + request.getResource())
                ? ResponseTemplate.responseBodyForFileContent(request, properties.getRootDirectory() + request.getResource())
                : ResponseTemplate.responseBodyForListNames(request, properties.getRootDirectory() + request.getResource());
        HttpResponse response = context.getResponse();
        response.setResponseBody(responseBody);
        response.setProtocol(HTTP_PROTOCOL);
        response.setStatus(OK);
        response.setHeaders(getDefaultResponseHeaders());
    }

    /**
     * Send response back to client.
     * @param out
     * @throws IOException
     */
    public void sendResponse(final BufferedWriter out) throws IOException {
        out.write(getResponse(new HashMap<>()));
    }

    /**
     * Fetch response.
     * @param headers
     * @return
     */
    public String getResponse(final Map<String, String> headers) {
        HttpResponse httpResponse = context.getResponse();
        return ResponseTemplate.completeResponse(httpResponse, headers);
    }

    private void resetContext() {
        context.setRequest(applicationContext.getBean(HttpRequest.class));
        context.setResponse(applicationContext.getBean(HttpResponse.class));
    }

    /**
     * Default response headers.
     * @return
     */
    private Map<String, String> getDefaultResponseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Transfer-Encoding", "chunked");
        headers.put("Date", new Date().toString());
        headers.put("Server", "EEGA 1.0");
        headers.put("Connection", "close");
        headers.put("Pragma", "public");
        headers.put("Expires", new Date().toString());
        headers.put("Cache-Control", "max-age=3600, public");
        headers.put("Content-Type", "text/html; charset=UTF-8");
        headers.put("Last-Modified", new Date().toString());

        return headers;
    }

    private List<String> convertRequestDataToList(final BufferedReader reader) throws IOException {
        List<String> requestData = new ArrayList<>();
        String line = reader.readLine();
        while (!line.isEmpty()) {
            requestData.add(line);
            line = reader.readLine();
        }
        return requestData;
    }

    public void sendErrorResponse(final BufferedWriter out, final ResponseCode responseCode) {
        HttpResponse httpResponse = context.getResponse();
        StringBuilder response = new StringBuilder();
        response.append(String.format("%s %d %s\r\n", HTTP_PROTOCOL, responseCode.getCode(), responseCode.name()));

        httpResponse.getHeaders().forEach((k, v) ->
                response.append(k).append(HEADER_SEPARATOR).append(v).append(NEXT_LINE));

        response.append(NEXT_LINE).append(httpResponse.getResponseBody());
        try {
            out.write(response.toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
