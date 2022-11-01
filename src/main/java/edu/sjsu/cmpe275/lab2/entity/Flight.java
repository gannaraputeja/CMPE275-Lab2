package edu.sjsu.cmpe275.lab2.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Data
@Entity
public class Flight {

    @EmbeddedId
    private FlightId id;
    /*  Date format: yy-mm-dd-hh, do not include minutes or seconds.
    ** Example: 2017-03-22-19
    */
    private Date departureTime; // Must be within the same calendar day as departureDate.   
    private Date arrivalTime;
    private Integer price;    // Full form only
    private String origin;
    private String destination;  
    private int seatsLeft; 
    private String description;   // Full form only
    @Embedded
    private Plane plane;  // Embedded,    Full form only
    @ManyToMany()
    @JoinTable(
            name="Flight_Passenger",
            joinColumns = {@JoinColumn(name = "flight_number", referencedColumnName = "flight_number")
                    , @JoinColumn(name = "departure_date", referencedColumnName = "departure_date")},
            inverseJoinColumns = {@JoinColumn(name = "passenger_id", referencedColumnName = "id")}
    )
    private List<Passenger> passengers;    // Full form only

}
