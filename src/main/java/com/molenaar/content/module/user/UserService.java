package com.molenaar.content.module.user;

import io.jbock.util.Either;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public final class UserService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private final UserRepository userRepository;

    public Mono<Optional<UserDomain.UserRecord>> getByUsername(String username) {
        return userRepository.findFirstByUsername(username).mapNotNull(Optional::of).switchIfEmpty(Mono.just(Optional.empty()));
    }

    public Mono<Either<String, UserDomain.UserRecord>> persist(UserDomain.UserRecord user) {
        return userRepository.save(user)
            .mapNotNull(Either::<String, UserDomain.UserRecord>right)
            .onErrorResume(throwable -> Mono.just(Either.<String, UserDomain.UserRecord>left(throwable.getMessage())))
            .switchIfEmpty(Mono.just(Either.<String, UserDomain.UserRecord>left("User not found or could not be saved")));
    }
}
