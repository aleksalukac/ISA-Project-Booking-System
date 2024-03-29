package com.isa.ISAproject.service;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.isa.ISAproject.dto.AdditionalItemDTO;
import com.isa.ISAproject.dto.BoatReservationDTO;
import com.isa.ISAproject.dto.CottageReservationDTO;
import com.isa.ISAproject.dto.TimePeriodDTO;
import com.isa.ISAproject.mapper.AdditionalItemMapper;
import com.isa.ISAproject.mapper.BoatReservationMapper;
import com.isa.ISAproject.mapper.CottageReservationMapper;
import com.isa.ISAproject.model.AdditionalItem;
import com.isa.ISAproject.model.Boat;
import com.isa.ISAproject.model.BoatReservation;
import com.isa.ISAproject.model.Client;
import com.isa.ISAproject.model.Cottage;
import com.isa.ISAproject.model.CottageReservation;
import com.isa.ISAproject.model.SystemEarnings;
import com.isa.ISAproject.model.UnavailabilityType;
import com.isa.ISAproject.repository.BoatRepository;
import com.isa.ISAproject.repository.BoatReservationRepository;
import com.isa.ISAproject.repository.ClientRepository;

@Service
public class BoatReservationService {
	@Autowired
	private BoatReservationRepository boatReservationRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private BoatRepository boatRepository;
	@Autowired
	private TimePeriodService timePeriodService;
	@Autowired 
	private EmailService emailService;
	
	public List<BoatReservation> findAll() {
		return this.boatReservationRepository.findAll();
	}
	public List<BoatReservation> findAllResByIdClient(Long id){
		List<BoatReservation> res=new ArrayList<>();
		List<BoatReservation> all=this.findAll();
		for (BoatReservation boatres : all) {
			if(boatres.getClient().getId().equals(id)) {
				res.add(boatres);
			}
		}
		return res;
	}
	public List<BoatReservation> oldReservationForClinet(Long id){
		List<BoatReservation> allRes=this.findAllResByIdClient(id);
		List<BoatReservation> res=new ArrayList<>();
		LocalDateTime lt= LocalDateTime.now();
		for (BoatReservation boatReservation : allRes) {
			if(boatReservation.getDate().isBefore(lt)) {
				res.add(boatReservation);
			}
		}
		return res;
	}
	public List<BoatReservation> sortByDate(Long id) {
		List<BoatReservation> reservations=this.oldReservationForClinet(id);
		List<BoatReservation> res=new ArrayList<>();
		List<BoatReservation> sorted=this.boatReservationRepository.findByOrderByDateDesc();
		for (BoatReservation boatReservation : sorted) {
			for (BoatReservation Reservation2 : reservations) {
				if(boatReservation.getId().equals(Reservation2.getId())) {
					res.add(boatReservation);
				}
			}
		}
		return res;
	}
	public List<BoatReservation> sortByDuration(Long id) {
		List<BoatReservation> reservations=this.oldReservationForClinet(id);
		List<BoatReservation> res=new ArrayList<>();
		List<BoatReservation> sorted=this.boatReservationRepository.findByOrderByDurationDesc();
		for (BoatReservation boatReservation : sorted) {
			for (BoatReservation boatReservation2 : reservations) {
				if(boatReservation.getId().equals(boatReservation2.getId())) {
					res.add(boatReservation);
				}
			}
		}
		return res;
	}
	public List<BoatReservation> sortByPrice(Long id) {
		List<BoatReservation> reservations=this.oldReservationForClinet(id);
		List<BoatReservation> res=new ArrayList<>();
		List<BoatReservation> sorted=this.boatReservationRepository.findByOrderByPriceDesc();
		for (BoatReservation boatRes : sorted) {
			for (BoatReservation boatReservation2 : reservations) {
				if(boatRes.getId().equals(boatReservation2.getId())) {
					res.add(boatRes);
				}
			}
		}
		return res;
	}
	public List<BoatReservation> activeReservation(Long id){
		List<BoatReservation> allRes=this.findAllResByIdClient(id);
		List<BoatReservation> res=new ArrayList<>();
		LocalDateTime lt= LocalDateTime.now();
		for (BoatReservation boatReservation : allRes) {
			if(boatReservation.getDate().isAfter(lt)) {
				res.add(boatReservation);
			}
		}
		return res;
	}
	public BoatReservationDTO addBoatReservation(BoatReservationDTO dto) throws PessimisticLockingFailureException, DateTimeException {
		Boat boat=boatRepository.getById(dto.getBoat().getId());
		Client client=clientRepository.getById(dto.getClient().getId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime start = LocalDateTime.parse(dto.getReservationStart(),formatter);
		LocalDateTime end = LocalDateTime.parse(dto.getReservationEnd(),formatter);
		
		
		TimePeriodDTO time=new TimePeriodDTO();
		time.setStart(dto.getReservationStart());
		time.setEnd(dto.getReservationEnd());
		time.setType(UnavailabilityType.Reservation);
		
		timePeriodService.setUnavailabilityBoat(time, dto.getBoat().getId());
				
		long days = Duration.between(start, end).toDays();
		int price=(int) (boat.getPrice()*days);
		Set<AdditionalItem> items=new HashSet<>();
		for (AdditionalItemDTO adto : dto.getAdditionalItems()) {
			AdditionalItem a=AdditionalItemMapper.convertFromDTO(adto);
			items.add(a);
			price+=a.getPrice();
			
		}
		double earning=SystemEarnings.percentage*price/100;
		BoatReservation res=new BoatReservation(dto.getId(),start,end,boat,dto.getMaxPersons(),price,items,client,null,earning);
		res.setSystemEarning(earning);
		boatReservationRepository.save(res);
		List<BoatReservation> list=client.getBoatReservations();
		list.add(res);
		clientRepository.save(client);
		
		String message="Boat owner has been make reservation for you for boat "+boat.getName()+".Check this in your reservation list";
		
			try {
				this.emailService.sendMessage(client.getEmail(), message);
			} catch (MailException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		return BoatReservationMapper.convertToDTO(res);
	}
}
