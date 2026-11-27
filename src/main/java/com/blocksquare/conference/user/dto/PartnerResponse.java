package com.blocksquare.conference.user.dto;

import com.blocksquare.conference.domain.partner.entity.ConferencePartner;
import com.blocksquare.conference.domain.partner.entity.Partner;

public record PartnerResponse(
        Long id,
        String name,
        String logoUrl,
        String websiteUrl,
        String description,
        int displayOrder
) {
    public static PartnerResponse from(ConferencePartner conferencePartner) {
        Partner partner = conferencePartner.getPartner();
        return new PartnerResponse(
                partner.getId(),
                partner.getName(),
                partner.getLogoUrl(),
                partner.getWebsiteUrl(),
                partner.getDescription(),
                conferencePartner.getDisplayOrder()
        );
    }
}
