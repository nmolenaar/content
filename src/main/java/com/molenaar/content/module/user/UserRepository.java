package com.molenaar.content.module.user;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Mutiny.SessionFactory sessionFactory;

    public UserRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Uni<UserDomain.User> findById(Long id) {
        return sessionFactory.withSession(session ->
                session.find(UserDomain.User.class, id)
        );
    }
}
