/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.web;

import com.acmeair.entities.Booking;
import com.acmeair.service.BookingService;
import com.acmeair.web.dto.BookingInfo;
import com.acmeair.web.dto.BookingReceiptInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/bookings")
public class BookingsREST {

	@Autowired
	private BookingService bs;
	
	@RequestMapping(method = RequestMethod.POST, value = "/bookflights", produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<BookingReceiptInfo> bookFlights(
			@RequestParam("userid") String userid,
			@RequestParam("toFlightId") String toFlightId,
			@RequestParam("toFlightSegId") String toFlightSegId,
			@RequestParam("retFlightId") String retFlightId,
			@RequestParam("retFlightSegId") String retFlightSegId,
			@RequestParam("oneWayFlight") boolean oneWay) {
		try {
			String bookingIdTo = bs.bookFlight(userid, toFlightSegId, toFlightId);
			String bookingIdReturn = null;
			if (!oneWay) {
				bookingIdReturn = bs.bookFlight(userid, retFlightSegId, retFlightId);
			}
			// YL. BookingInfo will only contains the booking generated keys as customer info is always available from the session
			BookingReceiptInfo bi;
			if (!oneWay)
				bi = new BookingReceiptInfo(bookingIdTo, bookingIdReturn, oneWay);
			else
				bi = new BookingReceiptInfo(bookingIdTo, null, oneWay);
			
			return new ResponseEntity<>(bi, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/bybookingnumber/{userid}/{number}", produces = "application/json")
	public BookingInfo getBookingByNumber(
			@PathVariable("number") String number,
			@PathVariable("userid") String userid
	) {
		try {
			Booking b = bs.getBooking(userid, number);
			BookingInfo bi = null;
			if(b != null){
				bi = new BookingInfo(b);
			}
			return bi;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/byuser/{user}", produces = "application/json")
	public List<BookingInfo> getBookingsByUser(@PathVariable("user") String user) {
		try {
			List<Booking> list =  bs.getBookingsByUser(user);
			List<BookingInfo> newList = new ArrayList<BookingInfo>();
			for(Booking b : list){
				newList.add(new BookingInfo(b));
			}
			return newList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/cancelbooking", produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> cancelBookingsByNumber(
			@RequestParam("number") String number,
			@RequestParam("userid") String userid) {
		try {
			bs.cancelBooking(userid, number);
			return new ResponseEntity<>("booking " + number + " deleted.", HttpStatus.OK);
					
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
