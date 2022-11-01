package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.dto.PassengerDTO;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti
 */

@Transactional
@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getPassenger(@PathVariable("id") String id, @RequestParam(value = "xml", required = false) Boolean responseType) {

        Passenger passenger = passengerService.getPassenger(id);

        ResponseEntity<PassengerDTO> responseEntity = prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
        //System.out.println(responseEntity);
        return responseEntity;
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createPassenger(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
         @RequestParam("birthyear") Integer birthyear, @RequestParam("gender") String gender, @RequestParam("phone") String phone,
        @RequestParam(value = "xml", required = false) Boolean responseType) {

        Passenger passenger = passengerService.createPassenger(firstname, lastname, birthyear, gender, phone);
        ResponseEntity<PassengerDTO> responseEntity = prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
        return responseEntity;
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
