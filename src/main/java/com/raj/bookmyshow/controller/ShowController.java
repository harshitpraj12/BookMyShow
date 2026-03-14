package com.raj.bookmyshow.controller;

import com.raj.bookmyshow.dto.ShowDto;
import com.raj.bookmyshow.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/show")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService service;

    @PostMapping
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto showDto){
        return ResponseEntity.ok(service.createShow(showDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id){
        return ResponseEntity.ok(service.getShowById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShowDto>> getAllShow(){
        return ResponseEntity.ok(service.getAllShow());
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ShowDto>> getAllByMovieId(@PathVariable Long movieId){
        return ResponseEntity.ok(service.getByMovieId(movieId));
    }

    @GetMapping("/city")
    public ResponseEntity<List<ShowDto>> getByMovieAndCity(Long movieId, String city){
        return ResponseEntity.ok(service.getByMovieAndCity(movieId, city));
    }

    @GetMapping("/from")
    public ResponseEntity<List<ShowDto>> getByDateRange(LocalDateTime start, LocalDateTime end){
        return ResponseEntity.ok(service.getByDateRange(start, end));
    }

}
