package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

}
