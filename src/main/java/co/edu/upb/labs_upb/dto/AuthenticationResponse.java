package co.edu.upb.labs_upb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class represents an Authentication Response Data Transfer Object (DTO).
 * DTOs are used to transfer data between different layers of an application.
 * This specific DTO carries information returned after a successful user authentication.
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Long id;
    private String nombre;
    private String email;

    /**
     * The access token used for subsequent API requests after successful authentication.
     * This token should be stored securely on the client-side and included in authorization headers
     * when making authorized requests to the server.
     *
     * @JsonProperty("access_token") (optional) This annotation is likely used with a serialization library
     * to map the property name to "access_token" during JSON serialization.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The refresh token used to obtain a new access token when the current one expires.
     * This token should also be stored securely on the client-side and used to refresh the access token
     * before it expires.
     *
     * @JsonProperty("refresh_token") (optional) This annotation is likely used with a serialization library
     * to map the property name to "refresh_token" during JSON serialization.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
