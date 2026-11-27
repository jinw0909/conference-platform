package com.blocksquare.conference.domain.partner.repository;

import com.blocksquare.conference.domain.partner.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
