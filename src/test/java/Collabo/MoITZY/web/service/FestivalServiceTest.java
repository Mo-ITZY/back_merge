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

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
@SpringBootTest
class FestivalServiceTest {

    private static final String API_URL = "https://apis.data.go.kr/6260000/FestivalService/getFestivalKr";

    private static final String API_KEY = "MvwRp6lI+00uP5QO2M1i1tLd2Rfn5pCVX0byg6cZLvujlrwGQt0EfinPijBsw8XCuiYQRvTTp/1/13BjQ+ka7Q==";


    private RestTemplate restTemplate;

    URI uri;

    @Autowired
    private FestivalService festivalService;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String encodedApiKey = URLEncoder.encode(API_KEY, StandardCharsets.UTF_8);

        uri = UriComponentsBuilder
                .fromUriString(API_URL)
                .queryParam("serviceKey", encodedApiKey)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("resultType", "JSON")
                .build(true)
                .toUri();

        log.info("uri: {}", uri);
    }

    @Test
    void festivalApiTest() throws JsonProcessingException {
        // API 호출
        ResponseEntity<String> entity = restTemplate.getForEntity(uri, String.class);

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