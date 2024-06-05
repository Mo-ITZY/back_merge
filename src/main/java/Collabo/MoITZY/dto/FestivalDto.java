package Collabo.MoITZY.dto;

import Collabo.MoITZY.domain.embed.Address;
import Collabo.MoITZY.domain.embed.Period;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PUBLIC;

@Data
@NoArgsConstructor(access = PUBLIC)
public class FestivalDto {

    @Id
    private Long id;

    private String name;

    private String img;

    private double lat;

    private double lng;

    private String trafficInfo;

    private String expense;

    private String contact;

    private String homepage;

    private String description;

    private String facilities;

    private Address place;

    private Period period;


    public FestivalDto(Long id, String name, String img, double lat, double lng, String trafficInfo, String expense, String contact, String homepage, String description, String facilities, Address place, Period period) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.lat = lat;
        this.lng = lng;
        this.trafficInfo = trafficInfo;
        this.expense = expense;
        this.contact = contact;
        this.homepage = homepage;
        this.description = description;
        this.facilities = facilities;
        this.place = place;
        this.period = period;
    }
}
