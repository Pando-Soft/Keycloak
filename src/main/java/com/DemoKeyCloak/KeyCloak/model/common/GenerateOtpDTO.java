package com.DemoKeyCloak.KeyCloak.model.common;

import java.time.ZonedDateTime;

public record GenerateOtpDTO(
    ZonedDateTime expirationTime
) {
}
