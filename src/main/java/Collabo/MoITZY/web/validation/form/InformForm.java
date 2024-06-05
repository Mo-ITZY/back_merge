package Collabo.MoITZY.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InformForm {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}