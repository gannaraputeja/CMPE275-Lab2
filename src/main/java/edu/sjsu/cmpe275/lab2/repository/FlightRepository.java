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

    public Optional<Flight> findByFlightNumberAndDepartureDate(String flightNumber, Date departureDate);

    public void deleteByFlightNumberAndDepartureDate(String flightNumber, Date departureDate);

//    @Query("SELECT * FROM Flight where (flightNumber, departure_date) IN " +
//            "   (SELECT flight_number,departure_date FROM Reservation_Flight WHERE reservation_number = ?1 )")
//    public List<Flight> findBy(String reservationNumber);

}

//
//select * from flight where (flight_number, departure_date) in
//        (select flight_number, departure_date from Reservation_Flight where reservation_number = 'd531001d-cf0c-4245-99cb-268b366bf2d3')


//@Query("SELECT u FROM User u WHERE u.status = ?1")
//User findUserByStatus(Integer status);
//
//@Query("SELECT u FROM User u WHERE u.status = ?1 and u.name = ?2")
//User findUserByStatusAndName(Integer status, String name);
