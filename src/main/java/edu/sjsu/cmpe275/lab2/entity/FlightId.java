package edu.sjsu.cmpe275.lab2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data @NoArgsConstructor
public class FlightId implements Serializable {
    @Column(name = "flight_number")
    private String flightNumber; // part of the primary key

    /*  Date format: yy-mm-dd, do not include hours, minutes, or seconds.
     ** Example: 2022-03-22
     **The system only needs to support PST. You can ignore other time zones.
     */
    @Column(name = "departure_date")
    private Date departureDate; //  serve as the primary key together with flightNumber
}