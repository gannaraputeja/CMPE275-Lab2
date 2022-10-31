package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti
 */

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public ResponseEntity<?> getPassenger(String id, Boolean responseType) {
        Passenger passenger = passengerRepository.findById(id).orElse(null);
        return prepareResponse(passenger, HttpStatus.OK, responseType);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> createPassenger(String firstname, String lastname, Integer birthyear, String gender, String phone, Boolean responseType) {
        Passenger passenger = null;
        try {
            passenger = new Passenger(firstname, lastname, birthyear, gender, phone);
            passengerRepository.save(passenger);
        } catch (Exception e) {
            System.out.println(e.getCause()+" **** "+e.getMessage());
            return prepareResponse(e.getCause(), HttpStatus.BAD_REQUEST, responseType);
        }
        return prepareResponse(passenger, HttpStatus.OK, responseType);
    }

    public ResponseEntity prepareResponse(Object response, HttpStatus status, Boolean responseType) {
        if(responseType != null && responseType) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity(response, responseHeaders, status);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(response, responseHeaders, status);
        }
    }

}
