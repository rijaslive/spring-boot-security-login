package dev.rias.app.controllers;

import dev.rias.app.service.LoginService;
import dev.rias.app.service.UserRegistrationService;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.rias.app.vo.LoginRequest;
import dev.rias.app.vo.SignupRequest;
import dev.rias.app.vo.UserInfoResponse;
import dev.rias.app.vo.MessageResponse;
import dev.rias.app.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  public static final String X_AUTH_TOKEN = "X-Auth-Token";

  private  JwtUtils jwtUtils;

  private LoginService loginService;

  private UserRegistrationService userRegistrationService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    UserInfoResponse userInfoResponse = loginService.authenticateUser(loginRequest);
    String jwtToken = jwtUtils.generateJWTToken(userInfoResponse);
    userInfoResponse.setToken(jwtToken);
    return ResponseEntity.ok()
        .body(userInfoResponse);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    MessageResponse messageResponse = userRegistrationService.registerUser(signUpRequest);
    return ResponseEntity.ok(messageResponse);
  }

}
