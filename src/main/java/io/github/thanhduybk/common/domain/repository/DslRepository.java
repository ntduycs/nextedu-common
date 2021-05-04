package io.github.thanhduybk.common.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public abstract class DslRepository<T> {
    protected final EntityManager entityManager;
    protected final JPAQueryFactory queryFactory;

    public DslRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
