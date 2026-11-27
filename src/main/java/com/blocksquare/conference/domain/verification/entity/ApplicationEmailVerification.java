package com.blocksquare.conference.domain.verification.entity;

import com.blocksquare.conference.domain.verification.enums.ApplicationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "application_email_verification",
        indexes = {
                @Index(
                        name = "idx_verification_application",
                        columnList = "application_type, application_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationEmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", nullable = false, length = 20)
    private ApplicationType applicationType;

    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(nullable = false)
    private Instant expiresAt;

    private Instant verifiedAt;

    private Instant invalidatedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public ApplicationEmailVerification(ApplicationType applicationType, Long applicationId, String tokenHash, Instant expiresAt) {
        this.applicationType = applicationType;
        this.applicationId = applicationId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired(Instant now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isVerified() {
        return verifiedAt != null;
    }

    public boolean isInvalidated() {
        return invalidatedAt != null;
    }

    public void markVerified(Instant now) {
        if (isVerified()) {
            return;
        }

        if (isExpired(now) || isInvalidated()) {
            throw new IllegalStateException(
                    "유효하지 않은 이메일 인증입니다."
            );
        }

        this.verifiedAt = now;
    }

    public void invalidate(Instant now) {
        if (!isVerified() && !isInvalidated()) {
            this.invalidatedAt = now;
        }
    }

}
