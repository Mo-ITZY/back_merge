package Collabo.MoITZY.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class MemberLoginForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
