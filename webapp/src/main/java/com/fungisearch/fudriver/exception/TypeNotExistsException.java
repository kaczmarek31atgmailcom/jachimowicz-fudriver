package com.fungisearch.fudriver.exception;

/**
 * Created by marcin on 09.05.16.
 */
public class TypeNotExistsException extends RuntimeException {
    public TypeNotExistsException(){
        super("Podany rodzaj nie istnieje!");
    }
}
