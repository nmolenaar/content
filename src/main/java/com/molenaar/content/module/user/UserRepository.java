package com.molenaar.content.module.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserDomain.UserRecord, UUID> {
    Mono<UserDomain.UserRecord> findFirstByUsername(String username);
}


