package com.blocksquare.conference.global.exception;

import java.util.Map;

public record ErrorResponse(
        int status,
        String detail,
        Map<String, String> errors
) {
    public ErrorResponse(int status, String detail) {
        this(status, detail, Map.of());
    }

}
