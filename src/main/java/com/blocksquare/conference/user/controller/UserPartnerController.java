package com.blocksquare.conference.user.controller;

import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.PartnerApplicationRequest;
import com.blocksquare.conference.user.dto.PartnerCategoryResponse;
import com.blocksquare.conference.user.service.UserPartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/conferences")
public class UserPartnerController {

    private final UserPartnerService partnerService;

    @PostMapping("/{conferenceId}/partner-applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(
            @PathVariable("conferenceId") Long conferenceId,
            @Valid @RequestBody PartnerApplicationRequest request
    ) {
        return partnerService.apply(conferenceId, request);
    }

    @GetMapping("/{conferenceId}/partners")
    public List<PartnerCategoryResponse> getPartners(
            @PathVariable("conferenceId") Long conferenceId
    ) {
        return partnerService.getPartners(conferenceId);
    }
}
