package co.edu.upb.labs_upb.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) operations.
 * It provides methods for extracting user email, generating token, validating token, etc.
 */
@Service
public class JwtService {


    /**
     * Secret key for JWT generation and validation.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKe;
    @Value("${security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    /**
     * Extracts user email from the given JWT token.
     *
     * @param token JWT token
     * @return User email
     */
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extracts a claim from the given JWT token.
     *
     * @param token          JWT token
     * @param claimsResolver Function to resolve the claim
     * @return Claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails User details
     * @return JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    /**
     * Generates a JWT token for the given user details and extra claims.
     *
     * @param extraClaims Extra claims
     * @param userDetails User details
     * @return JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Generates a refresh JWT token for the given user details.
     *
     * @param userDetails User details
     * @return JWT token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtRefreshExpiration);
    }


    /**
     * Builds a JWT token for the given user details, extra claims, and expiration time.
     *
     * @param extraClaims Extra claims
     * @param userDetails User details
     * @param expirationTime Expiration time
     * @return JWT token
     */
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expirationTime) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Checks if the given JWT token is valid for the given user details.
     *
     * @param token       JWT token
     * @param userDetails User details
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserEmail(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts expiration date from the given JWT token.
     *
     * @param token JWT token
     * @return Expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token JWT token
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the signing key for JWT generation and validation.
     *
     * @return Signing key
     */
    private Key getSigningKey() {

//        byte[] keyBytes = Decoders.BASE64.decode(JwtConfig.RSA_PUBLIC);
        byte[] keyBytes = Base64.getDecoder().decode(secretKe.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
