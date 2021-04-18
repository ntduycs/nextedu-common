package io.github.thanhduybk.common.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    protected final EntityManager entityManager;
    protected final JPAQueryFactory queryFactory;

    public BaseRepositoryImpl(Class<T> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
