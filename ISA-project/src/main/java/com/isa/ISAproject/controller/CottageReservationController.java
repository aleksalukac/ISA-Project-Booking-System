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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isa.ISAproject.dto.AdventureReservationDTO;
import com.isa.ISAproject.dto.CottageReservationDTO;
import com.isa.ISAproject.model.Boat;
import com.isa.ISAproject.model.CottageReservation;
import com.isa.ISAproject.service.CottageReservationService;

@CrossOrigin("*")
@RestController
public class CottageReservationController {
	@Autowired
	private CottageReservationService cottageReservationService;
	
	@RequestMapping(value="api/cottages-reservations/{id}",method = RequestMethod.GET,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<CottageReservationDTO>> findAllResrvationsByClient(@PathVariable Long id){
		List<CottageReservation> res=this.cottageReservationService.oldReservation(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	public List<CottageReservationDTO> convertToDTOList(List<CottageReservation> input){
		List<CottageReservationDTO> res=new ArrayList<>();
		for (CottageReservation cottageReservation : input) {
			res.add(new CottageReservationDTO(cottageReservation));
		}
		return res;
	}
	@RequestMapping(value="api/cottages-reservations/sort-by-price/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<CottageReservationDTO>> sortByPrice(@PathVariable Long id){
		List<CottageReservation> res=this.cottageReservationService.sortByPrice(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/cottages-reservations/sort-by-duration/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<CottageReservationDTO>> sortByDuration(@PathVariable Long id){
		List<CottageReservation> res=this.cottageReservationService.sortByDuration(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/cottages-reservations/sort-by-date/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<CottageReservationDTO>> sortByDate(@PathVariable Long id){
		List<CottageReservation> res=this.cottageReservationService.sortByDate(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	@RequestMapping(value="api/cottages-reservations/active/{id}", method = RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@PreAuthorize("hasRole('CLIENT')")
	public ResponseEntity<List<CottageReservationDTO>> activeReservations(@PathVariable Long id){
		List<CottageReservation> res=this.cottageReservationService.activeReservation(id);
		return new ResponseEntity<>(this.convertToDTOList(res),HttpStatus.OK);
	}
	
	@RequestMapping(value="api/cottageReservation/addReservation",method = RequestMethod.PUT,produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasRole('COTTAGE_OWNER')")
	public ResponseEntity<CottageReservationDTO>  addCottageReservation(@RequestBody CottageReservationDTO dto) {
		CottageReservationDTO fastDTO;
		try {
			fastDTO = this.cottageReservationService.addCottageReservation( dto);
		} catch (PessimisticLockingFailureException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (DateTimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(fastDTO,HttpStatus.OK);
	}
}
