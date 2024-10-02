package com.DemoKeyCloak.KeyCloak.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private ZonedDateTime timestamp;
    private String message;
    private int statusCode;
}
