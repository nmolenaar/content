package com.molenaar.content.module.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public final class UserDomain {

    @Entity
    public record User(
            @Id Long id,
            String name,
            String email
    ) { }

}
