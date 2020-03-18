package com.londonhydro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.londonhydro.service.user.UserService;
import com.londonhydro.util.user.AuthResponse;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@GetMapping(value= {"/","/login"})
	public String showWelcomePage() {
		return "user/login";
	}

	@PostMapping(value="/login")
	@ResponseBody
	public String doLogin(HttpServletRequest request){
		String username = request.getParameter("email");
		String password = request.getParameter("password");
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		return AuthResponse.token2;
	}

	@GetMapping(value="/profile")
	@PostMapping(value="/profile")
	public String showProfilePage(){
		return "user/profile";
	}
	
	@GetMapping(value="/error")
	public String showErrorPage(){
		return "error";
	}
	
	@PutMapping(value="/password/change")
	public String changePassword(){
		return null;
	}
}
