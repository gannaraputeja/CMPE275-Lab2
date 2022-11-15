package edu.sjsu.cmpe275.lab2.util;

import edu.sjsu.cmpe275.lab2.dto.FlightDTO;
import edu.sjsu.cmpe275.lab2.dto.PassengerDTO;
import edu.sjsu.cmpe275.lab2.dto.ReservationDTO;
import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * This is Util class.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */


public class Util {

    private static final String dateFormat = "yy-MM-dd";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

    /**
     * Prepares a Response with given Response, HttpStatus
     *
     * @param response
     * @param status
     * @param responseType
     * @return Response in responseType format
     */
    public static ResponseEntity prepareResponse(Object response, HttpStatus status, Boolean responseType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if(responseType != null && responseType) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return new ResponseEntity(response, responseHeaders, status);
    }

    /**
     * Prepares Error Response with given Code, Message, HttpStatus
     *
     * @param code
     * @param msg
     * @param status
     * @param responseType
     * @return Response in responseType format
     */
    public static ResponseEntity prepareErrorResponse(String code, String msg, HttpStatus status, Boolean responseType) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if(responseType != null && responseType) {
            responseHeaders.setContentType(MediaType.APPLICATION_XML);
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return new ResponseEntity(buildError(code, msg), responseHeaders, status);
    }

    /**
     * Builds Error Response Object
     *
     * @param code
     * @param msg
     * @return
     */
    private static HashMap<String, HashMap> buildError(String code, String msg) {
        HashMap<String, HashMap> json = new HashMap<>();
        HashMap<String, String> badRequest = new HashMap<>();
        badRequest.put("code", code);
        badRequest.put("msg", msg);
        json.put("BadRequest", badRequest);
        return json;
    }

    /**
     * Converts Passenger Entity to PassengerDTO Simple Form
     *
     * @param passenger
     * @return PassengerDTO
     */
    public static PassengerDTO convertToDTOSimple(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        if(passenger != null) {
            dto.setId(passenger.getId());
            dto.setFirstname(passenger.getFirstname());
            dto.setLastname(passenger.getLastname());
        }
        return dto;
    }

    /**
     * Converts Passenger Entity to PassengerDTO Full Form
     *
     * @param passenger
     * @return PassengerDTO
     */
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

    /**
     * Converts Reservation Entity to ReservationDTO Simple Form
     *
     * @param reservation
     * @return ReservationDTO
     */
    public static ReservationDTO convertToDTOSimple(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        if(reservation != null) {
            dto.setReservationNumber(reservation.getReservationNumber());
            dto.setOrigin(reservation.getOrigin());
            dto.setDestination(reservation.getDestination());
        }
        return dto;
    }

    /**
     * Converts Reservation Entity to ReservationDTO Full Form
     *
     * @param reservation
     * @return ReservationDTO
     */
    public static ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        if(reservation != null) {
            dto.setReservationNumber(reservation.getReservationNumber());
            dto.setPassenger(convertToDTOSimple(reservation.getPassenger()));
            dto.setOrigin(reservation.getOrigin());
            dto.setDestination(reservation.getDestination());
            dto.setPrice(reservation.getPrice());
            if(reservation.getFlights() != null) {
                dto.setFlights(reservation.getFlights().stream().map(flight -> convertToDTOSimple(flight)).collect(Collectors.toList()));
            }
        }
        return dto;
    }

    /**
     * Converts Flight Entity to FlightDTO Simple Form
     *
     * @param flight
     * @return FlightDTO
     */
    public static FlightDTO convertToDTOSimple(Flight flight) {
        FlightDTO dto = new FlightDTO();
        if(flight != null) {
            dto.setFlightNumber(flight.getFlightNumber());
            dto.setDepartureDate(flight.getDepartureDate());
            dto.setDepartureTime(flight.getDepartureTime());
            dto.setArrivalTime(flight.getArrivalTime());
            dto.setOrigin(flight.getOrigin());
            dto.setDestination(flight.getDestination());
            dto.setSeatsLeft(flight.getSeatsLeft());
        }
        return dto;
    }

    /**
     * Converts Flight Entity to FlightDTO Full Form
     *
     * @param flight
     * @return FlightDTO
     */
    public static FlightDTO convertToDTO(Flight flight){
        FlightDTO dto = new FlightDTO();
        if(flight!=null){
            dto.setFlightNumber(flight.getFlightNumber());
            dto.setDepartureDate(flight.getDepartureDate());
            dto.setDepartureTime(flight.getDepartureTime());
            dto.setArrivalTime(flight.getArrivalTime());
            dto.setOrigin(flight.getOrigin());
            dto.setDestination(flight.getDestination());
            dto.setSeatsLeft(flight.getSeatsLeft());
            dto.setPrice(flight.getPrice());
            dto.setDescription(flight.getDescription());

            if(flight.getPassengers()!=null){
                dto.setPassengers(flight.getPassengers().stream().map(passenger -> convertToDTOSimple(passenger)).collect(Collectors.toList()));
            }

            if(flight.getPlane()!=null){
                Plane plane = new Plane(flight.getPlane().getModel(),flight.getPlane().getCapacity(),flight.getPlane().getManufacturer(),
                        flight.getPlane().getYearOfManufacture());
                dto.setPlane(plane);
            }
        }
        return dto;
    }

    /**
     * Converts String to Date object.
     *
     * @param dateString
     * @return Date
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateString) throws ParseException {
        return dateFormatter.parse(dateString);
    }

}
