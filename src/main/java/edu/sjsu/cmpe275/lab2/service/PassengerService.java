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
 * This is Passenger Service.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Gets a Passenger based on PassengerId.
     *
     * @param id
     * @param responseType
     * @return Response in responseType format
     */
    public ResponseEntity<Object> getPassenger(String id, Boolean responseType) {
        ResponseEntity<Object> responseEntity;
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()) {
            responseEntity = Util.prepareResponse(Util.convertToDTO(passenger.get()), HttpStatus.OK, responseType);
        } else {
            responseEntity = Util.prepareErrorResponse("404", "Sorry, the requested passenger with ID " + id
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
        }
        return responseEntity;
    }

    /**
     * Creates a Passenger based on given Passenger details.
     *
     * @param firstname
     * @param lastname
     * @param birthyear
     * @param gender
     * @param phone
     * @param responseType
     * @return Response in responseType format
     */
    public ResponseEntity<Object> createPassenger(String firstname, String lastname, Integer birthyear, String gender, String phone, Boolean responseType) {
        Passenger passenger = new Passenger(firstname, lastname, birthyear, gender, phone);
        passengerRepository.save(passenger);
        ResponseEntity<Object> responseEntity = Util.prepareResponse(Util.convertToDTO(passenger), HttpStatus.OK, responseType);
        return responseEntity;
    }

    /**
     * Updates a Passenger based on PassengerId with other Passenger details.
     *
     * @param id
     * @param firstname
     * @param lastname
     * @param birthyear
     * @param gender
     * @param phone
     * @param responseType
     * @return Response in responseType format
     */
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

    /**
     * Deletes a given Reservation of given Passenger
     *
     * @param reservation
     * @param passenger
     */
    public void deleteReservation(Reservation reservation, Passenger passenger){
        for(Flight flight:reservation.getFlights()){
            updateFlightSeat(flight);
            flight.getPassengers().remove(passenger);
        }
        passenger.getReservations().remove(reservation);
        reservationRepository.delete(reservation);
    }

    /**
     * Updates Flights seatsLeft attribute.
     * @param flight
     */
    public void updateFlightSeat(Flight flight){
        int currSeats = flight.getSeatsLeft();
        flight.setSeatsLeft(currSeats+1);
    }

}
