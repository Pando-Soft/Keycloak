package com.DemoKeyCloak.KeyCloak.model.entity;

import com.DemoKeyCloak.KeyCloak.model.common.Location;
import com.DemoKeyCloak.KeyCloak.model.enums.GenderEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = EndUser.TABLE_NAME)
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class EndUser extends User {

    protected static final String TABLE_NAME = "end_user";

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "location_list")
    private List<Location> locationList = new ArrayList<>();

    @Column(name = "selected_location_uuid")
    private UUID selectedLocationUuid;

    private Double rating;

    private LocalDate dob;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private GenderEnum gender;

    @Column(name = "newsletter_subscription_email")
    private String newsletterSubscriptionEmail;
}
