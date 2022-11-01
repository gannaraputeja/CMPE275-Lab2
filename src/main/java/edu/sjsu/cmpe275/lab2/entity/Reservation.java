package edu.sjsu.cmpe275.lab2.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti
 */

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "reservation_number")
    private String reservationNumber; // primary key
    @ManyToOne()
    @JoinColumn(name="passengerId")
    private Passenger passenger;     // Full form only
    private String origin;
    private String destination;  
    private Integer price; // sum of each flight’s price.   // Full form only
    @ManyToMany()
    @JoinTable(
            name = "Reservation_Flight",
            joinColumns = {@JoinColumn(name = "reservation_number", referencedColumnName = "reservation_number")},
            inverseJoinColumns = {@JoinColumn(name = "flight_number", referencedColumnName = "flight_number")
                    , @JoinColumn(name = "departure_date", referencedColumnName = "departure_date")}
    )
    private List<Flight> flights;    // Full form only, CANNOT be empty, ordered chronologically by departureTime

}
