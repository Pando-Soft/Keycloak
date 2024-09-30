package com.DemoKeyCloak.KeyCloak.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = AdminUser.TABLE_NAME)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // Ensure the primary key joins with the parent
public class AdminUser extends User {
    protected static final String TABLE_NAME = "admin_user";

}
