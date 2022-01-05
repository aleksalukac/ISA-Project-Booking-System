package com.isa.ISAproject.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isa.ISAproject.dto.TimePeriodDTO;
import com.isa.ISAproject.model.Instructor;
import com.isa.ISAproject.model.TimePeriod;
import com.isa.ISAproject.repository.InstructorRepository;
import com.isa.ISAproject.repository.TimePeriodRepository;

@Service
public class TimePeriodService {
	
	@Autowired
	private TimePeriodRepository timePeriodRepository;
	@Autowired
	private InstructorRepository instructorRepository;
	
	public boolean setUnavailabilityInstructor(TimePeriodDTO dto,Long id) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime start = LocalDateTime.parse(dto.getStart(),formatter);
		LocalDateTime end = LocalDateTime.parse(dto.getEnd(),formatter);
		TimePeriod period=new TimePeriod(dto.getId(),start,end,dto.getType());
		Instructor instructor=instructorRepository.getById(id);
		Set<TimePeriod> periods=new HashSet<>();
		if(instructor.getUnavailability()!=null) {
			periods=instructor.getUnavailability();
			for (TimePeriod t : periods) {
				if(t.getStart().isBefore(end) &&  start.isBefore(t.getEnd())) {
					return false;
				}
			}
		}
		this.timePeriodRepository.save(period);
		periods.add(period);
		this.instructorRepository.save(instructor);
		return true;
	}
	
	public boolean removeUnavailabilityInstructor(TimePeriodDTO dto,Long id) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime start = LocalDateTime.parse(dto.getStart(),formatter);
		LocalDateTime end = LocalDateTime.parse(dto.getEnd(),formatter);
		TimePeriod period=new TimePeriod(dto.getId(),start,end,dto.getType());
		Instructor instructor=instructorRepository.getById(id);
		Set<TimePeriod> periods=new HashSet<>();
		if(instructor.getUnavailability()!=null) {
			periods=instructor.getUnavailability();
			for (TimePeriod t : periods) {
				if(t.getStart().toLocalDate().isEqual(start.toLocalDate()) &&  end.toLocalDate().isEqual(t.getEnd().toLocalDate())) {
					//instructor.getUnavailability().remove(period);
					periods.remove(t);
					this.instructorRepository.save(instructor);
					this.timePeriodRepository.delete(period);
					return true;
				}
			}
		}
	
		return false;
	}
	
	
	
	
	public List<TimePeriodDTO> findUnavailabilityByInstructor(Long id){
		Instructor instructor=instructorRepository.getById(id);
		Set<TimePeriod> times=instructor.getUnavailability();
		List<TimePeriodDTO> timesDTO=new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
		for (TimePeriod t : times) {
			
			TimePeriodDTO dto=new TimePeriodDTO(t.getId(),t.getStart().format(formatter),t.getEnd().format(formatter),t.getType());
			timesDTO.add(dto);
			
		}
		return timesDTO;
		
	}

}
