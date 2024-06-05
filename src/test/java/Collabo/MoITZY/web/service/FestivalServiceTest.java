package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Festival;
import Collabo.MoITZY.dto.FestivalApiDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Slf4j
@SpringBootTest
class FestivalServiceTest {

    private static final String API_URL = "http://apis.data.go.kr/6260000/FestivalService/getFestivalKr";
    private static final String API_KEY = "MvwRp6lI%2B00uP5QO2M1i1tLd2Rfn5pCVX0byg6cZLvujlrwGQt0EfinPijBsw8XCuiYQRvTTp%2F1%2F13BjQ%2Bka7Q%3D%3D";

    private RestTemplate restTemplate;
    private String url;

    @Autowired
    private FestivalService festivalService;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("resultType", "JSON")
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("serviceKey", API_KEY)
                .toUriString();
        log.info("url: {}", url);
    }

    @Test
    void festivalApiTest() throws JsonProcessingException {
        // API 호출
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);

        String body = entity.getBody();

        log.info("body: {}", body);

        ObjectMapper objectMapper = new ObjectMapper();
        FestivalApiDto festivalApiDto = objectMapper.readValue(body, FestivalApiDto.class);
        log.info("festivalApiDto: {}", festivalApiDto);

        List<Festival> festivals = festivalService.convertToFestivals(festivalApiDto);

        for (Festival festival : festivals) {
            log.info("festival: {}", festival);
        }
    }

    @Test
    void findFestivalsTest() {
        // given


        // when
        // then
    }
}