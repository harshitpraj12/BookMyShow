package com.raj.bookmyshow.service;

import com.raj.bookmyshow.dto.*;
import com.raj.bookmyshow.entity.*;
import com.raj.bookmyshow.exception.ResourceNotFoundException;
import com.raj.bookmyshow.exception.SeatUnavailableException;
import com.raj.bookmyshow.repo.BookingRepository;
import com.raj.bookmyshow.repo.ShowRepository;
import com.raj.bookmyshow.repo.ShowSeatRepository;
import com.raj.bookmyshow.repo.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;


    @Transactional
    public BookingDto createBooking(@Valid BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(bookingRequestDto.getShowId())
                .orElseThrow(()-> new ResourceNotFoundException("Show not found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequestDto.getSeatIds());
        for(ShowSeat seat : selectedSeats){
            if(!"AVAILABLE".equals(seat.getStatus())){
                throw new SeatUnavailableException("Seat "+ seat.getSeat().getSeatNumber()+ " is not available");
            }
            seat.setStatus("LOCKED");
        }

        showSeatRepository.saveAll(selectedSeats);
        Double totalAmount = selectedSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethods(bookingRequestDto.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);

        Booking saveBooking = bookingRepository.save(booking);

        selectedSeats.forEach(seat-> {
            seat.setStatus("BOOKED");
            seat.setBooking(saveBooking);
        });

        showSeatRepository.saveAll(selectedSeats);

        return mapToBookingDto(saveBooking, selectedSeats);
    }


    public BookingDto getBookById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }


    @Transactional
    public BookingDto getBookingByNumber(String bookingNumber){
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    @Transactional
    public List<BookingDto> getBookingByUserId(Long userId){
        List<Booking> bookings = bookingRepository.findByUserId(userId);
                return bookings.stream()
                        .map(booking -> {
                            List<ShowSeat> seats = showSeatRepository.findAll()
                                    .stream()
                                    .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(userId))
                                    .collect(Collectors.toList());
                            return mapToBookingDto(booking, seats);
                        })
                        .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto cancelBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        booking.setStatus("CANCELLED");
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat->{
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

        if(booking.getPayment()!=null){
            booking.getPayment().setStatus("REFUNDED");
        }

        Booking updateBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updateBooking, seats);
    }


    private BookingDto mapToBookingDto(Booking booking, List<ShowSeat> seats){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

//        User Mapping
        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

//        Show Mapping
        ShowDto show = new ShowDto();
        show.setId(booking.getShow().getId());
        show.setEndTime(booking.getShow().getEndTime());
        show.setStartTime(booking.getShow().getStartTime());

//        Movie Mapping
        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        show.setMovie(movieDto);

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setTotalScreen(booking.getShow().getScreen().getTheater().getTotalScreens());
        screenDto.setTheater(theaterDto);
        show.setScreen(screenDto);
        bookingDto.setShow(show);

        List<ShowSeatDto> seatDtos = seats.stream()
                .map(seat->{
                    ShowSeatDto showSeatDto = new ShowSeatDto();
                    showSeatDto.setStatus(seat.getStatus());
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setPrice(seat.getPrice());

                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(seat.getId());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    showSeatDto.setSeat(baseSeatDto);
                    return showSeatDto;
                })
                .collect(Collectors.toList());

        bookingDto.setSeats(seatDtos);

        if(booking.getPayment()!=null){
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethods());
        }
        return bookingDto;
    }
}
