package com.blocksquare.conference.user.dto;

import com.blocksquare.conference.domain.common.ApplicationStatus;

public record ApplicationResponse (
        Long id,
        ApplicationStatus status
) {}
