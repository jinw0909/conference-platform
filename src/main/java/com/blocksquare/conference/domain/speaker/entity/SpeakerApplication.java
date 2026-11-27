package com.blocksquare.conference.domain.speaker.entity;

import com.blocksquare.conference.domain.common.ApplicationStatus;
import com.blocksquare.conference.domain.conference.entity.Conference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "speaker_application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpeakerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(length = 1000)
    private String socialUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String presentationContent;

    @Column(nullable = false)
    private boolean privacyAgreed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Instant emailVerifiedAt;

    public SpeakerApplication(
            Conference conference,
            String name,
            String companyName,
            String email,
            String phone,
            String socialUrl,
            String presentationContent,
            boolean privacyAgreed
    ) {
        this.conference = conference;
        this.name = name;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.socialUrl = socialUrl;
        this.presentationContent = presentationContent;
        this.privacyAgreed = privacyAgreed;
    }

    public boolean isEmailVerified() {
        return emailVerifiedAt != null;
    }

    public void verifyEmail(Instant now) {
        if (!isEmailVerified()) {
            this.emailVerifiedAt = now;
        }
    }
}
