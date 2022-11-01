package edu.sjsu.cmpe275.lab2.repository;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.FlightId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface FlightRepository extends JpaRepository<Flight, FlightId> {

}
