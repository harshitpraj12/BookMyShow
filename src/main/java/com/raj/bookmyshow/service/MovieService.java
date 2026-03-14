package com.raj.bookmyshow.service;

import com.raj.bookmyshow.dto.MovieDto;
import com.raj.bookmyshow.entity.Movie;
import com.raj.bookmyshow.entity.Show;
import com.raj.bookmyshow.exception.ResourceNotFoundException;
import com.raj.bookmyshow.repo.MovieRepository;
import com.raj.bookmyshow.repo.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public MovieDto createMovie(MovieDto movieDto){
        Movie movie = mapToEntity(movieDto);
        Movie saveMovie = movieRepository.save(movie);

        return mapToDto(saveMovie);
    }

    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie Not Found with id "+ id));

        return mapToDto(movie);
    }

    public List<MovieDto> getAllMovies(){
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getAllMoviesByLanguage(String lang){
        List<Movie> movies = movieRepository.findByLanguage(lang);

        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getAllMoviesByGenre(String genre){
        List<Movie> movies = movieRepository.findByGenre(genre);

        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getAllMoviesByTitleContain(String title){
        List<Movie> movies = movieRepository.findByTitleContaining(title);

        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie Not Found with id "+ id));

        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setTitle(movieDto.getTitle());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToDto(updatedMovie);
    }

    public void deletedMovie(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Movie Not Found with id "+ id));
        movieRepository.delete(movie);
    }

    public MovieDto mapToDto(Movie movie){
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setGenre(movie.getGenre());
        movieDto.setTitle(movie.getTitle());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setDescription(movie.getDescription());
        movieDto.setPosterUrl(movie.getPosterUrl());
        movieDto.setReleaseDate(movie.getReleaseDate());

        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto){
        Movie movie = new Movie();

        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setTitle(movieDto.getTitle());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        return  movie;

    }

}
