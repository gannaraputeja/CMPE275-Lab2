package edu.sjsu.cmpe275.lab2.service;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;

    public ResponseEntity<Object> getReservation(String reservationNumber, Boolean responseType) {
        ResponseEntity<Object> responseEntity;
        Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
        if(reservation.isPresent()) {
            responseEntity = Util.prepareResponse(Util.convertToDTO(reservation.get()), HttpStatus.OK, responseType);
        } else {
            responseEntity = Util.prepareErrorResponse("404", "Reservation with number " + reservationNumber
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
        }
        return responseEntity;
    }

    public ResponseEntity<Object> makeReservation(String passengerId, String flightNumbers, Boolean responseType) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(passenger.isPresent()) {
            List<String> fnumbers = Arrays.stream(flightNumbers.split(",")).collect(Collectors.toList());
            List<Flight> flights = new ArrayList<>();
            fnumbers.stream().forEach(flightNumber -> {
                Optional<Flight> flight = flightRepository.findByFlightNumber(flightNumber);
                if(flight.isPresent())
                    flights.add(flight.get());
            });
            if(flights.isEmpty()) {
                return Util.prepareErrorResponse("400", "Reservation should have at least 1 valid flight.", HttpStatus.BAD_REQUEST, responseType);
            } else {
                String origin = flights.get(0).getOrigin();
                String destination = flights.get(flights.size()-1).getDestination();
                Integer price = flights.stream().map(flight -> flight.getPrice()).reduce(0, (a,b) -> a+b);
                Reservation reservation = new Reservation(passenger.get(), origin, destination, price, flights);
                reservationRepository.save(reservation);
                return Util.prepareResponse(Util.convertToDTO(reservation), HttpStatus.OK, responseType);
            }
        } else {
            return Util.prepareErrorResponse("404", "Sorry, the passenger with ID " + passengerId
                    + " does not exist" , HttpStatus.NOT_FOUND, responseType);
        }

    }

}
