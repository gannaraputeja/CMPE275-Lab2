package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public Optional<Passenger> getPassenger(String id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        return passenger;
    }

    public Passenger createPassenger(String firstname, String lastname, Integer birthyear, String gender, String phone) {
        Passenger passenger = new Passenger(firstname, lastname, birthyear, gender, phone);
        passengerRepository.save(passenger);

        return passenger;
    }

}
