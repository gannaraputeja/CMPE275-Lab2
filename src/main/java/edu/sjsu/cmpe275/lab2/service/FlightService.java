package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Plane;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public ResponseEntity<Object> getFlight(String flightNumber, Date departureDate, Boolean responseType) {
        Flight flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber,departureDate).orElse(null);
        System.out.println(flight);
        if(flight==null){
            String code = "Sorry, the requested flight with number "+ flightNumber +" does not exist";
            return Util.prepareErrorResponse("404",code, HttpStatus.NOT_FOUND,responseType);
        }

        return Util.prepareResponse(Util.convertToDTO(flight),HttpStatus.OK,responseType);
    }
    public ResponseEntity<Object> createOrUpdateFlight(String flightNumber, Date departureDate, Integer price, String origin,
                                                       String destination, Date departureTime, Date arrivalTime, String description,
                                                       Integer capacity, String model, String manufacturer, Integer yearOfManufacture) {
        Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumber,departureDate);
        Plane plane = new Plane(model, capacity, manufacturer, yearOfManufacture);
        if(!flight.isPresent()) {
            try{
                Flight newFlight = new Flight(flightNumber, departureDate, departureTime, arrivalTime, price,
                        origin, destination, capacity, description, plane, new ArrayList<Passenger>());
                flightRepository.save(newFlight);
                return Util.prepareResponse(Util.convertToDTO(newFlight), HttpStatus.OK, false);
            }catch (Exception e){
                return Util.prepareErrorResponse("400","New Flight creation failed",HttpStatus.BAD_REQUEST,false);
            }
        }else{
            try{
                Integer passengersCount = flight.get().getPassengers().size();
                if(passengersCount > capacity){
                    return Util.prepareErrorResponse("400","Flight Capacity cannot be lower than the number of active reservations",
                            HttpStatus.BAD_REQUEST,false);
                }else {
                    //Updating the seats left based on updated capacity value.
                    flight.get().setSeatsLeft(capacity-passengersCount);
                }

                flight.get().setFlightNumber(flightNumber);
                flight.get().setDepartureDate(departureDate);
                flight.get().setPrice(price);
                flight.get().setOrigin(origin);
                flight.get().setDestination(destination);
                flight.get().setDepartureTime(departureTime);
                flight.get().setArrivalTime(arrivalTime);
                flight.get().setDescription(description);
                flight.get().setPlane(plane);
                flightRepository.save(flight.get());
                return Util.prepareResponse(Util.convertToDTO(flight.get()),HttpStatus.OK,false);
            } catch (Exception e){
                return Util.prepareErrorResponse("400","Exception while updating the flight details",HttpStatus.BAD_REQUEST,false);
            }
        }
    }

    public ResponseEntity<Object> deleteFlight(String flightNumber, Date departureDate, Boolean responseType) {
        return null;
    }
}
