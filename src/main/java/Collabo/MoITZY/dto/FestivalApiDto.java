package Collabo.MoITZY.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class FestivalApiDto {

    @JsonProperty("getFestivalKr")
    private GetFestivalKr getFestivalKr;

    @Data
    public static class GetFestivalKr {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("item")
        private List<FestivalData> item;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;
    }

    @Data
    public static class Header {
        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;
    }

    @Data
    public static class FestivalData {
        @JsonProperty("UC_SEQ")
        private int ucSeq;

        @JsonProperty("MAIN_TITLE")
        private String mainTitle;

        @JsonProperty("GUGUN_NM")
        private String gugunNm;

        @JsonProperty("LAT")
        private double lat;

        @JsonProperty("LNG")
        private double lng;

        @JsonProperty("PLACE")
        private String place;

        @JsonProperty("TITLE")
        private String title;

        @JsonProperty("SUBTITLE")
        private String subtitle;

        @JsonProperty("MAIN_PLACE")
        private String mainPlace;

        @JsonProperty("ADDR1")
        private String addr1;

        @JsonProperty("ADDR2")
        private String addr2;

        @JsonProperty("CNTCT_TEL")
        private String cntctTel;

        @JsonProperty("HOMEPAGE_URL")
        private String homepageUrl;

        @JsonProperty("TRFC_INFO")
        private String trfcInfo;

        @JsonProperty("USAGE_DAY")
        private String usageDay;

        @JsonProperty("USAGE_DAY_WEEK_AND_TIME")
        private String usageDayWeekAndTime;

        @JsonProperty("USAGE_AMOUNT")
        private String usageAmount;

        @JsonProperty("MAIN_IMG_NORMAL")
        private String mainImgNormal;

        @JsonProperty("MAIN_IMG_THUMB")
        private String mainImgThumb;

        @JsonProperty("ITEMCNTNTS")
        private String itemCntnts;

        @JsonProperty("MIDDLE_SIZE_RM1")
        private String middleSizeRm1;
    }

    public List<FestivalData> getData() {
        if (getFestivalKr.getItem() != null) {
            return getFestivalKr.getItem();
        }
        return null;
    }
}
