package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.exception.MyParseException;
import edu.sjsu.cmpe275.lab2.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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

    /**
     * Get a Reservation
     *
     * @param reservationNumber
     * @param responseType
     * @return Reservation Detials
     */
    @GetMapping(value = "/{reservationNumber}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getReservation(@PathVariable("reservationNumber") String reservationNumber,
             @RequestParam(value = "xml", required = false) Boolean responseType) {
        return reservationService.getReservation(reservationNumber, responseType);
    }

    /**
     * Make a Reservation
     *
     * @param passengerId
     * @param flightNumbers
     * @param departureDates
     * @param responseType
     * @return Reservation Details
     * @throws MyParseException
     */
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> makeReservation(@RequestParam("passengerId") String passengerId,
          @RequestParam("flightNumbers") String flightNumbers,
          @RequestParam("departureDates") String departureDates,
          @RequestParam(value = "xml", required = false) Boolean responseType) throws MyParseException {
        return reservationService.makeReservation(passengerId, flightNumbers, departureDates, responseType);
    }

    /**
     * Update a Reservation
     *
     * @param reservationNumber
     * @param flightsAdded
     * @param flightsRemoved
     * @param departureDatesAdded
     * @param departureDatesRemoved
     * @param responseType
     * @return Reservation Details
     */
    @PostMapping(value = "/{number}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    public ResponseEntity<Object> UpdateReservation(
        @PathVariable("number") String reservationNumber,
        @RequestParam(value = "flightsAdded", required = false) String flightsAdded,
        @RequestParam(value = "flightsRemoved", required = false) String flightsRemoved,
        @RequestParam(value = "departureDatesAdded", required = false) String departureDatesAdded,
        @RequestParam(value = "departureDatesRemoved", required = false) String departureDatesRemoved,
        @RequestParam(value= "xml", required = false) Boolean responseType
        ) throws ParseException {
            return reservationService.updateReservation(reservationNumber, flightsAdded, flightsRemoved, departureDatesAdded, departureDatesRemoved, responseType);
        }

        /**
         * Delete a Reservation
         *
         * @param reservationNumber
         * @param responseType
         * @return Success/Error Response
         */
        @DeleteMapping(value = "/{number}",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
        public ResponseEntity<Object> deleteReservationById(
                @PathVariable(value="number") String reservationNumber,
                @RequestParam(value = "xml") boolean responseType)
        {
            return reservationService.deleteReservationById(reservationNumber, responseType);
        }

}
