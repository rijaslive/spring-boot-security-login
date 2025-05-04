package dev.rias.app.service;


import dev.rias.app.security.UserDetailsImpl;
import dev.rias.app.vo.LoginRequest;
import dev.rias.app.vo.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {


    private AuthenticationManager authenticationManager;

    public LoginService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new UserInfoResponse(userDetails);
    }

}
