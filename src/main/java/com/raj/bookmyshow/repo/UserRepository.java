package com.raj.bookmyshow.repo;

import com.raj.bookmyshow.entity.Booking;
import com.raj.bookmyshow.entity.Theater;
import com.raj.bookmyshow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}
