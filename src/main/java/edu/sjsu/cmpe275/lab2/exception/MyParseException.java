package edu.sjsu.cmpe275.lab2.exception;

import lombok.NoArgsConstructor;

import java.text.ParseException;

/**
 * This is a Custom ParseException
 */
@NoArgsConstructor
public class MyParseException extends RuntimeException {

    public MyParseException(ParseException e) {
        super(e);
    }

}
