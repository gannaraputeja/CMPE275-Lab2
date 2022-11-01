package edu.sjsu.cmpe275.lab2.service;


import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

}
