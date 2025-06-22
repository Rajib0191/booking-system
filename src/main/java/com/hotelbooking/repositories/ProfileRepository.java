package com.hotelbooking.repositories;

import com.hotelbooking.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long > {
    Profile findByUserId(Long userId);
}
