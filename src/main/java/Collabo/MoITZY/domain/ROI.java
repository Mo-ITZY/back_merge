package Collabo.MoITZY.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

/**
 * 관심 영역 (Region of Interest)
 * Member는 여러개의 Festival을 관심 영역으로 설정할 수 있다
 * Festival은 여러개의 Member에게 관심 영역으로 설정될 수 있다
 */

@Entity
@Table(name = "roi")
@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id"})
public class ROI {

    @Id @GeneratedValue
    @Column(name = "roi_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") // Member는 여러개의 ROI를 설정할 수 있다
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "festival_id") // Festival은 여러개의 ROI를 설정받을 수 있다
    private Festival festival;

    public ROI(Member member, Festival festival) {
        this.member = member;
        this.festival = festival;
    }
}