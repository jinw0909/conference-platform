package com.blocksquare.conference.domain.conference.repository;

import com.blocksquare.conference.domain.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    Optional<Conference> findBySlug(String slug);
}
