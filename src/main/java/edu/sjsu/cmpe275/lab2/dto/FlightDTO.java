package edu.sjsu.cmpe275.lab2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @NoArgsConstructor
public class FlightDTO implements Serializable {

    private String flightNumber;
    private Date departureDate;
    private Date departureTime;
    private Date arrivalTime;
    private Integer price;
    private String origin;
    private String destination;
    private int seatsLeft;
    private String description;
    private String model;
    private Integer capacity;
    private String manufacturer;
    private Integer yearOfManufacture;
    private List<PassengerDTO> passengers;

}
