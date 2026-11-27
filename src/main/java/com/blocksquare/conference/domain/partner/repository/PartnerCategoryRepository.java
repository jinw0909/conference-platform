package com.blocksquare.conference.domain.partner.repository;

import com.blocksquare.conference.domain.partner.entity.PartnerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerCategoryRepository extends JpaRepository<PartnerCategory, Long> {
}
