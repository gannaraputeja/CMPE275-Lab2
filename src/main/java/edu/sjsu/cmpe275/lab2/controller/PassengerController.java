package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

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

        return passengerService.getPassenger(id, responseType);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createPassenger(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
         @RequestParam("birthyear") Integer birthyear, @RequestParam("gender") String gender, @RequestParam("phone") String phone,
        @RequestParam(value = "xml", required = false) Boolean responseType) {

        return passengerService.createPassenger(firstname, lastname, birthyear, gender, phone, responseType);
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
