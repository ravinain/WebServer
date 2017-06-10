package org.eega.server.exceptions;

import org.eega.server.common.ResponseCode;

/**
 * Created by Ravi Nain on 6/10/2017.
 */
public class InvalidRequestException extends Exception{

    private ResponseCode code;

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(final ResponseCode code, final String message) {
        super(message);
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }
}
