package com.blocksquare.conference.domain.verification.event;

import com.blocksquare.conference.domain.verification.enums.ApplicationType;

public record ApplicationEmailVerificationEvent(
        ApplicationType applicationType,
        Long applicationId,
        String conferenceName,
        String applicantName,
        String email,
        String rawToken
) {

    @Override
    public String toString() {
        return "ApplicationEmailVerificationEvent[" +
                "applicationType=" + applicationType +
                ", applicationId=" + applicationId +
                "]";
    }
}