package Collabo.MoITZY.domain;

import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.domain.embed.Period;
import Collabo.MoITZY.dto.FestivalApiDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.persistence.CascadeType.*;
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

    @OneToMany(mappedBy = "festival", cascade = ALL, orphanRemoval = true) // Festival은 Member에 의해 여러 ROI로 설정될 수 있다
    private List<ROI> roiList = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = ALL, orphanRemoval = true) // Festival은 여러개의 Review를 가질 수 있다
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
        log.info("before parsing address = {}", data.getAddr1());
        Address address = null;
        if (data.getAddr1() != null && !data.getAddr1().isEmpty()) {
            address = parseAddress(data.getAddr1());
        }
        log.info("after parsing address = {}", address);

        log.info("before parsing period = {}", data.getUsageDayWeekAndTime());
        Period period = null;
        if (data.getUsageDayWeekAndTime() != null && !data.getUsageDayWeekAndTime().isEmpty()) {
            period = parsePeriod(data.getUsageDayWeekAndTime());
        }
        log.info("after parsing period = {}", period);
        log.info("------------------------------------");

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

    public static Period parsePeriod(String periodString) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        try {
            periodString = periodString.replaceAll("\\s+", ""); // 공백 제거
            log.info("periodString 공백 제거 = " + periodString);

            if (periodString.contains("~")) {
                // yyyy.MM.dd 형식의 경우
                String[] dates = periodString.split("~");
                for (String date : dates) {
                    log.info("date = " + date);
                }

                // 년도 보정
                if (!dates[1].startsWith("20")) {
                    dates[1] = dates[0].substring(0, 5) + dates[1];
                }

                // '.' 기준으로 다시 split
                String[] startDateParts = dates[0].split("\\.");
                String[] endDateParts = dates[1].split("\\.");

                LocalDate startLocalDate = LocalDate.of(
                        Integer.parseInt(startDateParts[0]),
                        Integer.parseInt(startDateParts[1]),
                        Integer.parseInt(startDateParts[2].split("\\(")[0])
                );

                LocalDate endLocalDate = LocalDate.of(
                        Integer.parseInt(endDateParts[0]),
                        Integer.parseInt(endDateParts[1]),
                        Integer.parseInt(endDateParts[2].split("\\(")[0])
                );

                startDate = startLocalDate.atStartOfDay();
                endDate = endLocalDate.atStartOfDay();

                log.info("1. startDate = " + startDate + ", endDate = " + endDate);

            } else if (periodString.matches("\\d{4}.\\d{1,2}.\\d{1,2}.\\(.\\)")) {
                // yyyy.MM.dd.(E) 형식의 경우
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d.(E)");
                startDate = LocalDate.parse(periodString.trim(), formatter).atStartOfDay();
                endDate = startDate;

                log.info("2. startDate = " + startDate + ", endDate = " + endDate);

            } else if (periodString.matches("\\d{4}년\\d{1,2}월예정") || periodString.matches("\\d{4}년\\d{1,2}월말예정")) {
                // yyyy년 MM월 예정 또는 yyyy년 MM월 말 예정 형식의 경우
                Pattern pattern = Pattern.compile("(\\d{4})년(\\d{1,2})월");
                Matcher matcher = pattern.matcher(periodString);
                if (matcher.find()) {
                    int year = Integer.parseInt(matcher.group(1));
                    int month = Integer.parseInt(matcher.group(2));

                    startDate = LocalDateTime.of(year, month, 1, 0, 0);
                    endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth());
                }

                log.info("3. startDate = " + startDate + ", endDate = " + endDate);
            }
        } catch (DateTimeParseException e) {
            log.info("Date parsing error: " + e.getMessage());
        }

        return new Period(startDate, endDate);
    }
}