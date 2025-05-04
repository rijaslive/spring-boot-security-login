package dev.rias.app.security.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rias.app.vo.ErrorMessage;
import dev.rias.app.vo.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@Slf4j
public class JwtUtils {


  private String jwtSecret;

  private int jwtExpirationMs;

  private ObjectMapper objectMapper;

  public JwtUtils( @Value("${rijas.app.jwtSecret}")String jwtSecret, @Value("${rijas.app.jwtExpirationMs}")int jwtExpirationMs, ObjectMapper objectMapper) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationMs = jwtExpirationMs;
    this.objectMapper = objectMapper;
  }

  public String getJWTTokenFromRequestHeader(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.isNotBlank(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }


  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  public String generateJWTToken(UserInfoResponse userInfoResponse) {
    log.info("Username: {}",userInfoResponse.getUsername());
    return Jwts.builder()
               .setClaims(getClaims(userInfoResponse))
               .setSubject(userInfoResponse.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
               .signWith(key(), SignatureAlgorithm.HS256)
               .compact();
  }

  public void sendErrorMessage(int errorCode, String error, HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setTimestamp(new Date());
    errorMessage.setMessage(ex.getMessage());
    errorMessage.setStatus(errorCode);
    errorMessage.setError(error);
    errorMessage.setPath(("uri=").concat(request.getRequestURI()));
    byte[] body = new ObjectMapper().writeValueAsBytes(errorMessage);
    response.setHeader("Content-Type", "application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream().write(body);
  }



  private Map<String, Object> getClaims(UserInfoResponse loggedInUserDetails) {
    return objectMapper.convertValue(loggedInUserDetails, Map.class);
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

}
