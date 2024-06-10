package Collabo.MoITZY.domain.member;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PROTECTED;

@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Admin extends Member {

    public Admin(String loginId, String password) {
        super(loginId, password);
    }
}