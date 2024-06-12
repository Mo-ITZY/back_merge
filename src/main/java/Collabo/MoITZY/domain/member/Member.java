package Collabo.MoITZY.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Table(name = "member")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(of = {"id", "loginId", "password"})
public abstract class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //@NotBlank
    private String loginId;

    //@NotBlank
    private String password;

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public String getRole(Member member) {
        if (member instanceof Admin) {
            return "ADMIN";
        } else if (member instanceof User) {
            return "USER";
        } else {
            return  "UNKNOWN";
        }
    }
}