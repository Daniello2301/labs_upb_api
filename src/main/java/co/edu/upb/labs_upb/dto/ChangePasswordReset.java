package co.edu.upb.labs_upb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a Change Password Reset Data Transfer Object (DTO).
 * DTOs are used to transfer data between different layers of an application.
 * This specific DTO carries information required to reset a user's password.
 *
 *
 * @see lombok.Getter (optional, if Lombok is used for getters)
 * @see lombok.Setter (optional, if Lombok is used for setters)
 */
@Getter
@Setter
@Builder
public class ChangePasswordReset {

    /**
     * The user's current password for verification before changing the password.
     */
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

}
