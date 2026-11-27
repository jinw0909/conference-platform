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
        name = "partner_category",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_partner_category_conference_name",
                        columnNames = {"conference_id", "name"}
                )
        }

)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnerCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int displayOrder;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public PartnerCategory(Conference conference, String name, int displayOrder) {
        this.conference = conference;
        this.name = name;
        this.displayOrder = displayOrder;
    }
}
