package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

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
        ResponseEntity<Object> responseEntity;
        Optional<Reservation> reservation = reservationService.getReservation(reservationNumber);
        if(reservation.isPresent()) {
            responseEntity = Util.prepareResponse(Util.convertToDTO(reservation.get()), HttpStatus.OK, responseType);
        } else {
            responseEntity = Util.prepareErrorResponse("404", "Reservation with number " + reservationNumber
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
        }
        return responseEntity;
    }

}
