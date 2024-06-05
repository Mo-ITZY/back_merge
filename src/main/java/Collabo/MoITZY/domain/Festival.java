package Collabo.MoITZY.domain;

import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.domain.embed.Period;
import Collabo.MoITZY.dto.FestivalApiDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Slf4j
@Table(name = "festival")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(of = {
    "id", "name", "img", "LAT", "LNG", "trafficInfo", "expense", "contact", "homepage", "description", "facilities", "place", "period"
})
public class Festival {

    @Id @GeneratedValue
    @Column(name = "festival_id")
    private Long id;

    private String name;

    private String img;

    private double LAT; // 위도

    private double LNG; // 경도

    private String trafficInfo; // 교통정보

    private String expense; // 비용

    private String contact; // 연락처

    private String homepage; // 홈페이지

    @Column(length = 3000)
    private String description; // 상세 정보

    private String facilities; // 편의시설

    @Embedded
    private Address place;

    @Embedded
    private Period period;

    @OneToMany(mappedBy = "festival") // Festival은 Member에 의해 여러 ROI로 설정될 수 있다
    private List<ROI> roiList = new ArrayList<>();

    @OneToMany(mappedBy = "festival") // Festival은 여러개의 Review를 가질 수 있다
    private List<Review> reviews = new ArrayList<>();

    // 편의 메서드
    public void addROI(ROI roi) {
        roiList.add(roi);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    // 생성자
    public Festival(String name, String img, double LAT, double LNG, String trafficInfo,
                    String expense, String contact, String homepage, String description,
                    String facilities, Address place, Period period) {
        this.name = name;
        this.img = img;
        this.LAT = LAT;
        this.LNG = LNG;
        this.trafficInfo = trafficInfo;
        this.expense = expense;
        this.contact = contact;
        this.homepage = homepage;
        this.description = description;
        this.facilities = facilities;
        this.place = place;
        this.period = period;
    }

    // API를 Entity로 변환
    public static Festival ApiToFestival(FestivalApiDto.FestivalData data) {
        Address address = null;
        if (data.getAddr1() != null && !data.getAddr1().isEmpty()) {
            address = parseAddress(data.getAddr1());
        }

        Period period = null;
        if (data.getUsageDayWeekAndTime() != null && !data.getUsageDayWeekAndTime().isEmpty()) {
            period = parsePeriod(data.getUsageDayWeekAndTime());
        }

        log.info("address = {}", data.getAddr1());
        log.info("period = {}", data.getUsageDayWeekAndTime());

        return new Festival(
                data.getTitle(),
                data.getMainImgNormal(),
                data.getLat(),
                data.getLng(),
                data.getTrfcInfo(),
                data.getUsageAmount(),
                data.getCntctTel(),
                data.getHomepageUrl(),
                data.getItemCntnts(),
                data.getMiddleSizeRm1(),
                address,
                period
        );
    }

    private static Address parseAddress(String data) {
        String[] addrParts = data.split(" ");
        String first = addrParts.length > 0 ? addrParts[0] : null;
        String second = addrParts.length > 1 ? addrParts[1] : null;
        String third = addrParts.length > 2 ? addrParts[2] : null;
        String detail = addrParts.length > 3 ? addrParts[3] : null;

        return new Address(first, second, third, detail);
    }

    private static Period parsePeriod(String periodString) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        try {
            if (periodString.startsWith("매년")) {
                // 매년 n월의 경우
                String month = periodString.replaceAll("[^0-9]", "");
                int monthInt = Integer.parseInt(month);

                startDate = LocalDateTime.of(LocalDateTime.now().getYear(), monthInt, 1, 0, 0);
                endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth());
            } else if (periodString.contains("~")) {
                // yyyy.MM.dd 형식의 경우
                String[] dates = periodString.split("~");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d.(E)");

                startDate = LocalDateTime.parse(dates[0].trim(), formatter);
                endDate = LocalDateTime.parse(dates[1].trim(), formatter);
            }
        } catch (DateTimeParseException e) {
            log.error("Date parsing error: {}", e.getMessage());
        }

        return new Period(startDate, endDate);
    }
}