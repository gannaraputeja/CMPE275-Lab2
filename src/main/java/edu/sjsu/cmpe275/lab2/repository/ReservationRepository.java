package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface ReservationRepository extends JpaRepository<Reservation, String> {

}
