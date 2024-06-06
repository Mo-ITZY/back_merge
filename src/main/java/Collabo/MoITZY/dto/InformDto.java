package Collabo.MoITZY.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class InformDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime writeDate;

    public InformDto(Long id, String title, String content, LocalDateTime writeDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
    }
}
