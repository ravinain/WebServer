package org.eega.server.domain;

import org.springframework.stereotype.Component;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
@Component
public class Context {

    private HttpRequest request;
    private HttpResponse response;

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

}
