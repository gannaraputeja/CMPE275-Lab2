package edu.sjsu.cmpe275.lab2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data @NoArgsConstructor
public class PassengerDTO implements Serializable {

    private String id;
    private String firstname;
    private String lastname;
    private Integer birthyear;
    private String gender;
    private String phone;
    private List<ReservationDTO> reservations;

}
