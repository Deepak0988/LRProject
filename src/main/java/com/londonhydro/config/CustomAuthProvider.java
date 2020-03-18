package com.londonhydro.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loginradius.sdk.api.authentication.AuthenticationApi;
import com.loginradius.sdk.models.requestmodels.EmailAuthenticationModel;
import com.loginradius.sdk.models.responsemodels.AccessToken;
import com.loginradius.sdk.models.responsemodels.userprofile.Identity;
import com.loginradius.sdk.util.AsyncHandler;
import com.loginradius.sdk.util.ErrorResponse;
import com.loginradius.sdk.util.LoginRadiusSDK;
import com.londonhydro.service.user.UserService;
import com.londonhydro.util.user.AuthResponse;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
	
	@Autowired
	UserService userService;
	
	@Value("${app.apikey}")
	private String apikey;
	
	@Value("${app.apisecret}")
	private String apisecret;
	
	@Value("${server.port}")
	private String server_port;
	
	Gson gson = new Gson();
	private LoginRadiusSDK.Initialize init = new LoginRadiusSDK.Initialize();

	private String emailverification = "";
	private String resetpassword = "";
	private String resp = "";
	private Boolean isAuthenticated = false;
	
	
	@PostConstruct
	public void init() {
		init.setApiKey(apikey);
		init.setApiSecret(apisecret);

		emailverification = "http://localhost:" + server_port + "/emailverification";
		resetpassword = "http://localhost:" + server_port + "/resetpassword";
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		AuthenticationApi auth = new AuthenticationApi();
		EmailAuthenticationModel payload = new EmailAuthenticationModel();
		payload.setEmail(authentication.getPrincipal().toString());
		payload.setPassword(authentication.getCredentials().toString());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		auth.loginByEmail(payload, null, null, null, null, new AsyncHandler<AccessToken<Identity>>() {

			@Override
			public void onSuccess(AccessToken<Identity> profile) {
				profile.getProfile().getRoles().forEach(r ->{
					GrantedAuthority authority = new SimpleGrantedAuthority(r);
					authorities.add(authority);
				});
				isAuthenticated = true;
				resp = gson.toJson(profile);
				AuthResponse.token2 = resp;
			}

			@Override
			public void onFailure(ErrorResponse error) {
				isAuthenticated = false;
				resp = error.getDescription();
			}
		});
		if (isAuthenticated) { 
			Authentication successAuthObject = new UsernamePasswordAuthenticationToken(authentication.getPrincipal().toString(),null, authorities);
			SecurityContextHolder.getContext().setAuthentication(successAuthObject);
			return successAuthObject;
		}else {
			throw new BadCredentialsException(resp);
		}
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
