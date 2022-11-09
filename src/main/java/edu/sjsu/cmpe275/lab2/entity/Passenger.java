package edu.sjsu.cmpe275.lab2.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Data @NoArgsConstructor
@Entity
public class Passenger {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;   // primary key
    private String firstname;
    private String lastname;
    private Integer birthyear;  // Full form only (see definition below)
    private String gender;  // Full form only
    @Column(unique = true)
    private String phone; // Phone numbers must be unique.   Full form only
    @OneToMany(mappedBy = "passenger")
    @ToString.Exclude
    private List<Reservation> reservations;   // Full form only

    public Passenger(String firstname, String lastname, Integer birthyear, String gender, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthyear = birthyear;
        this.gender = gender;
        this.phone = phone;
    }

}
