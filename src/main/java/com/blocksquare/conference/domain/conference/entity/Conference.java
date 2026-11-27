package com.blocksquare.conference.domain.conference.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conference")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private boolean active = true;

}
