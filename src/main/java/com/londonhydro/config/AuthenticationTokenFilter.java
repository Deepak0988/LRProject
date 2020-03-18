package com.londonhydro.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.loginradius.sdk.api.authentication.AuthenticationApi;
import com.loginradius.sdk.models.responsemodels.AccessTokenBase;
import com.loginradius.sdk.models.responsemodels.userprofile.Identity;
import com.loginradius.sdk.util.AsyncHandler;
import com.loginradius.sdk.util.ErrorResponse;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestToken = request.getParameter("accessToken");
		if(requestToken != null) {
			AuthenticationApi authenticationApi = new AuthenticationApi();		//CHECKING VALIDITY OF ACCESS TOKEN				
			authenticationApi.authValidateAccessToken(requestToken ,  new AsyncHandler<AccessTokenBase> (){

			@Override
			 public void onFailure(ErrorResponse errorResponse) {
				request.setAttribute("TOKEN_EXPIRED", errorResponse.getDescription());
				System.out.println(errorResponse.getDescription());
			 }
			
			 @Override
			 public void onSuccess(AccessTokenBase response) {					//IF VALID THEN AUTHENTICATE	
//				 AuthenticationApi authenticationApi = new AuthenticationApi();
				 
				 authenticationApi.getProfileByAccessToken(requestToken, null ,  new AsyncHandler<Identity> (){

				 @Override
				  public void onFailure(ErrorResponse errorResponse) {
					 System.out.println(errorResponse.getDescription());
				  }
				  @Override
				  public void onSuccess(Identity response) {
					  List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					  response.getRoles().forEach(r ->{
							GrantedAuthority authority = new SimpleGrantedAuthority(r);
							authorities.add(authority);
						});
					  Authentication successAuthObject = new UsernamePasswordAuthenticationToken(response.getEmail().get(0).getValue(), null, authorities);
					  SecurityContextHolder.getContext().setAuthentication(successAuthObject);
				  }
				 });
			 }
			});
		}
			filterChain.doFilter(request, response);
		
	}

}
