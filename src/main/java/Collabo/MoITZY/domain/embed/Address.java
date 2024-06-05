package Collabo.MoITZY.domain.embed;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Address {

    private String first; // 특별시, 광역시, 8도
    private String second; // 시, 군, 구

    private String third; // 동, 읍, 면

    private String detail; // 상세주소

    public Address(String first, String second, String third, String detail) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.detail = detail;
    }

}