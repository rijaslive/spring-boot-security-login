package dev.rias.app.service;

import dev.rias.app.entities.ERole;
import dev.rias.app.entities.Role;
import dev.rias.app.entities.User;
import dev.rias.app.vo.MessageResponse;
import dev.rias.app.repository.RoleRepository;
import dev.rias.app.repository.UserRepository;
import dev.rias.app.vo.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author RiJAS
 * @Date 04-05-2025
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserRegistrationService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public MessageResponse registerUser(SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        } else {

            Map<ERole, Role> rolesByName = roleRepository.findAll().stream().collect(Collectors.toMap(role -> role.getName(), role -> role));

            roles = strRoles.stream()
                    .map(role -> switch (role.toLowerCase()) {
                        case "admin" -> rolesByName.get(ERole.ROLE_ADMIN);
                        case "mod" -> rolesByName.get(ERole.ROLE_MODERATOR);
                        default -> rolesByName.get(ERole.ROLE_USER);
                    })
                    .collect(Collectors.toSet());

            if(CollectionUtils.isEmpty(roles)){
                new RuntimeException("Error: Role is not found.");
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponse("Successfully registered user!");
    }


}
