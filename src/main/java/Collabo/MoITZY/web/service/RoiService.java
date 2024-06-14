package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.domain.ROI;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.exception.MemberNotFoundException;
import Collabo.MoITZY.web.repository.FestivalRepository;
import Collabo.MoITZY.web.repository.RoiRepository;
import Collabo.MoITZY.web.security.TokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoiService {

    private final RoiRepository roiRepository;
    private final FestivalRepository festivalRepository;
    private final TokenProvider tokenProvider;

    // 찜하기
    @Transactional
    public ResponseDto<Void> addLike(String token, Long festivalId) {
        try {
            User user = tokenProvider.getValidateUser(token);
            Festival festival = getFestivalById(festivalId);

            if (roiRepository.existsByUserIdAndFestivalId(user.getId(), festival.getId())) {
                log.info("user: {}, festival: {}", user.getId(), festival.getId());
                return ResponseDto.error(CONFLICT, "이미 찜한 축제입니다.");
            }

            ROI roi = new ROI(user, festival);
            roiRepository.save(roi);
            user.addROI(roi);

            return ResponseDto.ok(OK, "찜 완료");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, "축제를 찾을 수 없습니다.");
        }
    }

    // 찜 삭제
    @Transactional
    public ResponseDto<Void> deleteLike(String token, Long festivalId) {
        try {
            User user = tokenProvider.getValidateUser(token);
            Festival festival = getFestivalById(festivalId);

            ROI roi = roiRepository.findByUserIdAndFestivalId(user.getId(), festival.getId())
                    .orElseThrow(EntityNotFoundException::new);
            roiRepository.delete(roi);

            return ResponseDto.ok(OK, "찜 삭제 완료");
        } catch (MemberNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseDto.error(NOT_FOUND, "찜한 축제를 찾을 수 없습니다.");
        }
    }

    // 찜 불러오기
    public List<ROI> getLikeList(String token) {
        User user = tokenProvider.getValidateUser(token);
        return user.getRoiList();
    }

    private Festival getFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(EntityNotFoundException::new);
    }
}