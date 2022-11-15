package edu.sjsu.cmpe275.lab2.repository;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface FlightRepository extends JpaRepository<Flight, String> {

    /**
     * Finds Flight based on FlightNumber and DepartureDate
     *
     * @param flightNumber
     * @param departureDate
     * @return Flight Entity
     */
    public Optional<Flight> findByFlightNumberAndDepartureDate(String flightNumber, Date departureDate);

    /**
     * Deletes Flight based on FlightNumber and DepartureDate
     *
     * @param flightNumber
     * @param departureDate
     */
    public void deleteByFlightNumberAndDepartureDate(String flightNumber, Date departureDate);

}
