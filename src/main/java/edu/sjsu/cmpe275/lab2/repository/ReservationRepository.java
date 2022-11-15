package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    /**
     * Finds Reservation based on Passenger
     *
     * @param passenger
     * @return Reservation Entity
     */
    List<Reservation> findByPassenger(Passenger passenger);

}