package io.github.thanhduybk.common.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    protected final EntityManager em;
    protected final JPAQueryFactory queryFactory;

    public BaseRepositoryImpl(Class<T> entityClass, EntityManager em) {
        super(entityClass, em);

        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }
}
