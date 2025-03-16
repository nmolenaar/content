package com.molenaar.content.config;

import com.molenaar.content.module.user.UserDomain;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", "vertx-reactive:postgresql://10.10.3.110:5432/molenaar")
                .applySetting("hibernate.connection.username", "postgres")
                .applySetting("hibernate.connection.password", "postgres")
                .applySetting("hibernate.connection.provider_class", "org.hibernate.reactive.pool.impl.DefaultReactiveConnectionPool")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop")
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.reactive.pool.class", "org.hibernate.reactive.pool.impl.SqlClientPool")
                .applySetting("hibernate.reactive.client.pool.size", "10")
                .build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(UserDomain.User.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build()
                .unwrap(Mutiny.SessionFactory.class);
    }
}
