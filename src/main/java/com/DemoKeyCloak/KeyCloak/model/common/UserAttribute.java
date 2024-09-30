package com.DemoKeyCloak.KeyCloak.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;

public class UserAttribute {

    private Long userId;

    public String toJson() throws JsonProcessingException {
        return (new ObjectMapper()).writeValueAsString(this);
    }

    public UserAttribute(final Long userId) {
        this.userId = userId;
    }

    public UserAttribute() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
