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
import com.isa.ISAproject.dto.CottageReservationDTO;
import com.isa.ISAproject.dto.TimePeriodDTO;
import com.isa.ISAproject.mapper.AdditionalItemMapper;
import com.isa.ISAproject.mapper.CottageReservationMapper;
import com.isa.ISAproject.model.AdditionalItem;
import com.isa.ISAproject.model.Client;
import com.isa.ISAproject.model.Cottage;
import com.isa.ISAproject.model.CottageReservation;
import com.isa.ISAproject.model.SystemEarnings;
import com.isa.ISAproject.model.UnavailabilityType;
import com.isa.ISAproject.repository.ClientRepository;
import com.isa.ISAproject.repository.CottageRepository;
import com.isa.ISAproject.repository.CottageReservationRepository;
import com.isa.ISAproject.repository.SystemEarningsRepository;

@Service
public class CottageReservationService {
	@Autowired
	private CottageReservationRepository cottageReservationRepository;
	@Autowired
	private CottageRepository cottageRepository;
	@Autowired
	private TimePeriodService timePeriodService;
	@Autowired 
	private EmailService emailService;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private SystemEarningsRepository systemEarningsRepository;

	
	public List<CottageReservation> findAll() {
		return this.cottageReservationRepository.findAll();
	}
	public List<CottageReservation> sortByPrice(Long id) {
		List<CottageReservation> reservations=this.oldReservation(id);
		List<CottageReservation> res=new ArrayList<>();
		List<CottageReservation> sorted=this.cottageReservationRepository.findByOrderByPriceDesc();
		for (CottageReservation cottageReservation : sorted) {
			for (CottageReservation cottageReservation2 : reservations) {
				if(cottageReservation.getId().equals(cottageReservation2.getId())) {
					res.add(cottageReservation);
				}
			}
		}
		return res;
	}
	public List<CottageReservation> sortByDate(Long id) {
		List<CottageReservation> reservations=this.oldReservation(id);
		List<CottageReservation> res=new ArrayList<>();
		List<CottageReservation> sorted=this.cottageReservationRepository.findByOrderByDateDesc();
		for (CottageReservation cottageReservation : sorted) {
			for (CottageReservation cottageReservation2 : reservations) {
				if(cottageReservation.getId().equals(cottageReservation2.getId())) {
					res.add(cottageReservation);
				}
			}
		}
		return res;
	}
	public List<CottageReservation> sortByDuration(Long id) {
		List<CottageReservation> reservations=this.oldReservation(id);
		List<CottageReservation> res=new ArrayList<>();
		List<CottageReservation> sorted=this.cottageReservationRepository.findByOrderByDurationDesc();
		for (CottageReservation cottageReservation : sorted) {
			for (CottageReservation cottageReservation2 : reservations) {
				if(cottageReservation.getId().equals(cottageReservation2.getId())) {
					res.add(cottageReservation);
				}
			}
		}
		return res;
	}
	public List<CottageReservation> findAllResByIdClient(Long id){
		List<CottageReservation> res=new ArrayList<>();
		List<CottageReservation> all=this.findAll();
		for (CottageReservation cottageReservation : all) {
			if(cottageReservation.getClient().getId().equals(id)) {
				res.add(cottageReservation);
			}
		}
		return res;
	}
	public List<CottageReservation> activeReservation(Long id){
		List<CottageReservation> allRes=this.findAllResByIdClient(id);
		List<CottageReservation> res=new ArrayList<>();
		LocalDateTime lt= LocalDateTime.now();
		for (CottageReservation r : allRes) {
			if(r.getDate().isAfter(lt)) {
				res.add(r);
			}
		}
		return res;
	}
	public List<CottageReservation> oldReservation(Long id){
		List<CottageReservation> allRes=this.findAllResByIdClient(id);
		List<CottageReservation> res=new ArrayList<>();
		LocalDateTime lt= LocalDateTime.now();
		for (CottageReservation r : allRes) {
			if(r.getDate().isBefore(lt)) {
				res.add(r);
			}
		}
		return res;
	}
	
	public CottageReservationDTO addCottageReservation(CottageReservationDTO dto) throws PessimisticLockingFailureException, DateTimeException {
		Cottage cottage=cottageRepository.getById(dto.getCottage().getId());
		Client client=clientRepository.getById(dto.getClient().getId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime start = LocalDateTime.parse(dto.getReservationStart(),formatter);
		LocalDateTime end = LocalDateTime.parse(dto.getReservationEnd(),formatter);
		
		
		TimePeriodDTO time=new TimePeriodDTO();
		time.setStart(dto.getReservationStart());
		time.setEnd(dto.getReservationEnd());
		time.setType(UnavailabilityType.Reservation);
		
		timePeriodService.setUnavailabilityCottage(time, dto.getCottage().getId());
				
		
		
		long days = Duration.between(start, end).toDays();
		int price=(int) (cottage.getPrice()*days);
		Set<AdditionalItem> items=new HashSet<>();
		for (AdditionalItemDTO adto : dto.getAdditionalItems()) {
			AdditionalItem a=AdditionalItemMapper.convertFromDTO(adto);
			items.add(a);
			price+=a.getPrice();
			
		}
		double earning=SystemEarnings.percentage*price/100;
		CottageReservation res=new CottageReservation(dto.getId(),start,end,cottage,dto.getMaxPersons(),price,items,client,null,earning);
		res.setSystemEarning(earning);
		cottageReservationRepository.save(res);
		List<CottageReservation> list=client.getCottageReservations();
		list.add(res);
		clientRepository.save(client);
		
		String message="Cottage owner has been make reservation for you for cottage "+cottage.getName()+".Check this in your reservation list";
		
			try {
				this.emailService.sendMessage(client.getEmail(), message);
			} catch (MailException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		return CottageReservationMapper.convertToDTO(res);
	}
}
