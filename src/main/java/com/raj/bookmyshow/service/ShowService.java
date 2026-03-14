package com.raj.bookmyshow.service;

import com.raj.bookmyshow.dto.*;
import com.raj.bookmyshow.entity.Movie;
import com.raj.bookmyshow.entity.Screen;
import com.raj.bookmyshow.entity.Show;
import com.raj.bookmyshow.entity.ShowSeat;
import com.raj.bookmyshow.exception.ResourceNotFoundException;
import com.raj.bookmyshow.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final ShowSeatRepository showSeatRepository;

    public ShowDto createShow(ShowDto showDto){
        Show show = new Show();
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Movie Not Found with id "+ showDto.getMovie().getId()));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Screen Not Found with id " + showDto.getScreen().getId()));

        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");
        return mapToDto(savedShow, availableSeats);
    }

    public  ShowDto getShowById(Long id){
        Show show = showRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Show not Found with id "+id));
        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, availableSeats);
    }

    public List<ShowDto> getAllShow(){
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> available = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, available);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getByMovieId(Long movieId){
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> available = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, available);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getByScreenId(Long screenId){
        List<Show> shows = showRepository.findByMovieId(screenId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> available = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, available);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getByMovieAndCity(Long movieId, String city){
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId, city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> available = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, available);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getByDateRange(LocalDateTime start, LocalDateTime end){
        List<Show> shows = showRepository.findByStartTimeBetween(start, end);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> available = showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show, available);
                })
                .collect(Collectors.toList());
    }

    private ShowDto mapToDto(Show show, List<ShowSeat> availableSeats){
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setEndTime(show.getEndTime());
        showDto.setStartTime(show.getStartTime());
        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));
        TheaterDto theaterDto = new TheaterDto(
                show.getScreen().getTheater().getId(),
                show.getScreen().getTheater().getName(),
                show.getScreen().getTheater().getAddress(),
                show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreens()
        );

        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));

        List<ShowSeatDto> seatDtos = availableSeats.stream()
                .map(seat-> {
                    ShowSeatDto showSeatDto = new ShowSeatDto();
                    showSeatDto.setId(seat.getId());
                    showSeatDto.setPrice(seat.getPrice());
                    showSeatDto.setStatus(seat.getStatus());

                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    showSeatDto.setSeat(baseSeatDto);
                    return showSeatDto;
                })
                .collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);

        return showDto;
    }
}
