package Collabo.MoITZY.web.repository.dynamic;

import Collabo.MoITZY.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static Collabo.MoITZY.domain.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 리뷰 조회
    @Override
    public List<ReviewDto> findReviewDtos(Long festivalId) {
        List<ReviewDto> result = queryFactory
                .select(Projections.constructor(ReviewDto.class,
                        review.user.img,
                        review.user.name,
                        review.img,
                        review.content))
                .from(review)
                .where(review.festival.id.eq(festivalId))
                .fetch();
        return result;
    }

    @Override
    public void writeReview(Long festivalId, String content, String img) {
        long execute = queryFactory
                .insert(review)
                .set(review.festival.id, festivalId)
                .set(review.content, content)
                .set(review.img, img)
                .execute();
    }
}