package edu.sjsu.cmpe275.lab2.util;

import edu.sjsu.cmpe275.lab2.dto.PassengerDTO;
import edu.sjsu.cmpe275.lab2.dto.ReservationDTO;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */


public class Util {


    public static ResponseEntity prepareResponse(Object response, HttpStatus status, Boolean responseType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if(responseType != null && responseType) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return new ResponseEntity(response, responseHeaders, status);
    }

    public static ResponseEntity prepareErrorResponse(String code, String msg, HttpStatus status, Boolean responseType) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if(responseType != null && responseType) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return new ResponseEntity(buildError(code, msg), responseHeaders, status);
    }

    private static HashMap<String, HashMap> buildError(String code, String msg) {
        HashMap<String, HashMap> json = new HashMap<>();
        HashMap<String, String> badRequest = new HashMap<>();
        badRequest.put("code", code);
        badRequest.put("msg", msg);
        json.put("BadRequest", badRequest);
        return json;
    }

    public static PassengerDTO convertToDTO(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        if(passenger != null) {
            dto.setId(passenger.getId());
            dto.setFirstname(passenger.getFirstname());
            dto.setLastname(passenger.getLastname());
            dto.setBirthyear(passenger.getBirthyear());
            dto.setGender(passenger.getGender());
            dto.setPhone(passenger.getPhone());

            if (passenger.getReservations() != null)
                dto.setReservations(passenger.getReservations().stream().map(reservation -> convertToDTOSimple(reservation))
                        .collect(Collectors.toList()));
        }
        return dto;
    }

    public static ReservationDTO convertToDTOSimple(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        if(reservation != null) {
            dto.setReservationNumber(reservation.getReservationNumber());
            dto.setOrigin(reservation.getOrigin());
            dto.setDestination(reservation.getDestination());
            dto.setPrice(reservation.getPrice());
        }
        return dto;
    }

}
