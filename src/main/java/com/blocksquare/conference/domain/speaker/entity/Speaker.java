package com.blocksquare.conference.domain.speaker.entity;

import com.blocksquare.conference.domain.conference.entity.Conference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "speaker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String englishName;

    @Column(length = 1000)
    private String profileImageUrl;

    @Column(length = 500)
    private String xUrl;

    @Column(length = 500)
    private String youtubeUrl;

    @Column(length = 500)
    private String facebookUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
