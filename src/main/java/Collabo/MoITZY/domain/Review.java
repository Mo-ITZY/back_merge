package Collabo.MoITZY.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(of = {"id", "img", "content"})
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // Review는 한명의 Member에 의해 작성된다
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Festival festival; // 해당 리뷰가 어느 Festival에 속해 있는지

    private String img;

    @NotNull
    private String content;

    public Review(Member member, Festival festival, String img, String content) {
        this.member = member;
        this.festival = festival;
        this.img = img;
        this.content = content;
    }
}