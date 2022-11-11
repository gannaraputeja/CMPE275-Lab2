package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.exception.MyParseException;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
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
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "/{reservationNumber}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getReservation(@PathVariable("reservationNumber") String reservationNumber,
             @RequestParam(value = "xml", required = false) Boolean responseType) {
        return reservationService.getReservation(reservationNumber, responseType);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> makeReservation(@RequestParam("passengerId") String passengerId,
          @RequestParam("flightNumbers") String flightNumbers,
          @RequestParam("departureDates") String departureDates,
          @RequestParam(value = "xml", required = false) Boolean responseType) throws MyParseException {
        return reservationService.makeReservation(passengerId, flightNumbers, departureDates, responseType);
    }

    @PostMapping(value = "/{number}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    public ResponseEntity<Object> UpdateReservation(
        @PathVariable("number") String reservationNumber,
        @RequestParam(value = "flightsAdded", required = false) String flightsAdded,
        @RequestParam(value = "flightsRemoved", required = false) String flightsRemoved,
        @RequestParam(value = "departureDate", required = false) String departureDate,
        @RequestParam(value= "xml", required = false) Boolean responseType
        ) {
            return reservationService.updateReservation(reservationNumber, flightsAdded, flightsRemoved, departureDate, responseType);
        }
}
