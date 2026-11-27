package com.blocksquare.conference.user.controller;

import com.blocksquare.conference.user.dto.ApplicationResponse;
import com.blocksquare.conference.user.dto.BoothApplicationRequest;
import com.blocksquare.conference.user.service.UserBoothService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/conferences")
public class UserBoothController {

    private final UserBoothService boothService;

    @PostMapping("/{conferenceId}/booth-applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(
            @PathVariable("conferenceId") Long conferenceId,
            @Valid @RequestBody BoothApplicationRequest request
    ) {
        return boothService.apply(conferenceId, request);
    }
}
