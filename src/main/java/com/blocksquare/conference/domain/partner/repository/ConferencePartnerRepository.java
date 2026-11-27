package com.blocksquare.conference.domain.partner.repository;

import com.blocksquare.conference.domain.partner.entity.ConferencePartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferencePartnerRepository extends JpaRepository<ConferencePartner, Long> {

    @Query("""
            SELECT cp
            FROM ConferencePartner cp
            JOIN FETCH cp.partner
            JOIN FETCH cp.category pc
            WHERE cp.conference.id = :conferenceId
              AND pc.conference.id = :conferenceId
              AND cp.published = true
            ORDER BY pc.displayOrder ASC,
                     pc.id ASC,
                     cp.displayOrder ASC,
                     cp.id ASC
            """)
    List<ConferencePartner> findPublishedPartners(
            @Param("conferenceId") Long conferenceId
    );
}
