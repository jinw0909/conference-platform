package com.blocksquare.conference.user.service;

import com.blocksquare.conference.domain.booth.entity.BoothApplication;
import com.blocksquare.conference.domain.booth.repository.BoothApplicationRepository;
import com.blocksquare.conference.domain.conference.entity.Conference;
import com.blocksquare.conference.domain.conference.repository.ConferenceRepository;
import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.BoothApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoothService {

    private final ConferenceRepository conferenceRepository;
    private final BoothApplicationRepository boothApplicationRepository;

    @Transactional
    public ApplicationResponse apply(
            Long conferenceId,
            BoothApplicationRequest request
    ) {
        Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 컨퍼런스입니다")
        );

        if (!conference.isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "현재 신청을 받지 않는 컨퍼런스입니다.");
        }

        BoothApplication application = new BoothApplication(
                conference,
                request.companyName().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.phone().trim(),
                request.contactName().trim(),
                request.operationPlan().trim(),
                request.privacyAgreed()
        );

        BoothApplication saved = boothApplicationRepository.save(application);

        return new ApplicationResponse(saved.getId(), saved.getStatus());
    }
}
