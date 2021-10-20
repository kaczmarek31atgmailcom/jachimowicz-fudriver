package com.fungisearch.fudriver.exception;

import com.fungisearch.fudriver.common.ErrorCode;

/**
 * Created by marcin on 27.12.15.
 */
public class DataNotFoundException extends FudriverApplicationException {
    public DataNotFoundException(){}
    public DataNotFoundException(ErrorCode code){
        super(code);
    }
    public DataNotFoundException(ErrorCode code, String message){
        super(message, code);
    }

}


