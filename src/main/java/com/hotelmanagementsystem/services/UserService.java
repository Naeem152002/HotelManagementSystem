package com.hotelmanagementsystem.services;

import com.hotelmanagementsystem.dto.LoginRequest;
import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.entity.User;

public interface UserService {
	public Response createUser(User user);
	public Response login(LoginRequest loginRequest) ;
	public Response updateUser(User user,Long id);
	public void deleteUser(Long id);
	public Response getSingleUserById(Long id);
	public Response getAllUsers();
	public Response getProfile(String email);
	public Response updateProfile(User user,String email);
	public void deleteProfile(String email);
}
