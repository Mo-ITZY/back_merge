package Collabo.MoITZY.dto;

import lombok.Data;

@Data
public class MyPageDto {

    private String name;

    private String img;

    private int reviewCount;

    public MyPageDto(String name, String img, int reviewCount) {
        this.name = name;
        this.img = img;
        this.reviewCount = reviewCount;
    }
}