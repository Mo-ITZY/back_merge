package Collabo.MoITZY.web.validation.form;

import Collabo.MoITZY.domain.embed.Address;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
public class UserUpdateForm {

    private String name;

    private String password;

    private String email;

    private String img;

    @Embedded
    private Address address;
}