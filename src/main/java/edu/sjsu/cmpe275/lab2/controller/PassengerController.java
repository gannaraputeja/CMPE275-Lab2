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
import java.util.Optional;
import java.lang.annotation.Repeatable;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
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
        Optional<Passenger> passenger = passengerService.getPassenger(id);
        if(passenger.isPresent()) {
            responseEntity = Util.prepareResponse(Util.convertToDTO(passenger.get()), HttpStatus.OK, responseType);
        } else {
            responseEntity = Util.prepareErrorResponse("404", "Sorry, the requested passenger with ID " + id
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
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

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> updatePassenger(
            @PathVariable("id") String id,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("birthyear") Integer birthyear,
            @RequestParam("gender") String gender,
            @RequestParam("phone") String phone,
            @RequestParam(value = "xml", required = false) Boolean responseType){
        return passengerService.updatePassenger(id,firstname,lastname,birthyear,gender,phone,responseType);
    }

    @DeleteMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> deletePassenger(
            @PathVariable("id") String id,
            @RequestParam(value = "xml", required = false) Boolean responseType){
        return passengerService.deletePassenger(id,responseType);
    }
}
