package com.blocksquare.conference.domain.booth.entity;

import com.blocksquare.conference.domain.common.ApplicationStatus;
import com.blocksquare.conference.domain.conference.entity.Conference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "booth_application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 100)
    private String contactName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String operationPlan;

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

    public BoothApplication(
            Conference conference,
            String companyName,
            String email,
            String phone,
            String contactName,
            String operationPlan,
            boolean privacyAgreed
    ) {
        this.conference = conference;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.contactName = contactName;
        this.operationPlan = operationPlan;
        this.privacyAgreed = privacyAgreed;
    }

}
