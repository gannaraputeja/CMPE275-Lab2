package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public ResponseEntity<Object> getFlight(String flightNumber, Date departureDate, Boolean responseType) {
        Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate);
        if (!flight.isPresent()) {
            String code = "Sorry, the requested flight with number " + flightNumber + " does not exist";
            return Util.prepareErrorResponse("404", code, HttpStatus.NOT_FOUND, responseType);
        }

        return Util.prepareResponse(Util.convertToDTO(flight.get()), HttpStatus.OK, responseType);
    }

    public ResponseEntity<Object> createOrUpdateFlight(String flightNumber, Date departureDate, Integer price, String origin,
                                                       String destination, Date departureTime, Date arrivalTime, String description,
                                                       Integer capacity, String model, String manufacturer, Integer yearOfManufacture, Boolean responseType) {
        Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate);
        Plane plane = new Plane(model, capacity, manufacturer, yearOfManufacture);

        if (!flight.isPresent()) {
            try {
                Flight newFlight = new Flight(flightNumber, departureDate, departureTime, arrivalTime, price,
                        origin, destination, capacity, description, plane, new ArrayList<>());
                flightRepository.save(newFlight);
                return Util.prepareResponse(Util.convertToDTO(newFlight), HttpStatus.OK, responseType);
            } catch (Exception e) {
                return Util.prepareErrorResponse("400", "New Flight creation failed", HttpStatus.BAD_REQUEST, responseType);
            }
        } else {
            try {
                Integer totalBookings = flight.get().getPlane().getCapacity() - flight.get().getSeatsLeft();
                if (totalBookings > capacity) {
                    return Util.prepareErrorResponse("400", "Flight Capacity cannot be lower than the number of active reservations",
                            HttpStatus.BAD_REQUEST, responseType);
                } else {
                    flight.get().setSeatsLeft(capacity - totalBookings);
                }

                for (Passenger passenger : flight.get().getPassengers()) {
                    if (checkFlightTimeConflict(passenger, departureTime, arrivalTime)) {
                        return Util.prepareErrorResponse("400", "Updating flight departure and/or arrival time " +
                                "causing one or more passengers to have overlapping flight time", HttpStatus.BAD_REQUEST, responseType);
                    }
                }

                flight.get().setFlightNumber(flightNumber);
                flight.get().setDepartureDate(departureDate);
                flight.get().setPrice(price);
                flight.get().setOrigin(origin);
                flight.get().setDestination(destination);
                flight.get().setDepartureTime(departureTime);
                flight.get().setArrivalTime(arrivalTime);
                flight.get().setDescription(description);
                flight.get().setPlane(plane);
                flightRepository.save(flight.get());
                return Util.prepareResponse(Util.convertToDTO(flight.get()), HttpStatus.OK, responseType);
            } catch (Exception e) {
                System.out.println(e.toString());
                return Util.prepareErrorResponse("400", "Exception while updating the flight details", HttpStatus.BAD_REQUEST, responseType);
            }
        }
    }

    public ResponseEntity<Object> deleteFlight(String flightNumber, Date departureDate, Boolean responseType) {
        Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate);
        if (!flight.isPresent()) {
            return Util.prepareErrorResponse("404", "Flight with the given flight number and departure date does not exists",
                    HttpStatus.NOT_FOUND, responseType);
        }

        if(flight.get().getPassengers().size()>0)
		{
            return Util.prepareErrorResponse("404", "The Flight Number"+ flightNumber +" cannot be deleted since it has ome or more reservations",
            HttpStatus.NOT_FOUND, responseType);        
        }
        		
		flightRepository.deleteByFlightNumberAndDepartureDate(flightNumber,departureDate);
        return Util.prepareResponse(new Success("200","Deleted flight successfully"), HttpStatus.OK, responseType);
    }

    private Boolean checkFlightTimeConflict(Passenger passenger, Date departureTime, Date arrivalTime) {
        List<Reservation> reservations = reservationRepository.findByPassenger(passenger);

        for (Reservation reservation : reservations) {
            for (Flight flight : reservation.getFlights()) {
                if (departureTime.getTime() >= flight.getDepartureTime().getTime() && departureTime.getTime() <= flight.getArrivalTime().getTime()) {
                    return true;
                }

                if (arrivalTime.getTime() >= flight.getDepartureTime().getTime() && arrivalTime.getTime() <= flight.getArrivalTime().getTime()) {
                    return true;
                }
            }
        }
        return false;
    }
}
