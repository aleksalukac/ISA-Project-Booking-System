package com.isa.ISAproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.isa.ISAproject.dto.ProfileDeleteRequestDTO;
import com.isa.ISAproject.dto.RegistrationRequestDTO;
import com.isa.ISAproject.dto.UserDTO;
import com.isa.ISAproject.mapper.UserMapper;
import com.isa.ISAproject.model.ProfileDeleteRequest;
import com.isa.ISAproject.model.ProfileDeleteRequestType;
import com.isa.ISAproject.model.RegistrationRequest;
import com.isa.ISAproject.model.User;
import com.isa.ISAproject.repository.InstructorRepository;
import com.isa.ISAproject.repository.ProfileDeleteRequestRepository;
import com.isa.ISAproject.repository.UserRepository;

@Service
public class ProfileDeleteRequestService {
	@Autowired
	private ProfileDeleteRequestRepository profileDeleteRequestRepository;
	@Autowired
	private InstructorRepository instructorRepository;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	public ProfileDeleteRequestDTO sendProfileDeleteRequest(ProfileDeleteRequestDTO dto) {
		
		User existUser = this.userRepository.getById(dto.getUserDTO().getId());
		UserDTO userDTO=UserMapper.convertToDTO(existUser);
		ProfileDeleteRequest request=new ProfileDeleteRequest(dto.getId(),existUser,dto.getReason(),ProfileDeleteRequestType.Unverified);
		this.profileDeleteRequestRepository.save(request);
		ProfileDeleteRequestDTO req=new ProfileDeleteRequestDTO(request.getId(),userDTO,request.getReason(),ProfileDeleteRequestType.Unverified);
		return req;
	}
	
	public List<ProfileDeleteRequestDTO> findAll() {
		List<ProfileDeleteRequest> requests=profileDeleteRequestRepository.findAll();
		List<ProfileDeleteRequestDTO> requestsDTO=new ArrayList<>();
		for (ProfileDeleteRequest r : requests) {
			UserDTO userDTO=UserMapper.convertToDTO(r.getUser());
			ProfileDeleteRequestDTO dto=new ProfileDeleteRequestDTO(r.getId(),userDTO,r.getReason(),r.getType());
			requestsDTO.add(dto);
		
	 }
	 return requestsDTO;
	}
	public List<ProfileDeleteRequestDTO> findAllUnverified() {
		List<ProfileDeleteRequest> requests=profileDeleteRequestRepository.findAll();
		List<ProfileDeleteRequestDTO> requestsDTO=new ArrayList<>();
		for (ProfileDeleteRequest r : requests) {
			if(r.getType()==ProfileDeleteRequestType.Unverified) {
				UserDTO userDTO=UserMapper.convertToDTO(r.getUser());
				ProfileDeleteRequestDTO dto=new ProfileDeleteRequestDTO(r.getId(),userDTO,r.getReason(),r.getType());
				requestsDTO.add(dto);
			}
			
		
	 }
	 return requestsDTO;
	}
	
	public void acceptDeleteRequest(ProfileDeleteRequestDTO requestDTO) throws MailException, InterruptedException {
		User user=userRepository.getById(requestDTO.getUserDTO().getId());
		user.setEnabled(false);
		userRepository.save(user);
		//userRepository.delete(user);
		ProfileDeleteRequest request=this.profileDeleteRequestRepository.getById(requestDTO.getId());
		request.setType(ProfileDeleteRequestType.Accepted);
		this.profileDeleteRequestRepository.save(request);
		
		emailService.sendMessage(requestDTO.getUserDTO().getEmail(),"Your profile delete request has been accepted. Your account has been deleted and you can not log in!");
		
		
	}
	
public void rejectDeleteRequest(ProfileDeleteRequestDTO requestDTO,String message) throws MailException, InterruptedException {
		
		
		ProfileDeleteRequest request=this.profileDeleteRequestRepository.getById(requestDTO.getId());
		request.setType(ProfileDeleteRequestType.Rejected);
		this.profileDeleteRequestRepository.save(request);
		emailService.sendMessage(requestDTO.getUserDTO().getEmail(),message);
		
		
	}
}
