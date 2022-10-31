package edu.sjsu.cmpe275.lab2.entity;


import javax.persistence.Embeddable;

@Embeddable
public class Plane { // Embedded
    private String model;
    private int capacity;
    private String manufacturer;
    private int yearOfManufacture;
}
