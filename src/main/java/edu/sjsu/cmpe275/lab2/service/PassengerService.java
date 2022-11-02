package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;
import java.util.Objects;


/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Passenger getPassenger(String id) {
        Passenger passenger = passengerRepository.findById(id).orElse(null);
        return passenger;
    }

    public Passenger createPassenger(String firstname, String lastname, Integer birthyear, String gender, String phone) {
        Passenger passenger = new Passenger(firstname, lastname, birthyear, gender, phone);
        passengerRepository.save(passenger);

        return passenger;
    }

    public ResponseEntity<Object> updatePassenger(String id, String firstname, String lastname, Integer birthyear, String gender, String phone, Boolean responseType){
        System.out.println("Updating Passenger");
        Passenger passenger = passengerRepository.findById(id).orElse(null);
        if(passenger==null){
            System.out.println("No passenger");
            return Util.prepareErrorResponse("404","Passenger doesnt exists", HttpStatus.BAD_REQUEST,responseType);
        }
        try{
            Passenger tempPass = passengerRepository.findByPhone(phone);
            if(tempPass != null && !Objects.equals(tempPass.getId(), id)){
                System.out.println("Same Phone number already exists");
                return (Util.prepareErrorResponse("404","User with this phone number already exists",HttpStatus.BAD_REQUEST,responseType));
            }

            passenger.setFirstname(firstname);
            passenger.setLastname(lastname);
            passenger.setBirthyear(birthyear);
            passenger.setGender(gender);
            passenger.setPhone(phone);
            passengerRepository.save(passenger);
        } catch (Exception e){
            return Util.prepareErrorResponse("404", "Passenger not updated",HttpStatus.BAD_REQUEST, responseType);
        }

        return Util.prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
    }

    public ResponseEntity<Object> deletePassenger(String id, Boolean responseType){
        Passenger passenger = passengerRepository.findById(id).orElse(null);
        if(passenger==null){
            return Util.prepareErrorResponse("404","Passenger not found",HttpStatus.NOT_FOUND,responseType);
        }

        try{
            List<Reservation> reservationList = reservationRepository.findByPassenger(passenger);
            if(reservationList.isEmpty()){
                //Delete Passenger
                passengerRepository.delete(passenger);
            }

            for(Reservation reservation:reservationList){
                //Delete all reservations using deleteReservation method.
                deleteReservation(reservation,passenger);
            }
        } catch (Exception e){
            return Util.prepareErrorResponse("404","Bad Request",HttpStatus.BAD_REQUEST,responseType);
        }

        return Util.prepareResponse(Util.convertToDTO(passenger),HttpStatus.OK,responseType);
    }

    public void deleteReservation(Reservation reservation, Passenger passenger){
        for(Flight flight:reservation.getFlights()){
            updateFlightSeat(flight);
            flight.getPassengers().remove(passenger);
        }
        passenger.getReservations().remove(reservation);
        reservationRepository.delete(reservation);
    }

    public void updateFlightSeat(Flight flight){
        int currSeats = flight.getSeatsLeft();
        flight.setSeatsLeft(currSeats+1);
    }

}
