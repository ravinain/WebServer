package org.eega.server.services;

import org.eega.server.common.Constants;
import org.eega.server.config.ApplicationProperties;
import org.eega.server.domain.Context;
import org.eega.server.domain.HttpRequest;
import org.eega.server.exceptions.InvalidRequestException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.eega.server.common.Constants.GET_REQUEST_ALLOWED_MSG;
import static org.eega.server.common.Constants.INVALID_REQUEST_MSG;
import static org.eega.server.common.Constants.NO_HEADER_FOUND_MSG;
import static org.eega.server.common.ResponseCode.BAD_REQUEST;
import static org.eega.server.common.ResponseCode.METHOD_NOT_ALLOWED;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
public class RequestResponseProcessorTest {

    @InjectMocks
    private RequestResponseProcessor requestResponseProcessor;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private Context context;

    @Mock
    private ApplicationProperties properties;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void fillHttpRequestThrowsNoHeaderFound() throws Exception {
        HttpRequest request = new HttpRequest();
        List<String> requestData = new ArrayList<>();
        Mockito.when(context.getRequest()).thenReturn(request);
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(NO_HEADER_FOUND_MSG);
        requestResponseProcessor.fillHttpRequest(requestData);
    }

    @Test
    public void fillHttpRequestInvalidRequest() throws Exception {
        HttpRequest request = new HttpRequest();
        List<String> requestData = new ArrayList<>();
        requestData.add("GET /test HTTP/1.1 INVALID_CHAR");
        Mockito.when(context.getRequest()).thenReturn(request);
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(INVALID_REQUEST_MSG);
        requestResponseProcessor.fillHttpRequest(requestData);
    }

    @Test
    public void fillHttpRequestMethodNotAllowed() throws Exception {
        HttpRequest request = new HttpRequest();
        List<String> requestData = new ArrayList<>();
        requestData.add("POST /test HTTP/1.1");
        Mockito.when(context.getRequest()).thenReturn(request);
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(GET_REQUEST_ALLOWED_MSG);
        requestResponseProcessor.fillHttpRequest(requestData);
    }

    @Test
    public void fillHttpRequest() throws Exception {
        HttpRequest request = new HttpRequest();
        List<String> requestData = getMockedRequestData();
        Mockito.when(context.getRequest()).thenReturn(request);
        Mockito.when(properties.getCharacterEncoding()).thenReturn("UTF-8");
        requestResponseProcessor.fillHttpRequest(requestData);

        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getResource(), equalTo("/abc"));
        assertThat(request.getProtocol(), equalTo("HTTP/1.1"));
        assertThat(request.getHeaders().size(), is(3));
        assertThat(request.getHeaders().get("Host"), equalTo("localhost:9090"));
    }

    @Test
    public void prepareRequestBasicInfoWithoutRequestParameter() throws Exception {
        HttpRequest request = new HttpRequest();
        String basicInfo = "GET /test HTTP/1.1";
        Mockito.when(properties.getCharacterEncoding()).thenReturn("UTF-8");
        requestResponseProcessor.prepareRequestBasicInfo(basicInfo, request);
        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getResource(), equalTo("/test"));
        assertThat(request.getProtocol(), equalTo("HTTP/1.1"));
        assertThat(request.getHeaders().size(), is(0));
        assertThat(request.getRequestParameters().size(), is(0));
    }

    @Test
    public void prepareRequestBasicInfoWithRequestParameter() throws Exception{
        HttpRequest request = new HttpRequest();
        String basicInfo = "GET /test?a=b;c=d HTTP/1.1";
        Mockito.when(properties.getCharacterEncoding()).thenReturn("UTF-8");
        requestResponseProcessor.prepareRequestBasicInfo(basicInfo, request);
        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getResource(), equalTo("/test"));
        assertThat(request.getProtocol(), equalTo("HTTP/1.1"));
        assertThat(request.getHeaders().size(), is(0));
        assertThat(request.getRequestParameters().size(), is(2));
    }

    @Test
    public void prepareRequestBasicInfoWithInvalidRequest() throws Exception{
        HttpRequest request = new HttpRequest();
        String basicInfo = "GET /test?a=b;c=d";
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(Constants.INVALID_REQUEST_MSG);
        requestResponseProcessor.prepareRequestBasicInfo(basicInfo, request);
    }

    @Test
    public void prepareRequestBasicInfoWithInvalidMethod() throws Exception{
        HttpRequest request = new HttpRequest();
        String basicInfo = "POST /test?a=b;c=d HTTP/1.1";
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage(Constants.GET_REQUEST_ALLOWED_MSG);
        requestResponseProcessor.prepareRequestBasicInfo(basicInfo, request);
    }

    private List<String> getMockedRequestData() {
        List<String> mockRequestData = new ArrayList<>();
        mockRequestData.add("GET /abc HTTP/1.1");
        mockRequestData.add("Host: localhost:9090");
        mockRequestData.add("Keep-Alive: 300");
        mockRequestData.add("Pragma: no-cache");

        return mockRequestData;
    }
}