package Collabo.MoITZY.domain.embed;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.*;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(of = {"first", "second", "third", "detail"})
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

    public Address getUpdateAddress(Address address) {
        this.first = address.getFirst() == null || address.getFirst().equals("") ? this.getFirst() : address.getFirst();
        this.second = address.getSecond() == null || address.getSecond().equals("") ? this.getSecond() : address.getSecond();
        this.third = address.getThird() == null || address.getThird().equals("") ? this.getThird() : address.getThird();
        this.detail = address.getDetail() == null || address.getDetail().equals("") ? this.getDetail() : address.getDetail();
        return this;
    }
}