package edu.sjsu.cmpe275.lab2.advice;

import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {

        return Util.prepareErrorResponse("400", e.getMessage(),  HttpStatus.BAD_REQUEST,Boolean.valueOf(request.getParameter("xml")));
    }

}
