package edu.sjsu.cmpe275.lab2.service;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.exception.MyParseException;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
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

        Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
        if(reservation.isPresent()) {
            return Util.prepareResponse(Util.convertToDTO(reservation.get()), HttpStatus.OK, responseType);
        } else {
            return Util.prepareErrorResponse("404", "Reservation with number " + reservationNumber
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
        }
    }

    public ResponseEntity<Object> makeReservation(String passengerId, String flightNumbers, String departureDates, Boolean responseType) throws MyParseException {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(!passenger.isPresent()) {
            return Util.prepareErrorResponse("404", "Sorry, the passenger with ID " + passengerId
                    + " does not exist" , HttpStatus.NOT_FOUND, responseType);
        } else {
            List<String> flightNumbersList = Arrays.stream(flightNumbers.split(",")).collect(Collectors.toList());
            List<String> departureDatesStringList = Arrays.stream(departureDates.split(",")).collect(Collectors.toList());
            if(flightNumbersList.size() != departureDatesStringList.size()) {
                return Util.prepareErrorResponse("400", "Parameters count do not match. No. of FlightNumbers: " + flightNumbersList.size()
                        + ", No. of DepartureDates: " + departureDatesStringList.size(), HttpStatus.BAD_REQUEST, responseType);
            }
            List<Date> departureDatesList = departureDatesStringList.stream().map(d -> {
                try {
                    return Util.convertStringToDate(d);
                } catch (ParseException e) {
                    throw new MyParseException(e);
                }
            }).collect(Collectors.toList());
            List<Flight> flights = new ArrayList<>();
            for(int i=0; i < flightNumbersList.size(); i++) {
                Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumbersList.get(i),
                        departureDatesList.get(i));
                if(flight.isPresent()) {
                    flights.add(flight.get());
                } else {
                    return Util.prepareErrorResponse("404", "Sorry, the flight with number " + flightNumbersList.get(i)
                            + " does not exist", HttpStatus.NOT_FOUND, responseType);
                }
            };
            if(flights.isEmpty()) {
                return Util.prepareErrorResponse("400", "Reservation should have at least 1 valid flight.", HttpStatus.BAD_REQUEST, responseType);
            } else {
                if(flights.size() > 1 && hasOverlapConflict(flights)) {
                    return Util.prepareErrorResponse("409", "Sorry, found time overlap with given flights.", HttpStatus.CONFLICT, responseType);
                } else if(hasOverlapReservation(passenger.get(), flights)){
                    return Util.prepareErrorResponse("409", "Sorry, passenger cannot have overlap reservations.", HttpStatus.CONFLICT, responseType);
                } else {
                    String origin = flights.get(0).getOrigin();
                    String destination = flights.get(flights.size()-1).getDestination();
                    Integer price = flights.stream().map(flight -> flight.getPrice()).reduce(0, (a,b) -> a+b);

                    if(!hasSeatsLeft(flights)){
                        return Util.prepareErrorResponse("400", "Sorry, one or more flights has no seats left.", HttpStatus.BAD_REQUEST, responseType);
                    } else {
                        flights.stream().forEach(flight -> {
                            flight.setSeatsLeft(flight.getSeatsLeft() - 1);
                            flight.getPassengers().add(passenger.get());
                            flightRepository.save(flight);
                        });
                    }

                    Reservation reservation = new Reservation(passenger.get(), origin, destination, price, flights);
                    reservationRepository.save(reservation);
                    return Util.prepareResponse(Util.convertToDTO(reservation), HttpStatus.OK, responseType);
                }
            }
        }

    }

    public Boolean hasOverlapConflict(List<Flight> flights) {
        for(int i=0; i<flights.size()-1; i++) {
            Flight f1 = flights.get(i);
            Flight f2 = flights.get(i+1);
            if(validateOverlap(f1.getDepartureTime(), f1.getArrivalTime(), f2.getDepartureTime(), f2.getArrivalTime()))
                return true;
        }
        return false;
    }

    public Boolean validateOverlap(Date dt1, Date at1, Date dt2, Date at2) {
        if(dt1.compareTo(dt2) == 0 || at1.compareTo(at2) == 0 || dt1.compareTo(at2) == 0 || at1.compareTo(dt2) == 0) {
            return true;
        } else if( (at1.after(dt2) && at1.before(at2))|| (at2.after(dt1) && at2.before(at1)) ) {
            return true;
        }
        return false;
    }

    public Boolean hasOverlapReservation(Passenger passenger, List<Flight> flights) {
        if(passenger.getReservations().size() < 1) {
            return false;
        } else {
            List<Reservation> existingReservations = passenger.getReservations();
            Date dt1 = flights.get(0).getDepartureTime();
            Date at1 = flights.get(flights.size()-1).getArrivalTime();
            for(int i = 0; i < existingReservations.size(); i++) {
                List<Flight> existingflights = existingReservations.get(i).getFlights();
                if(existingflights.size() > 0) {
                    Date dt2 = existingflights.get(0).getDepartureTime();
                    Date at2 = existingflights.get(existingflights.size() - 1).getArrivalTime();
                    if (validateOverlap(dt1, at1, dt2, at2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public Boolean hasSeatsLeft(List<Flight> flights) {
        return flights.stream().allMatch(flight -> flight.getSeatsLeft() > 0 && flight.getSeatsLeft() <= flight.getPlane().getCapacity());
    }

}
