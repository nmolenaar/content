package com.molenaar.content.module.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;
import java.util.UUID;

public final class UserDomain {
    @Table("users")
    public record UserRecord(
            @Id UUID id,
            @Column("username") String username,
            @Column("password") String password
    ) {}
}
