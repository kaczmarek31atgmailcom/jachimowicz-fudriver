package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 20.03.16.
 */
public class AnotherActiveTrolleyException extends RuntimeException {
    public AnotherActiveTrolleyException(){
        super("Can not activate trolley while another of the same type is already active.");
    }
}
