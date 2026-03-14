package com.raj.bookmyshow.repo;

import com.raj.bookmyshow.entity.Booking;
import com.raj.bookmyshow.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByScreenId(Long theaterId);

    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieId, String city);

}
