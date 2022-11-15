package edu.sjsu.cmpe275.lab2.controller;


import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * This is Passenger Entity.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Transactional
@RestController
@RequestMapping("/flight")
public class FlightController {

    @Autowired
    private FlightService flightService;

    /**
     *
     * @param flightNumber
     * @param departureDate
     * @param responseType
     * @return
     */
    @GetMapping(value = "/{flightNumber}/{departureDate}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getFlight(
            @PathVariable("flightNumber") String flightNumber,
            @PathVariable("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate,
            @RequestParam(value = "xml", required = false) Boolean responseType ){
        return flightService.getFlight(flightNumber,departureDate,responseType);
    }

    /**
     *
     * @param flightNumber
     * @param departureDate
     * @param price
     * @param origin
     * @param destination
     * @param departureTime
     * @param arrivalTime
     * @param description
     * @param capacity
     * @param model
     * @param manufacturer
     * @param yearOfManufacture
     * @return
     */
    @PostMapping(value = "/{flightNumber}/{departureDate}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createOrUpdateFlight(
            @PathVariable("flightNumber") String flightNumber,
            @PathVariable("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate,
            @RequestParam("price") Integer price,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("departureTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date departureTime,
            @RequestParam("arrivalTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date arrivalTime,
            @RequestParam("description") String description,
            @RequestParam("capacity") Integer capacity,
            @RequestParam("model") String model,
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("yearOfManufacture") Integer yearOfManufacture,
            @RequestParam(value = "xml", required = false) Boolean responseType){
        System.out.println("Flight Number--> "+flightNumber);
        return flightService.createOrUpdateFlight(flightNumber,departureDate,price,origin,destination,
                departureTime,arrivalTime,description,capacity,model,manufacturer,yearOfManufacture, responseType);
    }

    /**
     *
     * @param flightNumber
     * @param departureDate
     * @param responseType
     * @return
     */
    @DeleteMapping(value = "/{flightNumber}/{departureDate}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> deleteFlight(
            @PathVariable("flightNumber") String flightNumber,
            @PathVariable("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate,
            @RequestParam(value = "xml", required = false) Boolean responseType ){
        return flightService.deleteFlight(flightNumber,departureDate,responseType);
    }

}
