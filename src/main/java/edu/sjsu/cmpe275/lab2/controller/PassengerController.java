package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getPassenger(@PathVariable("id") String id, @RequestParam(value = "xml", required = false) Boolean responseType) {

        ResponseEntity<Object> responseEntity;
        Passenger passenger = passengerService.getPassenger(id);
        if(passenger != null) {
            responseEntity = Util.prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
        } else {
            responseEntity = Util.prepareErrorResponse("400", "Passenger not found.", HttpStatus.BAD_REQUEST, responseType);
        }
        return responseEntity;
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createPassenger(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
         @RequestParam("birthyear") Integer birthyear, @RequestParam("gender") String gender, @RequestParam("phone") String phone,
        @RequestParam(value = "xml", required = false) Boolean responseType) {

        Passenger passenger = passengerService.createPassenger(firstname, lastname, birthyear, gender, phone);
        ResponseEntity<Object> responseEntity = Util.prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
        return responseEntity;
    }


}
