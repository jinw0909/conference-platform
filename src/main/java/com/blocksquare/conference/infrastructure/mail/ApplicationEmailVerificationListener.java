package com.blocksquare.conference.infrastructure.mail;

import com.blocksquare.conference.domain.verification.event.ApplicationEmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationEmailVerificationListener {

    private final MailService mailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(ApplicationEmailVerificationEvent event) {

        try {
            String applicationType = switch (event.applicationType()) {
                case PARTNER -> "파트너";
                case SPEAKER -> "연사";
            };

            String verificationUrl = UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path("/email-verifications/confirm")
                    .queryParam("token", event.rawToken())
                    .build()
                    .toUriString();

            String subject = "[" + event.conferenceName()
                    + "] 이메일 인증 안내";

            String html = mailService.renderTemplate(
                    "mail/application-email-verification",
                    Map.of(
                            "conferenceName", event.conferenceName(),
                            "applicantName", event.applicantName(),
                            "applicationType", applicationType,
                            "verificationUrl", verificationUrl
                    )
            );

            String plainText = """
                    신청이 정상적으로 접수되었습니다.

                    아래 링크를 통해 이메일을 인증해주세요.

                    %s

                    인증 링크는 24시간 동안 유효합니다.
                    """.formatted(verificationUrl);

            mailService.sendHtmlEmail(
                    event.email(),
                    subject,
                    plainText,
                    html
            );

            log.info(
                    "인증 메일 발송 성공: type={}, applicationId={}",
                    event.applicationType(),
                    event.applicationId()
            );

        } catch (RuntimeException e) {
            log.error(
                    "인증 메일 발송 실패: type={}, applicationId={}, errorType={}",
                    event.applicationType(),
                    event.applicationId(),
                    e.getClass().getSimpleName()
            );
        }
    }
}