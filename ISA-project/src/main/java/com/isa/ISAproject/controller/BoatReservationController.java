package com.isa.ISAproject.controller;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isa.ISAproject.dto.BoatReservationDTO;
import com.isa.ISAproject.dto.CottageReservationDTO;
import com.isa.ISAproject.model.BoatReservation;
import com.isa.ISAproject.model.CottageReservation;
import com.isa.ISAproject.service.BoatReservationService;

@CrossOrigin("*")
@RestController
public class BoatReservationController {
	@Autowired
	private BoatReservationService boatReservationService;
	
	@RequestMapping(value="api/boat-reservations/{id}",method = RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<BoatReservationDTO>> findAllResrvationsByClient(@PathVariable Long id){
		List<BoatReservation> res=this.boatReservationService.oldReservationForClinet(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	
	public List<BoatReservationDTO> convertToDTOList(List<BoatReservation> input){
		List<BoatReservationDTO> res=new ArrayList<>();
		for (BoatReservation boatReservation : input) {
			res.add(new BoatReservationDTO(boatReservation));
		}
		return res;
	}
	@RequestMapping(value="api/boat-reservations/sort-by-price/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<BoatReservationDTO>> sortByPrice(@PathVariable Long id){
		List<BoatReservation> res=this.boatReservationService.sortByPrice(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/boat-reservations/sort-by-duration/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<BoatReservationDTO>> sortByDuration(@PathVariable Long id){
		List<BoatReservation> res=this.boatReservationService.sortByDuration(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/boat-reservations/sort-by-date/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<BoatReservationDTO>> sortByDate(@PathVariable Long id){
		List<BoatReservation> res=this.boatReservationService.sortByDate(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/boat-reservations/active/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<BoatReservationDTO>> activeReservations(@PathVariable Long id){
		List<BoatReservation> res=this.boatReservationService.activeReservation(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	
	@RequestMapping(value="api/boatReservation/addReservation",method = RequestMethod.PUT,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasRole('BOAT_OWNER')")
	public ResponseEntity<BoatReservationDTO>  addBoatReservation(@RequestBody BoatReservationDTO dto) {
		BoatReservationDTO fastDTO;
		try {
			fastDTO = this.boatReservationService.addBoatReservation( dto);
		} catch (PessimisticLockingFailureException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (DateTimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(fastDTO,HttpStatus.OK);
	}
}
