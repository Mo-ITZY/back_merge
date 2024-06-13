package Collabo.MoITZY;

import Collabo.MoITZY.domain.*;
import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.domain.embed.Period;
import Collabo.MoITZY.domain.member.Admin;
import Collabo.MoITZY.domain.member.User;
import Collabo.MoITZY.dto.FestivalApiDto;
import Collabo.MoITZY.web.repository.FestivalRepository;
import Collabo.MoITZY.web.service.FestivalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {

    private final DummyService initService;
    private final FestivalApiInitService festivalApiInitService;

    @PostConstruct
    public void init() {
        initService.dummyInit();
        festivalApiInitService.initApi();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class DummyService {
        private final EntityManager em;

        public void dummyInit() {
            User user_label = new User("로그인 아이디", "비밀번호", "이름", "회원 이메일",
                    new Address("특별시, 광역시, 도", "시, 군, 구", "동, 읍, 면", "상세 주소"), "회원 이미지 주소");
            em.persist(user_label);

            Admin admin = new Admin("admin", "1q2w3e4r");
            em.persist(admin);

            User user = new User("test", "1234", "테스트", "test1234@naver.com",
                    new Address("부산광역시", "부산진구", "가야 1동", "동의대학교 산학협력관 416-1호"), null);
            em.persist(user);

            User user2 = new User("test2", "1234", "테스트2", "test2222@naver.com",
                    new Address("부산광역시", "부산진구", "가야 1동", "동의대학교 산학협력관 416-2호"), null);
            em.persist(user2);

            Festival festival = new Festival("이름", "이미지 주소", 0.0, 0.0, "교통 정보", "비용", "연락처", "홈페이지 주소", "상세 설명", "편의시설",
                    new Address("특별시, 광역시, 도", "시, 군, 구", "동, 읍, 면", "상세 주소"),
                    new Period(LocalDateTime.now(), LocalDateTime.now()));
            em.persist(festival);

            ROI roi = new ROI(user, festival);
            em.persist(roi);

            Review review = new Review(user, festival, "리뷰 이미지 주소", "리뷰 내용");
            em.persist(review);

            Inform inform = new Inform("제목", "내용", LocalDateTime.now());
            Inform inform1 = new Inform("title11111", "content11111", LocalDateTime.now());
            Inform inform2 = new Inform("title22222", "content22222", LocalDateTime.now());
            Inform inform3 = new Inform("title33333", "content33333", LocalDateTime.now());
            Inform inform4 = new Inform("title44444", "content44444", LocalDateTime.now());
            Inform inform5 = new Inform("title55555", "content55555", LocalDateTime.now());
            Inform inform6 = new Inform("title66666", "content66666", LocalDateTime.now());
            em.persist(inform);
            em.persist(inform1);
            em.persist(inform2);
            em.persist(inform3);
            em.persist(inform4);
            em.persist(inform5);
            em.persist(inform6);

            List<Review> reviews1 = user.getReviews();
            for (Review review1 : reviews1) {
                log.info("before review1 = {}", review1);
            }

            List<ROI> roiList1 = user.getRoiList();
            for (ROI roi1 : roiList1) {
                log.info("before roi1 = {}", roi1);
            }

            user.addROI(roi);
            user.addReview(review);
            festival.addROI(roi);
            festival.addReview(review);

            List<Review> reviews2 = user.getReviews();
            for (Review review2 : reviews2) {
                log.info("after review2 = {}", review2);
            }

            List<ROI> roiList2 = user.getRoiList();
            for (ROI roi2 : roiList2) {
                log.info("after roi2 = {}", roi2);
            }
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class FestivalApiInitService {

        private final FestivalRepository festivalRepository;
        private final FestivalService festivalService;

        public void initApi() {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String API_URL = "https://apis.data.go.kr/6260000/FestivalService/getFestivalKr";
            String API_KEY = "MvwRp6lI+00uP5QO2M1i1tLd2Rfn5pCVX0byg6cZLvujlrwGQt0EfinPijBsw8XCuiYQRvTTp/1/13BjQ+ka7Q==";
            String encodedApiKey = URLEncoder.encode(API_KEY, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder
                    .fromUriString(API_URL)
                    .queryParam("serviceKey", encodedApiKey)
                    .queryParam("numOfRows", 10)
                    .queryParam("pageNo", 1)
                    .queryParam("resultType", "JSON")
                    .build(true)
                    .toUri();

            log.info("API_KEY: {}", API_KEY);
            log.info("Decoded API_KEY: {}", encodedApiKey);
            log.info("url: {}", uri);

            String body = restTemplate.getForEntity(uri, String.class).getBody();

            log.info("body: {}", body);

            ObjectMapper objectMapper = new ObjectMapper();

            FestivalApiDto festivalApiDto = null;
            try {
                festivalApiDto = objectMapper.readValue(body, FestivalApiDto.class);
            } catch (JsonProcessingException e) {
                log.error("API 호출 중 오류 발생", e);
                throw new RuntimeException(e);
            }

            if (festivalApiDto != null) {
                List<Festival> festivals = festivalService.convertToFestivals(festivalApiDto);
                festivalRepository.saveAll(festivals);
            }
        }
    }
}