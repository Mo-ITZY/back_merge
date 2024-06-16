package Collabo.MoITZY.domain.member;

import Collabo.MoITZY.domain.ROI;
import Collabo.MoITZY.domain.Review;
import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.web.validation.form.UserJoinForm;
import Collabo.MoITZY.web.validation.form.UserUpdateForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@DiscriminatorValue("USER")
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString(of =
        {"name", "email", "address", "img"}
)
public class User extends Member {

    //@NotBlank
    private String name;

    //@NotBlank
    private String email;

    @Embedded
    private Address address;

    private String img;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true) // User는 여러 Review를 작성할 수 있다
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true) // User는 여러 ROI를 설정할 수 있다
    private List<ROI> roiList = new ArrayList<>();

    public User(String loginId, String password, String name, String email, Address address, String img) {
        super(loginId, password);
        this.name = name;
        this.email = email;
        this.address = address;
        this.img = img;
    }

    public void addROI(ROI roi) {
        roiList.add(roi);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public static User createUser(UserJoinForm userJoinForm) {
        return new User(
                userJoinForm.getLoginId(),
                userJoinForm.getPassword(),
                userJoinForm.getName(),
                userJoinForm.getEmail(),
                userJoinForm.getAddress(),
                null);
    }

    public void updateUser(UserUpdateForm form) {
        this.name = form.getName() == null || form.getName().equals("") ? this.name : form.getName();
        this.email = form.getEmail() == null || form.getEmail().equals("") ? this.email : form.getEmail();
        this.img = form.getImg() == null || form.getImg().equals("") ? this.img : form.getImg();
        this.address = form.getAddress() == null ? this.address : form.getAddress();
        if (form.getPassword() != null && !form.getPassword().equals(""))
            this.changePassword(form.getPassword());
        else {
            this.changePassword(this.getPassword());
        }
    }
}