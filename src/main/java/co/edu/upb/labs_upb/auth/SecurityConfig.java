package co.edu.upb.labs_upb.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for Spring Security.
 * It registers the UserDetailsService class.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
//    /**
//     * UserDetailsService instance for user-related operations.
//     * This is typically used for loading user-specific data like usernames, passwords, and granted authorities.
//     */
//    private final UserDetailsService userDetailsService;
    /**
     * Filter for JWT authentication.
     * This filter intercepts every request and checks if it contains a valid JWT token in the Authorization header.
     * If the token is valid, it sets the authentication in the security context.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * AuthenticationProvider instance for user authentication.
     * This is typically used for authenticating a user in the context of a specific authentication request.
     */
    private final AuthenticationProvider authenticationProvider;
    /**
     * List of URLs that do not require authentication.
     */
    private static final String[] WHITE_LIST_URL = {
            "/auth/**",
            "/prestamos/create",
            "/activos/enable/**",
            "/activos/pagination",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    /**
     * Bean for SecurityFilterChain.
     * It configures the security filter chain with the necessary authentication and authorization settings.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()
//                                .requestMatchers("/usuarios/**").hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    /**
     * Bean for CorsConfigurationSource.
     * It configures the CORS settings for the application.
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "PATCH", "OPTIONS"));
        config.setAllowCredentials(true);//permitir credenciales
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Bean for FilterRegistrationBean.
     * It registers the CorsFilter with the highest precedence.
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(configurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);// dar un orden bajo: entre más bajo el orden, mayor la precedencia
        // como es el filtro más alto es sufiente para aplicar a todos los controllers
        return bean;
    }
}
