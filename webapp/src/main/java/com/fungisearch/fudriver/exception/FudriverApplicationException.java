package com.fungisearch.fudriver.exception;

import com.fungisearch.fudriver.common.ErrorCode;

/**
 * Created by marcin on 27.12.15.
 */
public class FudriverApplicationException extends RuntimeException{

    private ErrorCode errorCode;

    public FudriverApplicationException(){

    }

    public FudriverApplicationException(String message){
        super(message);
    }

    public FudriverApplicationException(ErrorCode code){
        this.errorCode = code;
    }

    public FudriverApplicationException(String message, ErrorCode code){
        super(message);
        this.errorCode = code;
    }

    public FudriverApplicationException(String message, Throwable cause){
        super(message,cause);
    }

    public FudriverApplicationException(ErrorCode code, String message, Throwable cause){
        super(message,cause);
        this.errorCode = code;
    }

    public FudriverApplicationException(Throwable cause){
        super(cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
