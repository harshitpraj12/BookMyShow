package com.raj.bookmyshow.controller;

import com.raj.bookmyshow.dto.TheaterDto;
import com.raj.bookmyshow.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theater")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto){
        return ResponseEntity.ok(theaterService.createTheater(theaterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getMappingById(@PathVariable Long id){
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }

    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAll(){
        return ResponseEntity.ok(theaterService.getAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable Long id, @RequestBody TheaterDto theaterDto){
        return ResponseEntity.ok(theaterService.updateTheater(id, theaterDto));
    }

    @DeleteMapping("/{id}")
    public void deleteTheater(@PathVariable Long id){
        theaterService.deleteTheater(id);
        System.out.println("Theater Deleted Successfully of id: "+ id);    }

}
