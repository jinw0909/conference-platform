package com.blocksquare.conference.domain.speaker.repository;

import com.blocksquare.conference.domain.speaker.entity.ConferenceSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferenceSpeakerRepository extends JpaRepository<ConferenceSpeaker, Long> {
    @Query("""
        SELECT cs
        FROM ConferenceSpeaker cs
        JOIN FETCH cs.speaker
        WHERE cs.conference.id = :conferenceId
          AND cs.published = true
        ORDER BY cs.displayOrder ASC, cs.id ASC
        """)
    List<ConferenceSpeaker> findPublishedSpeakers(
            @Param("conferenceId") Long conferenceId
    );
}
