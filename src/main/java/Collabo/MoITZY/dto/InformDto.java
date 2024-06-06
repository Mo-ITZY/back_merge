package Collabo.MoITZY.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime writeDate;
}
