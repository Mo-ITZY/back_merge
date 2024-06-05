package Collabo.MoITZY.web.validation.form;

import Collabo.MoITZY.domain.embed.Address;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberJoinForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @Embedded
    private Address address;
}