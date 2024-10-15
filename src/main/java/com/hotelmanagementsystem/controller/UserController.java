package com.hotelmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.dto.UserDTO;
import com.hotelmanagementsystem.entity.User;
import com.hotelmanagementsystem.response.ApiResponse;
import com.hotelmanagementsystem.services.UserService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
    private UserService userService;

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateUser(@Valid @RequestBody User user,@PathVariable Long id) {
		return new ResponseEntity<Response>(userService.updateUser(user, id),HttpStatus.OK);
	}
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
		Response rs = userService.getSingleUserById(id);
		UserDTO dto=rs.getUser();
        String name= dto.getEmail();
        String role=dto.getRole();
          userService.deleteUser(id);
         String message = role+"=" +name+ " deleted successfully";
         return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
	}
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getSingleUserById(@PathVariable Long id) {
    	return new ResponseEntity<Response>(userService.getSingleUserById(id),HttpStatus.OK);
	}
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllUsers() {
    	return new ResponseEntity<Response>(userService.getAllUsers(),HttpStatus.OK);
	}
    @GetMapping("/get-profile")
	public ResponseEntity<Response> getProfile() {
      Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
     String email =authentication.getName();
       return new ResponseEntity<Response>(userService.getProfile(email),HttpStatus.OK);
	}
    @PutMapping("/update-profile")
	public ResponseEntity<Response> updateProfile(@Valid @RequestBody User user) {
    	 Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
         String email =authentication.getName();
    	 return new ResponseEntity<Response>(userService.updateProfile(user, email),HttpStatus.OK);
	}@DeleteMapping("/delete-profile")
	public ResponseEntity<ApiResponse> deleteProfile() {
		 Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
         String email =authentication.getName();
         Response res=userService.getProfile(email);
         UserDTO dto=res.getUser();
         userService.deleteProfile(email);
         String message = dto.getRole()+"=" +email+ " deleted successfully";
         return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
	}
	

    }
  
