package edu.sjsu.cmpe275.lab2.repository;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface FlightRepository extends JpaRepository<Flight, String> {

    public Optional<Flight> findByFlightNumber(String flightNumber);

    public Optional<Flight> findByFlightNumberAndDepartureDate(String flightNumber, Date departureDate);

}
