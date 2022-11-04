package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public ResponseEntity<Object> getFlight(String flightNumber, Date departureDate, Boolean responseType) {
        return null;
    }

    public ResponseEntity<Object> createOrUpdateFlight(String flightNumber, Date departureDate, Integer price, String origin, String destination, Date departureTime, Date arrivalTime, String description, String capacity, String model, String manufacturer, String yearOfManufacture) {
        return null;
    }

    public ResponseEntity<Object> deleteFlight(String flightNumber, Date departureDate, Boolean responseType) {
        return null;
    }
}
