package com.blocksquare.conference.domain.partner.entity;

import com.blocksquare.conference.domain.conference.entity.Conference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "conference_partner",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_conference_partner",
                columnNames = {"conference_id", "partner_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConferencePartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private PartnerCategory category;

    @Column(nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean published = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public ConferencePartner(Conference conference, Partner partner, PartnerCategory category, int displayOrder) {

        if (conference.getId() == null || !conference.getId().equals(category.getConference().getId())) {
            throw new IllegalArgumentException("해당 컨퍼런스의 파트너 분류가 아닙니다.");
        }


        this.conference = conference;
        this.partner = partner;
        this.category = category;
        this.displayOrder = displayOrder;
    }

    public void publish() {
        this.published = true;
    }

    public void unpublish() {
        this.published = false;
    }
}
