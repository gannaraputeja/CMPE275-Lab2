package edu.sjsu.cmpe275.lab2.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Plane { // Embedded
    private String model;
    private Integer capacity;
    private String manufacturer;
    private Integer yearOfManufacture;
}
