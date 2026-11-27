package com.blocksquare.conference.user.dto;

import jakarta.validation.constraints.*;

public record SpeakerApplicationRequest(
        @NotBlank(message = "성함을 입력해주세요.")
        @Size(max = 100)
        String name,

        @NotBlank(message = "회사/프로젝트명을 입력해주세요.")
        @Size(max = 200)
        String companyName,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일을 입력해주세요.")
        @Size(max = 255)
        String email,

        @NotBlank(message = "연락처를 입력해주세요.")
        @Size(max = 30)
        String phone,

        @Size(max = 1000)
        @Pattern(
                regexp = "^(?:https?://\\S+)?$",
                message = "올바른 소셜 미디어 URL을 입력해주세요."
        )
        String socialUrl,

        @NotBlank(message = "강연 주제 및 내용을 입력해주세요.")
        @Size(max = 5000)
        String presentationContent,

        @AssertTrue(message = "개인정보 수집 및 이용에 동의해주세요.")
        boolean privacyAgreed

) {
}
