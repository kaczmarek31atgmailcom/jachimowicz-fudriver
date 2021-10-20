package com.fungisearch.fudriver.reclassification.command.exception;

import com.fungisearch.fudriver.reclassification.command.model.ReclassificationSkup;

/**
 * Created by marcin on 18.02.16.
 */
public class ReclassificationProcessedException extends Exception {

    public ReclassificationProcessedException(String message){
        super(message);
    }
}
