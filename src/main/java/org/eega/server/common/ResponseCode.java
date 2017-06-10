package org.eega.server.common;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
public enum ResponseCode {

    OK(200), BAD_REQUEST(400), NOT_FOUND(404), BAD_GATEWAY(502), METHOD_NOT_ALLOWED(405);

    private int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
