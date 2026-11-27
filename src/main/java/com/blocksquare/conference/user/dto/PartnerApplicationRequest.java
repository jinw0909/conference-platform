package com.blocksquare.conference.user.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PartnerApplicationRequest(

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

        @NotBlank(message = "담당자 성함을 입력해주세요.")
        @Size(max = 100)
        String contactName,

        @NotBlank(message = "참가 및 활동 계획을 입력해주세요.")
        @Size(max = 5000)
        String operationPlan,

        @AssertTrue(message = "개인정보 수집 및 이용에 동의해주세요.")
        boolean privacyAgreed

){}
