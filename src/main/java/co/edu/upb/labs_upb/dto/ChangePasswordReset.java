package co.edu.upb.labs_upb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordReset {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

}
