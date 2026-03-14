package com.raj.bookmyshow.service;

import com.raj.bookmyshow.dto.TheaterDto;
import com.raj.bookmyshow.entity.Theater;
import com.raj.bookmyshow.exception.ResourceNotFoundException;
import com.raj.bookmyshow.repo.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    public TheaterDto createTheater(TheaterDto theaterDto){
        Theater theater = mapToEntity(theaterDto);
        Theater saveTheater = theaterRepository.save(theater);
        return mapToDto(saveTheater);
    }

    public TheaterDto getTheaterById(Long id){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Theater not found with id "+ id));

        return mapToDto(theater);
    }

    public List<TheaterDto> getByTheaterId(Long id){
        Optional<Theater> theaters = theaterRepository.findById(id);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    public List<TheaterDto> getByCity(String city){
        List<Theater> theaters = theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    public List<TheaterDto> getAll(){
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TheaterDto updateTheater(Long id, TheaterDto theaterDto){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Theater not found with id : "+ id));

        theater.setId(theaterDto.getId());
        theater.setCity(theaterDto.getCity());
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreens(theaterDto.getTotalScreen());

        Theater updateTheater = theaterRepository.save(theater);

        return mapToDto(updateTheater);
    }

    public void deleteTheater(Long id){
        Theater theater = theaterRepository.findById(id)
                        .orElseThrow(()-> new ResourceNotFoundException("Theater not found with id "+id));

        theaterRepository.delete(theater);
    }

    public TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setTotalScreen(theater.getTotalScreens());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setCity(theater.getCity());
        return theaterDto;
    }

    public Theater mapToEntity(TheaterDto theaterDto){
        Theater theater = new Theater();
        theater.setId(theaterDto.getId());
        theater.setCity(theaterDto.getCity());
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreens(theaterDto.getTotalScreen());
        return theater;
    }
}
