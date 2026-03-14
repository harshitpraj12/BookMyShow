package com.raj.bookmyshow.controller;

import com.raj.bookmyshow.dto.BookingDto;
import com.raj.bookmyshow.dto.BookingRequestDto;
import com.raj.bookmyshow.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto){
        return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BookingDto>> getBookingByUserId(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingByUserId(id));
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
