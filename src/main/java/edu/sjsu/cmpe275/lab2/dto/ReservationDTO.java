package edu.sjsu.cmpe275.lab2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data @NoArgsConstructor
public class ReservationDTO implements Serializable {

    private String reservationNumber;
    private PassengerDTO passenger;
    private String origin;
    private String destination;
    private Integer price;
    private List<FlightDTO> flights;

}
