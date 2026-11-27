package com.blocksquare.conference.user.service;

import com.blocksquare.conference.domain.partner.repository.PartnerApplicationRepository;
import com.blocksquare.conference.domain.speaker.repository.SpeakerApplicationRepository;
import com.blocksquare.conference.domain.verification.entity.ApplicationEmailVerification;
import com.blocksquare.conference.domain.verification.enums.ApplicationType;
import com.blocksquare.conference.domain.verification.repository.ApplicationEmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationEmailVerificationService {

    private static final Duration TOKEN_EXPIRATION =
            Duration.ofHours(24);

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final ApplicationEmailVerificationRepository
            verificationRepository;

    private final PartnerApplicationRepository
            partnerApplicationRepository;

    private final SpeakerApplicationRepository
            speakerApplicationRepository;

    @Transactional
    public String issueInitialToken(
            ApplicationType applicationType,
            Long applicationId
    ) {
        String rawToken = generateToken();
        String tokenHash = hashToken(rawToken);

        Instant expiresAt = Instant.now()
                .plus(TOKEN_EXPIRATION);

        ApplicationEmailVerification verification =
                new ApplicationEmailVerification(
                        applicationType,
                        applicationId,
                        tokenHash,
                        expiresAt
                );

        verificationRepository.save(verification);

        return rawToken;
    }

    @Transactional
    public void verify(String rawToken) {

        if (rawToken == null
                || !rawToken.matches("[A-Za-z0-9_-]{43}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "유효하지 않은 인증 링크입니다."
            );
        }

        String tokenHash = hashToken(rawToken);

        ApplicationEmailVerification verification =
                verificationRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "유효하지 않은 인증 링크입니다."
                                )
                        );

        if (verification.isInvalidated()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "무효화된 인증 링크입니다."
            );
        }

        if (verification.isVerified()) {
            return;
        }

        Instant now = Instant.now();

        if (verification.isExpired(now)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "인증 링크가 만료되었습니다."
            );
        }

        switch (verification.getApplicationType()) {
            case PARTNER -> {
                var application = partnerApplicationRepository
                        .findById(verification.getApplicationId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "파트너 신청 정보를 찾을 수 없습니다."
                                )
                        );

                application.verifyEmail(now);
            }

            case SPEAKER -> {
                var application = speakerApplicationRepository
                        .findById(verification.getApplicationId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "연사 신청 정보를 찾을 수 없습니다."
                                )
                        );

                application.verifyEmail(now);
            }
        }

        verification.markVerified(now);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashed = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hashed);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "토큰 해싱에 실패했습니다.", e
            );
        }
    }
}