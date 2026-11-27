package com.blocksquare.conference.domain.speaker.repository;

import com.blocksquare.conference.domain.speaker.entity.SpeakerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerApplicationRepository extends JpaRepository<SpeakerApplication, Long> {
}
