package dev.rias.app.vo;

import dev.rias.app.security.UserDetailsImpl;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private String token;

	public UserInfoResponse(Long id, String username, String email, List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public UserInfoResponse(UserDetailsImpl userDetails){
		this.id = userDetails.getId();
		this.username = userDetails.getUsername();
		this.email = userDetails.getEmail();
		this.roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
	}

}
