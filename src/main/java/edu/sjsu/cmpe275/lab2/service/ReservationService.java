package edu.sjsu.cmpe275.lab2.service;


import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.exception.MyParseException;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;
import edu.sjsu.cmpe275.lab2.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is Reservation Service.
 * @author Raviteja Gannarapu, Sarat Kumar Kaniti, Ramya Kotha, Sai Charan Peda
 */

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;

	/**
	 * Get a Reservation based on ReservationNumber
	 *
	 * @param reservationNumber
	 * @param responseType
	 * @return Response in responseType format
	 */
    public ResponseEntity<Object> getReservation(String reservationNumber, Boolean responseType) {

        Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
        if(reservation.isPresent()) {
            return Util.prepareResponse(Util.convertToDTO(reservation.get()), HttpStatus.OK, responseType);
        } else {
            return Util.prepareErrorResponse("404", "Reservation with number " + reservationNumber
                    + " does not exist", HttpStatus.NOT_FOUND, responseType);
        }
    }

	/**
	 * Make a Reservation with given Reservation details.
	 *
	 * @param passengerId
	 * @param flightNumbers
	 * @param departureDates
	 * @param responseType
	 * @return Response in responseType format
	 * @throws MyParseException
	 */
    public ResponseEntity<Object> makeReservation(String passengerId, String flightNumbers, String departureDates, Boolean responseType) throws MyParseException {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(!passenger.isPresent()) {
            return Util.prepareErrorResponse("404", "Sorry, the passenger with ID " + passengerId
                    + " does not exist" , HttpStatus.NOT_FOUND, responseType);
        } else {
            List<String> flightNumbersList = Arrays.stream(flightNumbers.split(",")).collect(Collectors.toList());
            List<String> departureDatesStringList = Arrays.stream(departureDates.split(",")).collect(Collectors.toList());
            if(flightNumbersList.size() != departureDatesStringList.size()) {
                return Util.prepareErrorResponse("400", "Parameters count do not match. No. of FlightNumbers: " + flightNumbersList.size()
                        + ", No. of DepartureDates: " + departureDatesStringList.size(), HttpStatus.BAD_REQUEST, responseType);
            }
            List<Date> departureDatesList = departureDatesStringList.stream().map(d -> {
                try {
                    return Util.convertStringToDate(d);
                } catch (ParseException e) {
                    throw new MyParseException(e);
                }
            }).collect(Collectors.toList());
            List<Flight> flights = new ArrayList<>();
            for(int i=0; i < flightNumbersList.size(); i++) {
                Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightNumbersList.get(i),
                        departureDatesList.get(i));
                if(flight.isPresent()) {
                    flights.add(flight.get());
                } else {
                    return Util.prepareErrorResponse("404", "Sorry, the flight with number " + flightNumbersList.get(i)
                            + " does not exist", HttpStatus.NOT_FOUND, responseType);
                }
            };
            if(flights.isEmpty()) {
                return Util.prepareErrorResponse("400", "Reservation should have at least 1 valid flight.", HttpStatus.BAD_REQUEST, responseType);
            } else {
                if(flights.size() > 1 && hasOverlapConflict(flights)) {
                    return Util.prepareErrorResponse("409", "Sorry, found time overlap with given flights.", HttpStatus.CONFLICT, responseType);
                } else if(hasOverlapReservation(passenger.get(), flights)){
                    return Util.prepareErrorResponse("409", "Sorry, passenger cannot have overlap reservations.", HttpStatus.CONFLICT, responseType);
                } else {
                    String origin = flights.get(0).getOrigin();
                    String destination = flights.get(flights.size()-1).getDestination();
                    Integer price = flights.stream().map(flight -> flight.getPrice()).reduce(0, (a,b) -> a+b);

                    if(!hasSeatsLeft(flights)){
                        return Util.prepareErrorResponse("400", "Sorry, one or more flights has no seats left.", HttpStatus.BAD_REQUEST, responseType);
                    } else {
                        flights.stream().forEach(flight -> {
                            flight.setSeatsLeft(flight.getSeatsLeft() - 1);
                            flight.getPassengers().add(passenger.get());
                            flightRepository.save(flight);
                        });
                    }

                    Reservation reservation = new Reservation(passenger.get(), origin, destination, price, flights);
                    reservationRepository.save(reservation);
                    return Util.prepareResponse(Util.convertToDTO(reservation), HttpStatus.OK, responseType);
                }
            }
        }

    }

	/**
	 * Checks if given Flights has conflicts based on its departureTime and arrivalTime
	 *
	 * @param flights
	 * @return True/False
	 */
    public Boolean hasOverlapConflict(List<Flight> flights) {
        for(int i=0; i<flights.size()-1; i++) {
            Flight f1 = flights.get(i);
            Flight f2 = flights.get(i+1);
            if(validateOverlap(f1.getDepartureTime(), f1.getArrivalTime(), f2.getDepartureTime(), f2.getArrivalTime()))
                return true;
        }
        return false;
    }

	/**
	 * Checks for overlap of given dates.
	 *
	 * @param dt1
	 * @param at1
	 * @param dt2
	 * @param at2
	 * @return True/False
	 */
    public Boolean validateOverlap(Date dt1, Date at1, Date dt2, Date at2) {
        if(dt1.compareTo(dt2) == 0 || at1.compareTo(at2) == 0 || dt1.compareTo(at2) == 0 || at1.compareTo(dt2) == 0) {
            return true;
        } else if( (at1.after(dt2) && at1.before(at2))|| (at2.after(dt1) && at2.before(at1)) ) {
            return true;
        }
        return false;
    }

	/**
	 * Check if Passenger has conflict with existing Reservations
	 *
	 * @param passenger
	 * @param flights
	 * @return True/False
	 */
    public Boolean hasOverlapReservation(Passenger passenger, List<Flight> flights) {
        if(passenger.getReservations().size() < 1) {
            return false;
        } else {
            List<Reservation> existingReservations = passenger.getReservations();
            Date dt1 = flights.get(0).getDepartureTime();
            Date at1 = flights.get(flights.size()-1).getArrivalTime();
            for(int i = 0; i < existingReservations.size(); i++) {
                List<Flight> existingflights = existingReservations.get(i).getFlights();
                if(existingflights.size() > 0) {
                    Date dt2 = existingflights.get(0).getDepartureTime();
                    Date at2 = existingflights.get(existingflights.size() - 1).getArrivalTime();
                    if (validateOverlap(dt1, at1, dt2, at2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

	/**
	 * Checks if given Flights has seatsLeft.
	 *
	 * @param flights
	 * @return True/False
	 */
    public Boolean hasSeatsLeft(List<Flight> flights) {
        return flights.stream().allMatch(flight -> flight.getSeatsLeft() > 0 && flight.getSeatsLeft() <= flight.getPlane().getCapacity());
    }

	/**
	 * Converts String to Date object.
	 *
	 * @param departureDatesStringList
	 * @return
	 */
    public List<Date> convertStingDatesToDate(List<String> departureDatesStringList){
        List<Date> departureDatesList = departureDatesStringList.stream().map(d -> {
            try {
                return Util.convertStringToDate(d);
            } catch (ParseException e) {
                throw new MyParseException(e);
            }
        }).collect(Collectors.toList());
        return departureDatesList;
    }

	/**
	 * Checks if given dates have intersection/overlap.
	 *
	 * @param dt1
	 * @param at1
	 * @param dt2
	 * @param at2
	 * @return True/False
	 */
    public Boolean checkIntersection(Date dt1, Date at1, Date dt2, Date at2) 
	{
	    return !dt1.after(at2) && !dt2.after(at1);
    }

	/**
	 * Checks given flights has overlap conflict.
	 *
	 * @param flightList
	 * @return True/False
	 */
	private boolean checkConflict(List<Flight> flightList) 
	{
		if(flightList.size()==1)
		{
			return false;
		}
		
		for(int i=0; i<flightList.size()-1; i++) 
		{
            Flight curr = flightList.get(i);
            Flight next = flightList.get(i+1);
            int t = curr.getDepartureDate().compareTo(next.getDepartureDate());
            if(t==0)
            {
            	
            	if(checkIntersection(curr.getDepartureTime(), curr.getArrivalTime(), next.getDepartureTime(), next.getArrivalTime()))
                {
            		
                	return true;
                }
            }
            
        }
		
        return false;
	}

	/**
	 * Checks if Passenger has Reservation conflict and given Flights.
	 *
	 * @param passenger
	 * @param flightList
	 * @param reservationNumber
	 * @return
	 */
	private boolean checkReservationConflict(Passenger passenger, List<Flight> flightList, String reservationNumber) 
	{
		if(passenger.getReservations().size() < 1) 
		{
            return false;
        } 
		else 
		{
            List<Reservation> existingReservations = passenger.getReservations();
            Date dt1 = flightList.get(0).getDepartureTime();
            Date at1 = flightList.get(flightList.size()-1).getArrivalTime();
            for(int i = 0; i < existingReservations.size(); i++) 
            {
            	if(!reservationNumber.equals(existingReservations.get(i).getReservatioNumber()))
            	{
	                List<Flight> existingflights = existingReservations.get(i).getFlights();
	                if(existingflights.size() > 0) 
	                {
	                    Date dt2 = existingflights.get(0).getDepartureTime();
	                    Date at2 = existingflights.get(existingflights.size() - 1).getArrivalTime();
	                    
	                    if (checkIntersection(dt1, at1, dt2, at2)) {
	                        return true;
	                    }
	                }
            	}
            }
            return false;
        }
		
	}

	/**
	 * Updates a Reservation based on given Reservation params.
	 *
	 * @param reservationNumber
	 * @param flightsAdded
	 * @param flightsRemoved
	 * @param departureDatesAdded
	 * @param departureDatesRemoved
	 * @param responseType
	 * @return Response in responseType format
	 * @throws ParseException
	 */
	public ResponseEntity<Object> updateReservation(String reservationNumber, String flightsAdded, String flightsRemoved, String departureDatesAdded, String departureDatesRemoved, boolean responseType) throws ParseException 
	{
		Optional<Reservation> reservationObj = reservationRepository.findById(reservationNumber);
		if(!reservationObj.isPresent())
		{
            return Util.prepareErrorResponse("400", "Sorry, reservation number does not exist.", HttpStatus.BAD_REQUEST, responseType); 
		}
		if((flightsAdded!=null && flightsAdded.isEmpty()) || (departureDatesAdded!=null && departureDatesAdded.isEmpty()))
		{
            return Util.prepareErrorResponse("400", "Invalid Request, Data Missing", HttpStatus.BAD_REQUEST, responseType); 
		}
		
		if((flightsRemoved!=null && flightsRemoved.isEmpty()) || (departureDatesRemoved!=null && departureDatesRemoved.isEmpty()))
		{
            return Util.prepareErrorResponse("400", "Invalid Request, Data Missing", HttpStatus.BAD_REQUEST, responseType); 
		}
		
		if((flightsAdded!=null && departureDatesAdded==null) || (departureDatesAdded!=null && flightsAdded==null))
		{
            return Util.prepareErrorResponse("400", "Invalid Request, Data Missing", HttpStatus.BAD_REQUEST, responseType); 
		}
		
		if((flightsRemoved!=null && departureDatesRemoved==null) || (departureDatesRemoved!=null && flightsRemoved==null))
		{
            return Util.prepareErrorResponse("400", "Invalid Request, Data Missing", HttpStatus.BAD_REQUEST, responseType); 
		}
		System.out.println("***************************************************************");
		Reservation res = reservationObj.get();
		
		List<Flight> flightList = res.getFlights();
		
		List<Flight> existingFlightList = new ArrayList<>();
		
		for(int i=0;i<flightList.size();i++)
		{
			existingFlightList.add(flightList.get(i));
		}
		
		String[] flightNumbersRemovedArr = new String[0];
		String[] departureDatesRemovedArr = new String[0];
		///removing flights
		if(flightsRemoved!=null && (!flightsRemoved.isEmpty()) && departureDatesRemoved!=null && (!departureDatesRemoved.isEmpty()))
		{
			flightNumbersRemovedArr = flightsRemoved.trim().split("\\s*,\\s*");
			departureDatesRemovedArr = departureDatesRemoved.trim().split("\\s*,\\s*");
			if(flightNumbersRemovedArr.length != departureDatesRemovedArr.length)
			{
                return Util.prepareErrorResponse("400", "Invalid Request, Flights Removed or Depature Dates Removed are missing some values", HttpStatus.BAD_REQUEST, responseType); 
			}
			
			List<Flight> fRem = new ArrayList<>();
			//Remove Flights from existing list
			for(int i=0;i<flightNumbersRemovedArr.length;i++)
			{
				boolean flag = false;
				for(Flight f:existingFlightList)
				{
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
					Date dateObjRem = (Date)formatter.parse(departureDatesRemovedArr[i]);
					int d = dateObjRem.compareTo(f.getDepartureDate());
					if(flightNumbersRemovedArr[i].equals(f.getFlightNumber()) && d==0)
					{
						fRem.add(f);
						flag = true;
						
					}
				}
				//if flight not found in list of existing reservation
				if(flag==false)
				{
					String msg = "Invalid request, The Flight number "+flightNumbersRemovedArr[i]+" and Departure Date "+ departureDatesRemovedArr[i]+" does not exist in the reservation";
                    return Util.prepareErrorResponse("400", msg, HttpStatus.BAD_REQUEST, responseType); 
				}
			}
			
			existingFlightList.removeAll(fRem);
			
		}
		//if all the flights in the reservation are removed
		if(existingFlightList.size()<=0 && flightsAdded==null)
		{
			String msg = "Invalid request, Cannot Remove all the flights in the Reservation";
            return Util.prepareErrorResponse("400", msg, HttpStatus.BAD_REQUEST, responseType); 
		}
		
		
		int newFlightsListPrice = 0;
		List<Flight> newAddedFlights = new ArrayList<>();
		///Adding flights
		if(flightsAdded!=null && (!flightsAdded.isEmpty()) && departureDatesAdded!=null && (!departureDatesAdded.isEmpty()))
		{			
			String[] flightNumbersAddedArr = flightsAdded.trim().split("\\s*,\\s*");
			String[] departureDatesAddedArr = departureDatesAdded.trim().split("\\s*,\\s*");

			//checking for insufficient data 
			if(flightNumbersAddedArr.length != departureDatesAddedArr.length)
			{
                return Util.prepareErrorResponse("400", "Invalid Request, Flights Added or Depature Dates Addded are missing some values", HttpStatus.BAD_REQUEST, responseType); 
			}
			
			for(int i=0;i<flightNumbersAddedArr.length;i++)
			{
				
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
				Date dateObj = (Date)formatter.parse(departureDatesAddedArr[i]);
				//checking of the new;y added flights are valid flights or not
				Optional<Flight> flightObj = flightRepository.findByFlightNumberAndDepartureDate(flightNumbersAddedArr[i], dateObj);
				if(!flightObj.isPresent())
				{
					String msg = "Flight with Number "+flightNumbersAddedArr[i]+" and Departure Date "+departureDatesAddedArr[i]+" cannot be found";
                    return Util.prepareErrorResponse("400", msg, HttpStatus.BAD_REQUEST, responseType); 
				}
				//checking if the newly added flights have sufficient seats or not
				if(flightObj.get().getSeatsLeft() <= 0)
				{
					String msg = "Flight with Number "+flightNumbersAddedArr[i]+" is full";
                    return Util.prepareErrorResponse("400", msg, HttpStatus.BAD_REQUEST, responseType); 
				}
				newFlightsListPrice = newFlightsListPrice + flightObj.get().getPrice();
				existingFlightList.add(flightObj.get());
				newAddedFlights.add(flightObj.get());
			}
			
			//sorting the newly newly generated flight list flights
			Collections.sort(existingFlightList, (a,b)->
			{
				int t = a.getDepartureDate().compareTo(b.getDepartureDate());
				if(t==0)
				{
					return a.getDepartureTime().compareTo(b.getDepartureTime());
				}
			    return a.getDepartureDate().compareTo(b.getDepartureDate());
		   });
			//checking if there is any time overlap conflict in the newly created flight list
			if(checkConflict(existingFlightList))
			{
				String msg = "Flight timings overlap with each other";
                return Util.prepareErrorResponse("400",msg, HttpStatus.BAD_REQUEST, responseType); 
			}
			//checking if there is any time overlap conflict in the newly created flight list with existing reservations
			if(checkReservationConflict(reservationObj.get().getPassenger(), existingFlightList, reservationObj.get().getReservatioNumber()))
			{
				String msg = "Flight timings overlap with existing reservation for the passenger";
                return Util.prepareErrorResponse("400",msg, HttpStatus.BAD_REQUEST, responseType); 
			}
		}
		
		Reservation r = reservationObj.get();
		//updating flight seats
		for(int i=0;i<newAddedFlights.size();i++)
		{
			Flight updateFlight = newAddedFlights.get(i);
			updateFlight.setSeatsLeft(updateFlight.getSeatsLeft() - 1);
			updateFlight.getPassengers().add(r.getPassenger());
            flightRepository.save(updateFlight);
		}
		//updating price and seats for removed flights
		int priceRemoved=0;
		for(int i=0;i<flightNumbersRemovedArr.length;i++)
		{
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			Date remObj = (Date)formatter.parse(departureDatesRemovedArr[i]);
			Optional<Flight> fObj = flightRepository.findByFlightNumberAndDepartureDate(flightNumbersRemovedArr[i], remObj);
			if(!fObj.isPresent())
			{
				Flight f = fObj.get();
				f.setSeatsLeft(f.getSeatsLeft() + 1);
				f.getPassengers().remove(r.getPassenger());
				flightRepository.save(f);
				priceRemoved = f.getPrice() + priceRemoved;
			}
		}
		
		r.setPrice(reservationObj.get().getPrice() + newFlightsListPrice - priceRemoved);
		r.setOrigin(existingFlightList.get(0).getOrigin());
		r.setDestination(existingFlightList.get(existingFlightList.size()-1).getDestination());
		r.setFlights(existingFlightList);
		Reservation response = reservationRepository.save(r);
		
        return Util.prepareResponse(Util.convertToDTO(response), HttpStatus.OK, responseType);
    }


    // public ResponseEntity<Object> updateReservation(String reservationNumber, String fltAdd, String fltRm, String dda, String ddr, Boolean responseType ) {
    //     ResponseEntity<Object> responseEntity;        
    //     if(fltAdd != null && fltAdd.isEmpty() && dda !=null && dda.isEmpty() || fltRm != null && fltRm.isEmpty() ){
    //         return Util.prepareErrorResponse("400", "Sorry, flightsAdded or flightsRemoved is Empty.", HttpStatus.BAD_REQUEST, responseType); 
    //     }
    //     Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
    //     if(!reservation.isPresent()) {
    //         return Util.prepareErrorResponse("400", "Sorry, reservation number does not exist.", HttpStatus.BAD_REQUEST, responseType); 
    //     }
    //     Reservation reservationObj = reservation.get();
    //     List<Flight> flights = reservationObj.getFlights();
    //     if(fltRm != null) {
    //         List<String> flightsToRemove = Arrays.stream(fltRm.split(",")).collect(Collectors.toList());
    //         // Remove Flights
    //         flights.removeIf(flight -> flightsToRemove.contains(flight.getFlightNumber()));
    //         // Update Removed flights
    //         flights.stream().forEach(flight -> {
    //             if(flightsToRemove.contains(flight.getFlightNumber())) {
    //                 flight.setSeatsLeft(flight.getSeatsLeft() + 1);
    //                 flight.getPassengers().remove(reservationObj.getPassenger());
    //                 flightRepository.save(flight);
    //             }
    //         });
    //     }

    //     if(fltAdd != null && dda != null) {
    //         List<String> flightsToAdd    = Arrays.stream(fltAdd.split(",")).collect(Collectors.toList());
    //         List<String> ddaStrList = Arrays.stream(dda.split(",")).collect(Collectors.toList());
    //         int nFlightsToAdd = flightsToAdd.size(); 
    //         if( nFlightsToAdd != ddaStrList.size()) {
    //             return Util.prepareErrorResponse("400", "Parameters count do not match. No. of FlightNumbers: " + flightsToAdd.size() + ", No. of DepartureDates: " + ddaStrList.size(), HttpStatus.BAD_REQUEST, responseType);
    //         }

    //         List<Date> ddaList = convertStingDatesToDate(ddaStrList);

    //         for(int i=0; i < flightsToAdd.size(); i++) {
    //             Optional<Flight> flight = flightRepository.findByFlightNumberAndDepartureDate(flightsToAdd.get(i), ddaList.get(i));
    //             if(flight.isPresent()) {
    //                 flights.add(flight.get());
    //             } else {
    //                 return Util.prepareErrorResponse("404", "Sorry, the flight with number " + flightsToAdd.get(i) + " does not exist", HttpStatus.NOT_FOUND, responseType);
    //             }
    //         };

    //         if(!hasSeatsLeft(flights) || hasOverlapConflict(flights) || hasOverlapReservation(reservationObj.getPassenger(), flights)) {
    //             return Util.prepareErrorResponse("400", "Sorry, the new flights has the conflict in either capacity or overlap or same reservation ", HttpStatus.NOT_FOUND, responseType);
    //         }
    //     }

    //     String origin = flights.get(0).getOrigin();
    //     String destination = flights.get(flights.size()-1).getDestination();
    //     Integer price = flights.stream().map(flight -> flight.getPrice()).reduce(0, (a,b) -> a+b);

    //     reservationObj.setFlights(flights);
    //     reservationObj.setOrigin(origin);
    //     reservationObj.setDestination(destination);
    //     reservationObj.setPrice(price);
    //     reservationRepository.save(reservationObj);
    //     return Util.prepareResponse(Util.convertToDTO(reservationObj), HttpStatus.OK, responseType);
    // }


	/**
	 * Deletes a Reservation based on reservationNumber
	 *
 	 * @param reservationNumber
	 * @param responseType
	 * @return Success/Error Response in given responseType
	 */
	public ResponseEntity<Object> deleteReservationById(String reservationNumber, boolean responseType) 
	{
        Optional<Reservation> reservation = reservationRepository.findById(reservationNumber);
        if(!reservation.isPresent()) {
            return Util.prepareErrorResponse("400", "Sorry, reservation number does not exist.", HttpStatus.BAD_REQUEST, responseType); 
        }
		List<Flight> flights = reservation.get().getFlights();
		for(Flight f : flights)
		{
			f.setSeatsLeft(f.getSeatsLeft()+1);
			flightRepository.save(f);
		}
		
		reservationRepository.delete(reservation.get());
        String msg = "Reservation with number "+reservationNumber +" is canceled successfully";
		return Util.prepareResponse(new Success("200","msg"), HttpStatus.OK, responseType);
	}
}
