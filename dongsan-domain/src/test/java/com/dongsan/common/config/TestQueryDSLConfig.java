package com.dongsan.common.config;

import com.dongsan.domains.review.repository.ReviewQueryDSLRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestQueryDSLConfig {
    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public ReviewQueryDSLRepository reviewQueryDSLRepository(JPAQueryFactory jpaQueryFactory){
        return new ReviewQueryDSLRepository(jpaQueryFactory);
    }
}
