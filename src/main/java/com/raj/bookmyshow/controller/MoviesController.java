package com.raj.bookmyshow.controller;

import com.raj.bookmyshow.dto.BookingDto;
import com.raj.bookmyshow.dto.BookingRequestDto;
import com.raj.bookmyshow.dto.MovieDto;
import com.raj.bookmyshow.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto){
        return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies()
    {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<MovieDto>> getAllMoviesByLanguage(String language)
    {
        return ResponseEntity.ok(movieService.getAllMoviesByLanguage(language));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MovieDto>> getAllMoviesByGenre(String genre)
    {
        return ResponseEntity.ok(movieService.getAllMoviesByGenre(genre));
    }

    @GetMapping("/title")
    public ResponseEntity<List<MovieDto>> getAllMoviesByTitleContained(String title)
    {
        return ResponseEntity.ok(movieService.getAllMoviesByTitleContain(title));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto){
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping
    public void deleteMovie(Long id){
        movieService.deletedMovie(id);
        System.out.println("Movie Deleted Successfully of id: "+ id);
    }
}
