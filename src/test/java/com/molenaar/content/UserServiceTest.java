package com.molenaar.content;


import com.molenaar.content.module.user.UserDomain.UserRecord;
import com.molenaar.content.module.user.UserService;
import io.jbock.util.Either;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseClient databaseClient;

    private final Faker faker = new Faker();

    private String email = faker.internet().emailAddress();
    private String password = faker.internet().password();

    @BeforeEach
    void setUp() {
        databaseClient.sql("DELETE FROM users")
                .fetch()
                .rowsUpdated()
                .block();
    }

    @Test
    void createUserTest() {
        Mono<Either<String, UserRecord>> user = userService.persist(new UserRecord(null, email, password));

        StepVerifier.create(user)
                .expectNextMatches(userOpt -> userOpt.isRight() &&
                        userOpt.getRight().isPresent() &&
                        userOpt.getRight().get().id() != null &&
                        userOpt.getRight().get().username().equals(email) &&
                        userOpt.getRight().get().password().equals(password))
                .verifyComplete();
    }

    @Test
    void updateUserTest() {
        userService.persist(new UserRecord(null, email, password)).block();

        Mono<Optional<UserRecord>> user = userService.getByUsername(email);
        String newPassword = faker.internet().password();

        StepVerifier.create(user)
                .expectNextMatches(Optional::isPresent)
                .verifyComplete();

        Mono<Either<String, UserRecord>> result = user
                .map(Optional::get)
                .map(u -> new UserRecord(u.id(), u.username(), newPassword))
                .flatMap(userService::persist)
                .onErrorResume(ex -> Mono.just(Either.left("An error occurred: " + ex.getMessage())))
                .switchIfEmpty(Mono.just(Either.left("User is empty")));

        StepVerifier.create(result)
                .expectNextMatches(userOpt -> userOpt.isRight() && userOpt.getRight().get().password().equals(newPassword))
                .verifyComplete();

    }
}