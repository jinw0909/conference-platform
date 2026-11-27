package com.blocksquare.conference.domain.verification.repository;

import com.blocksquare.conference.domain.verification.entity.ApplicationEmailVerification;
import com.blocksquare.conference.domain.verification.enums.ApplicationType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationEmailVerificationRepository extends JpaRepository<ApplicationEmailVerification, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ApplicationEmailVerification> findByTokenHash(String tokenHash);

    @Query("""
        SELECT v
        FROM ApplicationEmailVerification v
        WHERE v.applicationType = :applicationType
          AND v.applicationId = :applicationId
          AND v.verifiedAt IS NULL
          AND v.invalidatedAt IS NULL
        """)
    List<ApplicationEmailVerification> findUnconsumedVerifications(
            @Param("applicationType") ApplicationType applicationType,
            @Param("applicationId") Long applicationId
    );

    Optional<ApplicationEmailVerification> findFirstByApplicationTypeAndApplicationIdOrderByCreatedAtDescIdDesc(
            ApplicationType applicationType,
            Long applicationId
    );
}
