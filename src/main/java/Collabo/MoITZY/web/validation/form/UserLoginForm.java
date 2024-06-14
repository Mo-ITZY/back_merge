package Collabo.MoITZY.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
