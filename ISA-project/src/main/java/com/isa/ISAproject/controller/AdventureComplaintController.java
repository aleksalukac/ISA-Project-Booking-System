package com.isa.ISAproject.controller;

import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isa.ISAproject.dto.AdventureComplaintDTO;
import com.isa.ISAproject.dto.AdventureFastReservationDTO;
import com.isa.ISAproject.dto.EditAdventureFastReservationDTO;
import com.isa.ISAproject.service.AdventureComplaintService;

@CrossOrigin("*")
@RestController
public class AdventureComplaintController {
	
	@Autowired
	private AdventureComplaintService adventurComplaintService;
	
	@RequestMapping(value="api/admin/getAdventureComplaints",method = RequestMethod.GET,produces=
			MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN') || hasRole('SYSADMIN')")
	public ResponseEntity<List<AdventureComplaintDTO>> getAdventureComplaints(){
		List<AdventureComplaintDTO> dtos=adventurComplaintService.getAdventureComplaints();
	
		return new ResponseEntity<>(dtos,HttpStatus.OK);
	}
	
	@RequestMapping(value="api/admin/answerComplaint",method = RequestMethod.POST,params = {"message"},produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasRole('ADMIN') || hasRole('SYSADMIN')")
	public ResponseEntity<?>  answerAdventureComplaint(@RequestBody AdventureComplaintDTO dto,@RequestParam String message){
		try {
			adventurComplaintService.answerAdventureComplaints(dto,message);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		return new ResponseEntity<>(HttpStatus.OK);
		
	}

}
