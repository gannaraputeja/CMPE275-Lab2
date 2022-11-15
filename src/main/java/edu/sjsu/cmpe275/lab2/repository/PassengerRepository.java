package edu.sjsu.cmpe275.lab2.repository;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

public interface PassengerRepository extends JpaRepository<Passenger, String> {

    /**
     * Finds Passenger based on Phone
     *
     * @param phone
     * @return Passenger
     */
    Passenger findByPhone(String phone);
}
