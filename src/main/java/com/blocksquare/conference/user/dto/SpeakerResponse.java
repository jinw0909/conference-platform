package com.blocksquare.conference.user.dto;

import com.blocksquare.conference.domain.speaker.entity.ConferenceSpeaker;
import com.blocksquare.conference.domain.speaker.entity.Speaker;

public record SpeakerResponse(
        Long id,
        String name,
        String englishName,
        String publicTitle,
        String profileImageUrl,
        String xUrl,
        String youtubeUrl,
        String facebookUrl,
        Integer displayOrder
) {
    public static SpeakerResponse from(ConferenceSpeaker conferenceSpeaker) {
        Speaker speaker = conferenceSpeaker.getSpeaker();

        return new SpeakerResponse(
                speaker.getId(),
                speaker.getName(),
                speaker.getEnglishName(),
                conferenceSpeaker.getPublicTitle(),
                speaker.getProfileImageUrl(),
                speaker.getXUrl(),
                speaker.getYoutubeUrl(),
                speaker.getFacebookUrl(),
                conferenceSpeaker.getDisplayOrder()
        );
    }
}
