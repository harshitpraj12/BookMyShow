package com.raj.bookmyshow.repo;

import com.raj.bookmyshow.entity.Booking;
import com.raj.bookmyshow.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    List<Screen> findByTheaterId(Long theaterId);

}
