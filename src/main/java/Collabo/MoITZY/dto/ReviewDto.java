package Collabo.MoITZY.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Data
@NoArgsConstructor(access = PUBLIC)
public class ReviewDto {

    private String userImg;
    private String userName;
    private String reviewImg;
    private String reviewContent;

    public ReviewDto(String userImg, String userName, String reviewImg, String reviewContent) {
        this.userImg = userImg;
        this.userName = userName;
        this.reviewImg = reviewImg;
        this.reviewContent = reviewContent;
    }
}