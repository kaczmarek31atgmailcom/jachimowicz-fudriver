package com.fungisearch.fudriver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


public class StringConverter {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public  String convertToISO(String toBeConverted)  {
        if(toBeConverted == null){
            return "";

        }
        try {
            toBeConverted =  new String(toBeConverted.getBytes("LATIN1"), "ISO-8859-2");
        } catch (UnsupportedEncodingException e) {
            logger.debug("Encoding exception " + e.getMessage());
        }
        return toBeConverted;
    }


    public  String convertToLatin(String toBeConverted)  {
        if(toBeConverted == null){
            return "";
        }
        try {
            toBeConverted =  new String(toBeConverted.getBytes("ISO-8859-2"), "LATIN1");
        } catch (UnsupportedEncodingException e) {
            logger.debug("Encoding exception " + e.getMessage());
        }
        return toBeConverted;
    }


}