package com.blocksquare.conference.domain.booth.repository;

import com.blocksquare.conference.domain.booth.entity.BoothApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothApplicationRepository extends JpaRepository<BoothApplication, Long> {
}
