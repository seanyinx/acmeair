package com.acmeair.web;

import com.acmeair.morphia.entities.BookingImpl;
import com.acmeair.service.BookingService;
import com.acmeair.web.dto.BookingInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingsREST.class)
public class BookingsRESTTest {
    private final String      bookingNumber  = "abc";
    private final String      userId         = "mike";
    private final String      flightId       = "SIN-1021";
    private final String      toFlightId     = "toFlightId-1021";
    private final String      toFlightSegId  = "toFlightSegId-1021";
    private final String      retFlightId    = "retFlightId-1021";
    private final String      retFlightSegId = "retFlightSegId-1021";
    private final boolean      oneWayFlight = false;
    private final Date        dateOfFlight   = new Date();
    private final BookingImpl booking        = new BookingImpl(bookingNumber, dateOfFlight, userId, flightId);

    private JacksonTester<BookingInfo> json;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void getsBookingInfoByNumber() throws Exception {
        given(bookingService.getBooking(userId, bookingNumber))
                .willReturn(booking);

        mvc.perform(get("/bookings/bybookingnumber/{userid}/{number}", userId, bookingNumber).accept(APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(APPLICATION_JSON_UTF8))
           .andExpect(jsonPath("$.bookingId", is(bookingNumber)))
           .andExpect(jsonPath("$.customerId", is(userId)));
    }

    @Test
    public void getsBookingsByUser() throws Exception {
        given(bookingService.getBookingsByUser(userId))
                .willReturn(singletonList(booking));

        mvc.perform(get("/bookings/byuser/{user}", userId).accept(APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentType(APPLICATION_JSON_UTF8))
           .andExpect(jsonPath("$[0].bookingId", is(bookingNumber)))
           .andExpect(jsonPath("$[0].customerId", is(userId)));
    }

    @Test
    public void cancelsBookingByNumber() throws Exception {
        mvc.perform(post("/bookings/cancelbooking").param("number", bookingNumber)
                                                   .param("userid", userId)
                                                   .contentType(APPLICATION_FORM_URLENCODED))
           .andExpect(status().isOk())
           .andExpect(content().contentType(APPLICATION_JSON_UTF8))
           .andExpect(content().string("booking " + bookingNumber + " deleted."));
    }

    @Test
    public void booksFlight() throws Exception {
        given(bookingService.bookFlight(userId, toFlightSegId, toFlightId)).willReturn(flightId);
        given(bookingService.bookFlight(userId, retFlightSegId, retFlightId)).willReturn(flightId);

        mvc.perform(post("/bookings/bookflights").param("number", bookingNumber)
                                                 .param("userid", userId)
                                                 .param("toFlightId", toFlightId)
                                                 .param("toFlightSegId", toFlightSegId)
                                                 .param("retFlightId", retFlightId)
                                                 .param("retFlightSegId", retFlightSegId)
                                                 .param("oneWayFlight", String.valueOf(oneWayFlight))
                                                 .contentType(APPLICATION_FORM_URLENCODED))
           .andExpect(status().isOk())
           .andExpect(content().contentType(APPLICATION_JSON_UTF8))
           .andExpect(jsonPath("$.departBookingId", is(flightId)))
           .andExpect(jsonPath("$.returnBookingId", is(flightId)))
           .andExpect(jsonPath("$.oneWay", is(oneWayFlight)));
    }
}
