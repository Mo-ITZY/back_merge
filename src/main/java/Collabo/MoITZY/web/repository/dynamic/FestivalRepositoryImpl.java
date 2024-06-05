package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.dto.FestivalDto;
import Collabo.MoITZY.web.repository.cond.FestivalSearchCond;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static Collabo.MoITZY.domain.QFestival.festival;
import static org.springframework.util.StringUtils.hasText;

public class FestivalRepositoryImpl implements FestivalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FestivalRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FestivalDto> searchFestival(FestivalSearchCond cond, Pageable pageable) {
        List<FestivalDto> content = queryFactory
                .select(Projections.constructor(FestivalDto.class,
                        festival.id,
                        festival.name,
                        festival.img,
                        festival.LAT,
                        festival.LNG,
                        festival.trafficInfo,
                        festival.expense,
                        festival.contact,
                        festival.homepage,
                        festival.description,
                        festival.facilities,
                        festival.place,
                        festival.period))
                .from(festival)
                .where(keywordEq(cond.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Festival> countQuery = queryFactory
                .selectFrom(festival)
                .where(keywordEq(cond.getKeyword()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression keywordEq(String keyword) {
        return hasText(keyword) ? festival.name.contains(keyword) : null;
    }
}
