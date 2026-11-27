package com.blocksquare.conference.user.service;

import com.blocksquare.conference.domain.conference.entity.Conference;
import com.blocksquare.conference.domain.conference.repository.ConferenceRepository;
import com.blocksquare.conference.domain.speaker.entity.SpeakerApplication;
import com.blocksquare.conference.domain.speaker.repository.ConferenceSpeakerRepository;
import com.blocksquare.conference.domain.speaker.repository.SpeakerApplicationRepository;
import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.SpeakerApplicationRequest;
import com.blocksquare.conference.user.dto.SpeakerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSpeakerService {

    private final ConferenceRepository conferenceRepository;
    private final ConferenceSpeakerRepository conferenceSpeakerRepository;
    private final SpeakerApplicationRepository speakerApplicationRepository;

    public List<SpeakerResponse> getSpeakers(Long conferenceId) {

        if (!conferenceRepository.existsById(conferenceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 컨퍼런스입니다."
            );
        }

        return conferenceSpeakerRepository
                .findPublishedSpeakers(conferenceId)
                .stream()
                .map(SpeakerResponse::from)
                .toList();
    }

    @Transactional
    public ApplicationResponse apply(
            Long conferenceId,
            SpeakerApplicationRequest request
    ) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 컨퍼런스입니다."
                ));

        if (!conference.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "현재 신청을 받지 않는 컨퍼런스입니다."
            );
        }

        String socialUrl = request.socialUrl();

        if (socialUrl != null) {
            socialUrl = socialUrl.trim();

            if (socialUrl.isEmpty()) {
                socialUrl = null;
            }
        }

        SpeakerApplication application = new SpeakerApplication(
                conference,
                request.name().trim(),
                request.companyName().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.phone().trim(),
                socialUrl,
                request.presentationContent().trim(),
                request.privacyAgreed()
        );

        SpeakerApplication saved =
                speakerApplicationRepository.save(application);

        return new ApplicationResponse(
                saved.getId(),
                saved.getStatus()
        );

    }
}
