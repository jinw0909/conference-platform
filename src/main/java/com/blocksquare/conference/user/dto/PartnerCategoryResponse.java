package com.blocksquare.conference.user.dto;

import com.blocksquare.conference.domain.partner.entity.PartnerCategory;

import java.util.List;

public record PartnerCategoryResponse(
        Long categoryId,
        String categoryName,
        int displayOrder,
        List<PartnerResponse> partners
) {

    public static PartnerCategoryResponse from(PartnerCategory category,
                                               List<PartnerResponse> partners) {
        return new PartnerCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDisplayOrder(),
                partners
        );
    }
}
