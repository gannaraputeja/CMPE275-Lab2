package edu.sjsu.cmpe275.lab2.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Data @NoArgsConstructor
@Entity
@IdClass(FlightId.class)
public class Flight {

    @Column(name = "flight_number")
    @Id
    private String flightNumber; // part of the primary key
    /*  Date format: yy-mm-dd, do not include hours, minutes, or seconds.
     ** Example: 2022-03-22
     **The system only needs to support PST. You can ignore other time zones.
     */
    @Id
    @Column(name = "departure_date")
    private Date departureDate; //  serve as the primary key together with flightNumber
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
    @JsonIgnore
    private List<Passenger> passengers;    // Full form only

    /*@ManyToMany(mappedBy = "flights")
    private List<Reservation> reservationList;*/


    public Flight(String flightNumber, Date departureDate, Date departureTime, Date arrivalTime, Integer price,
                  String origin, String destination, Integer capacity, String description, Plane plane, List<Passenger> passengers) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.origin = origin;
        this.destination = destination;
        this.description = description;
        this.plane = plane;
        this.seatsLeft = capacity;
        this.passengers = passengers;
    }
}
