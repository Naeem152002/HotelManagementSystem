package com.hotelmanagementsystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotelmanagementsystem.dto.LoginRequest;
import com.hotelmanagementsystem.dto.Response;
import com.hotelmanagementsystem.dto.UserDTO;
import com.hotelmanagementsystem.entity.Booking;
import com.hotelmanagementsystem.entity.User;
import com.hotelmanagementsystem.exception.OurException;
import com.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelmanagementsystem.repo.BookingRepository;
import com.hotelmanagementsystem.repo.UserRepository;
import com.hotelmanagementsystem.security.JwtTokenHelper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
@Autowired
private BookingRepository bookingRepo;
	
	
	@Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private AuthenticationManager authenticationManager;
    
	@Override
	public Response createUser(User user) {
		 Response response=new Response() ;
	            if (user.getRole() == null || user.getRole().isBlank()) {
	                user.setRole("USER");
	            }
	            if (userRepo.existsByEmail(user.getEmail())) {
	                throw new OurException(user.getEmail() + "=Already Exists");
	            }
	            user.setPassword(passwordEncoder.encode(user.getPassword()));
	            User savedUser = userRepo.save(user);
	            UserDTO userDTO = modelMapper.map(savedUser, UserDTO.class);
	            response.setUser(userDTO);
	  
	        return response;
	    }
	
	 @Override
	    public Response login(LoginRequest loginRequest) {
		 Response response=new Response() ;
	            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
	            User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("user Not found"));
	            var token = jwtTokenHelper.generateToken(user);
	            response.setToken(token);
	            response.setUsername(user.getEmail());;
	            response.setRole(user.getRole());
	            response.setExpirationTime("7 Days");
	            response.setMessage("successful");
	        return response;
	    }
	@Override
	public Response updateUser(User user, Long id) {
		 Response response=new Response() ;
		 if (user.getRole() == null || user.getRole().isBlank()) {
             user.setRole("USER");
         }
		User u=userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException ("User","Id", id));
		u.setName(user.getName());
		u.setEmail(user.getEmail());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		u.setMobile(user.getMobile());
		u.setRole(user.getRole());
		User save=userRepo.save(u);
		UserDTO userDTO = modelMapper.map(save, UserDTO.class);
        response.setUser(userDTO);
		return response;
	}

	@Override
	public void deleteUser(Long id) {

		User u=userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException ("User","Id", id));
		userRepo.delete(u);
		
	}

	@Override
	public Response getSingleUserById(Long id) {
		 Response response=new Response() ;

		User u=userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException ("User","Id", id));
		UserDTO userDTO = modelMapper.map(u, UserDTO.class);
        response.setUser(userDTO);
		return response;
	}

	@Override
	public Response getAllUsers() {
		 Response response=new Response() ;

		List<User>usersList=userRepo.findAll();
		List<UserDTO>dtoList=usersList.stream().map(user->modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
		response.setUserList(dtoList);
		return response;
	}

	@Override
	public Response getProfile(String email) {
		 Response response=new Response() ;

	            User user = userRepo.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
	            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
	            response.setUser(userDTO);
	        return response;
	}

	@Override
	public Response updateProfile(User user, String email) {
		 Response response=new Response() ;
		 if (user.getRole() == null || user.getRole().isBlank()) {
             user.setRole("USER");
         }
		User u= userRepo.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
		u.setName(user.getName());
		u.setEmail(user.getEmail());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		u.setMobile(user.getMobile());
		User save=userRepo.save(u);
		UserDTO userDTO = modelMapper.map(save, UserDTO.class);
        response.setUser(userDTO);
		return response;
	}

	@Override
	public void deleteProfile(String email) {
		User u= userRepo.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
		userRepo.delete(u);
		
	}

}
