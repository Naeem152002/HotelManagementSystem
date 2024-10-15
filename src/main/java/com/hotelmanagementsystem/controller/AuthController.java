package com.hotelmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagementsystem.dto.LoginRequest;
import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.entity.User;
import com.hotelmanagementsystem.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<Response> createUser(@Valid @RequestBody User user) {
		return new ResponseEntity<Response>(userService.createUser(user),HttpStatus.OK);
	}
	@PostMapping("/login")
	public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest loginRequest) {
		return new ResponseEntity<Response>(userService.login(loginRequest),HttpStatus.OK);
	}

}
