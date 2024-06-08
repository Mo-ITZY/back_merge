package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.dto.InformDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static Collabo.MoITZY.domain.QInform.*;

public class InformRepositoryImpl implements InformRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public InformRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<InformDto> getInformList(Pageable pageable) {
        List<InformDto> content = queryFactory
                .select(Projections.constructor(InformDto.class,
                        inform.id,
                        inform.title,
                        inform.content,
                        inform.writeDate))
                .from(inform)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Inform> countQuery = queryFactory.selectFrom(inform);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
}