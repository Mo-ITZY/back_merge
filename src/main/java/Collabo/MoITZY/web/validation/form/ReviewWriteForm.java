package Collabo.MoITZY.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewWriteForm {

    private String img;

    @NotBlank
    private String content;
}