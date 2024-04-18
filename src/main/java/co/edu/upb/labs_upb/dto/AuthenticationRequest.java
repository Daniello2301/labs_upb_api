package co.edu.upb.labs_upb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class represents an Authentication Request Data Transfer Object (DTO).
 * DTOs are used to transfer data between different layers of an application.
 * This specific DTO carries information required for user authentication.
 *
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;

    /**
     * The user's password used for authentication.
     * <p>
     * **Security Note:** Consider storing passwords securely using hashing or encryption mechanisms,
     * and avoid storing them in plain text within the DTO or the system.
     */
    private String password;
}
