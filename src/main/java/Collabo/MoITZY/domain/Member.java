package Collabo.MoITZY.domain;

import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.web.validation.form.MemberJoinForm;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(of = {"id", "loginId", "name", "password", "email", "img", "address"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //@NotBlank
    private String loginId;

    //@NotBlank
    private String name;

    //@NotBlank
    private String password;

    //@NotBlank
    private String email;

    @Embedded
    private Address address;

    private String img;

    @OneToMany(mappedBy = "member") // Member는 여러 Review를 작성할 수 있다
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member") // Member는 여러 ROI를 설정할 수 있다
    private List<ROI> roiList = new ArrayList<>();

    public Member(String loginId, String name, String password, String email, Address address, String img) {
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.img = img;
        this.address = address;
    }

    public static Member createMember(MemberJoinForm memberJoinForm) {
        return new Member(
                memberJoinForm.getLoginId(),
                memberJoinForm.getName(),
                memberJoinForm.getPassword(),
                memberJoinForm.getEmail(),
                memberJoinForm.getAddress(),
                null);
    }

    public void addROI(ROI roi) {
        roiList.add(roi);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }
}