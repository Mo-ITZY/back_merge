package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.domain.Review;
import Collabo.MoITZY.domain.member.Member;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.dto.ReviewDto;
import Collabo.MoITZY.exception.MemberNotFoundException;
import Collabo.MoITZY.exception.ReviewWriteException;
import Collabo.MoITZY.web.repository.FestivalRepository;
import Collabo.MoITZY.web.repository.ReviewRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import Collabo.MoITZY.web.validation.form.ReviewWriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FestivalRepository festivalRepository;
    private final TokenProvider tokenProvider;

    // 리뷰 작성
    @Transactional
    public ResponseDto<Void> writeReview(String token, Long festivalId, ReviewWriteForm form) {
        try {
            Member member = tokenProvider.getMemberByToken(token);
            String role = member.getRole(member);
            if (!role.equals("USER")) {
                tokenProvider.IsNotUser(role);
            }

            User user = (User) member;
            Long userId = user.getId();
            log.info("userId: {}", userId);

            Optional<Festival> festival = festivalRepository.findById(festivalId);
            validation(festivalId, userId, festival);

            try {
                reviewRepository.save(new Review(user, festival.get(), form.getContent(), form.getImg()));
            } catch (Exception e) {
                return ResponseDto.error(NOT_FOUND, "리뷰 작성 중 오류가 발생했습니다. 다시 시도해 주세요." + e.getMessage());
            }

            return ResponseDto.ok(OK, "리뷰 작성 성공");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        } catch (ReviewWriteException e) {
            return ResponseDto.error(CONFLICT, e.getMessage());
        }
    }

    private void validation(Long festivalId, Long userId, Optional<Festival> festival) {
        boolean present = festival.isPresent();
        if (!present) {
            throw new ReviewWriteException("해당 축제를 찾을 수 없습니다.");
        }

        boolean exists = reviewRepository.existsByFestivalIdAndUserId(festivalId, userId);
        log.info("해당 축제에 이미 리뷰를 썼는지: {}", exists);
        if (exists) {
            throw new ReviewWriteException("이미 리뷰를 작성하셨습니다.");
        }
    }

    // 리뷰 조회
    public ResponseDto<List<ReviewDto>> findReviewOfFestival(Long festivalId) {
        List<ReviewDto> reviewDtos = reviewRepository.findReviewDtos(festivalId);
        return ResponseDto.ok(OK, "리뷰 조회 성공", reviewDtos);
    }
}