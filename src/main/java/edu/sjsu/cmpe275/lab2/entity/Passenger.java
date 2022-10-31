package edu.sjsu.cmpe275.lab2.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Passenger {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;   // primary key
    private String firstname;
    private String lastname;
    private int birthyear;  // Full form only (see definition below)
    private String gender;  // Full form only
    private String phone; // Phone numbers must be unique.   Full form only
    @OneToMany(mappedBy = "passenger")
    private List<Reservation> reservations;   // Full form only

}
