package com.blocksquare.conference.user.controller;

import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.SpeakerApplicationRequest;
import com.blocksquare.conference.user.dto.SpeakerResponse;
import com.blocksquare.conference.user.service.UserSpeakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/conferences")
public class UserSpeakerController {

    private final UserSpeakerService speakerService;

    @GetMapping("/{conferenceId}/speakers")
    public List<SpeakerResponse> getSpeakers(@PathVariable Long conferenceId) {
        return speakerService.getSpeakers(conferenceId);
    }

    @PostMapping("/{conferenceId}/speaker-applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(
            @PathVariable Long conferenceId,
            @Valid @RequestBody SpeakerApplicationRequest request
    ) {
        return speakerService.apply(conferenceId, request);
    }


}
