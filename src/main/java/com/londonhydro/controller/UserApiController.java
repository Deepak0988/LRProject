package com.londonhydro.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class UserApiController {
	@PostMapping("/api/profile")
	public String userProfile() {
		return "Success";
	}
}
