package edu.sjsu.cmpe275.lab2.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private int price;    // Full form only
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

@Data
@Embeddable
class FlightId implements Serializable {
    @Column(name = "flight_number")
    private String flightNumber; // part of the primary key

    /*  Date format: yy-mm-dd, do not include hours, minutes, or seconds.
     ** Example: 2022-03-22
     **The system only needs to support PST. You can ignore other time zones.
     */
    @Column(name = "departure_date")
    private Date departureDate; //  serve as the primary key together with flightNumber
}
