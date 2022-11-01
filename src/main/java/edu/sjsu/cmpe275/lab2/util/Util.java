package edu.sjsu.cmpe275.lab2.util;

import edu.sjsu.cmpe275.lab2.dto.PassengerDTO;
import edu.sjsu.cmpe275.lab2.dto.ReservationDTO;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;

import java.util.stream.Collectors;

public class Util {

    public static PassengerDTO convertToDTO(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(passenger.getId());
        dto.setFirstname(passenger.getFirstname());
        dto.setLastname(passenger.getLastname());
        dto.setBirthyear(passenger.getBirthyear());
        dto.setGender(passenger.getGender());
        dto.setPhone(passenger.getPhone());

        dto.setReservations(passenger.getReservations().stream().map(reservation -> convertToDTOSimple(reservation))
                .collect(Collectors.toList()));
        return dto;
    }

    public static ReservationDTO convertToDTOSimple(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationNumber(reservation.getReservationNumber());
        dto.setOrigin(reservation.getOrigin());
        dto.setDestination(reservation.getDestination());
        dto.setPrice(reservation.getPrice());
        return dto;
    }

}
