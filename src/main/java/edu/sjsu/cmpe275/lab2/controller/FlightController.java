package edu.sjsu.cmpe275.lab2.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import java.lang.annotation.Repeatable;
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

    @GetMapping(value = "/{flightNumber}/{departureDate}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getFlight(
            @PathVariable("flightNumber") String flightNumber,
            @PathVariable("departureDate") Date departureDate,
            @RequestParam(value = "xml", required = false) Boolean responseType ){
        return flightService.getFlight(flightNumber,departureDate,responseType);
    }

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
            @RequestParam("yearOfManufacture") Integer yearOfManufacture){
        System.out.println("Flight Number--> "+flightNumber);
        return flightService.createOrUpdateFlight(flightNumber,departureDate,price,origin,destination,
                departureTime,arrivalTime,description,capacity,model,manufacturer,yearOfManufacture);
    }

    @DeleteMapping(value = "/{flightNumber}/{departureDate}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> deleteFlight(
            @PathVariable("flightNumber") String flightNumber,
            @PathVariable("departureDate") Date departureDate,
            @RequestParam(value = "xml", required = false) Boolean responseType ){
        return flightService.deleteFlight(flightNumber,departureDate,responseType);
    }

}
