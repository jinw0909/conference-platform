package com.blocksquare.conference.user.service;

import com.blocksquare.conference.domain.partner.entity.ConferencePartner;
import com.blocksquare.conference.domain.partner.entity.PartnerApplication;
import com.blocksquare.conference.domain.partner.entity.PartnerCategory;
import com.blocksquare.conference.domain.partner.repository.ConferencePartnerRepository;
import com.blocksquare.conference.domain.partner.repository.PartnerApplicationRepository;
import com.blocksquare.conference.domain.conference.entity.Conference;
import com.blocksquare.conference.domain.conference.repository.ConferenceRepository;
import com.blocksquare.conference.domain.verification.enums.ApplicationType;
import com.blocksquare.conference.domain.verification.event.ApplicationEmailVerificationEvent;
import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.PartnerApplicationRequest;
import com.blocksquare.conference.user.dto.PartnerCategoryResponse;
import com.blocksquare.conference.user.dto.PartnerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPartnerService {

    private final ConferenceRepository conferenceRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final ConferencePartnerRepository conferencePartnerRepository;
    private final ApplicationEmailVerificationService verificationService;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public ApplicationResponse apply(
            Long conferenceId,
            PartnerApplicationRequest request
    ) {
        Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 컨퍼런스입니다")
        );

        if (!conference.isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "현재 신청을 받지 않는 컨퍼런스입니다.");
        }

        PartnerApplication application = new PartnerApplication(
                conference,
                request.companyName().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.phone().trim(),
                request.contactName().trim(),
                request.operationPlan().trim(),
                request.privacyAgreed()
        );

        PartnerApplication saved = partnerApplicationRepository.save(application);

        String rawToken = verificationService.issueInitialToken(
                ApplicationType.PARTNER,
                saved.getId()
        );

        eventPublisher.publishEvent(
                new ApplicationEmailVerificationEvent(
                        ApplicationType.PARTNER,
                        saved.getId(),
                        conference.getName(),
                        saved.getContactName(),
                        saved.getEmail(),
                        rawToken
                )
        );

        return new ApplicationResponse(
                saved.getId(),
                saved.getStatus()
        );

    }
    public List<PartnerCategoryResponse> getPartners(Long conferenceId) {

        if (!conferenceRepository.existsById(conferenceId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 컨퍼런스입니다."
            );
        }

        List<ConferencePartner> partners =
                conferencePartnerRepository
                        .findPublishedPartners(conferenceId);

        return partners.stream()
                .collect(Collectors.groupingBy(
                        cp -> cp.getCategory().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(group -> {
                    PartnerCategory category =
                            group.getFirst().getCategory();

                    List<PartnerResponse> partnerResponses =
                            group.stream()
                                    .map(PartnerResponse::from)
                                    .toList();

                    return PartnerCategoryResponse.from(
                            category,
                            partnerResponses
                    );
                })
                .toList();
    }

}
