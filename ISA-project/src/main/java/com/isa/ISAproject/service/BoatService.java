package com.isa.ISAproject.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isa.ISAproject.dto.AdditionalItemDTO;
import com.isa.ISAproject.dto.BoatAddDTO;
import com.isa.ISAproject.dto.BoatDTO;
import com.isa.ISAproject.dto.BoatBehavioralRuleDTO;
import com.isa.ISAproject.dto.NavigationEquipmentDTO;
import com.isa.ISAproject.mapper.AdditionalItemMapper;
import com.isa.ISAproject.mapper.BoatBehavioralRuleMapper;
import com.isa.ISAproject.mapper.BoatMapper;
import com.isa.ISAproject.mapper.NavigationEquipmentMapper;
import com.isa.ISAproject.model.AdditionalItem;
import com.isa.ISAproject.model.Address;
import com.isa.ISAproject.model.Boat;
import com.isa.ISAproject.model.BoatOwner;
import com.isa.ISAproject.model.BoatReservation;
import com.isa.ISAproject.model.Client;
import com.isa.ISAproject.model.BoatBehavioralRule;
import com.isa.ISAproject.model.NavigationEquipment;
import com.isa.ISAproject.repository.AdditionalItemRepository;
import com.isa.ISAproject.repository.AddressRepository;
import com.isa.ISAproject.repository.BoatBehavioralRuleRepository;
import com.isa.ISAproject.repository.BoatNavigationEquipmentRepository;
import com.isa.ISAproject.repository.BoatOwnerRepository;
import com.isa.ISAproject.repository.BoatRepository;
import com.isa.ISAproject.repository.BoatReservationRepository;
import com.isa.ISAproject.repository.ClientRepository;

@Component
public class BoatService {
	@Autowired
	private BoatRepository boatRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AdditionalItemRepository additionalItemRepository;
	@Autowired
	private BoatReservationRepository boatReservationRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private BoatOwnerRepository boatOwnerRepository;
	@Autowired
	private BoatBehavioralRuleRepository ruleRepository;
	@Autowired
	private BoatNavigationEquipmentRepository equipmentRepository;
	
	public List<Boat> findAll(){
		return this.boatRepository.findAll();
	}
	public Optional<Boat> getOne(Long id) {
		return this.boatRepository.findById(id);
	}
	public List<Boat> findByMotorNumber(int motorNumber){
		return this.boatRepository.findByMotorNumber(motorNumber);
	}
	public List<Boat> findByMotorPower(double motorPower){
		return this.boatRepository.findByMotorPower(motorPower);
	}
	public List<Boat> findByMotorPowerAndMotorNumber(double motorPower, int motorNumber){
		return this.boatRepository.findByMotorPowerAndMotorNumber(motorPower,motorNumber);
	}
	public List<Boat> sortByName(){
		return this.boatRepository.findByOrderByName();
	}
	public List<Boat> sortByGrade(){
		return this.boatRepository.findByOrderByGradeDesc();
	}
	public List<Boat> sortByCity(){
		List<Boat> allBoats=this.boatRepository.findAll();
		List<Address> allAddressesSortByCities=this.addressRepository.findByOrderByCity();
		List<Boat> res=new ArrayList<>();
		for (Address address : allAddressesSortByCities) {
			for (Boat boat : allBoats) {
				if(boat.getAddress().getId().equals(address.getId())  ) {
					res.add(boat);
				}
			}
		}
		return res;
	}
	public List<AdditionalItem> findAllAdditionalItems(){
		return this.additionalItemRepository.findAll();
	}
	public List<Boat> findByName(String name){
		return this.boatRepository.findByName(name);
	}
	public List<Boat> findByCity(String city){
		List<Boat> all=this.boatRepository.findAll();
		List<Boat> res=new ArrayList<>();
		for (Boat boat : all) {
			if(boat.getAddress().getCity().equals(city)) {
				res.add(boat);
			}
		}
		return res;
	}
	public BoatDTO findById(Long id) {
		Optional<Boat> boat=this.boatRepository.findById(id);
		return BoatMapper.convertToDTO(boat.get());
	}
	public void delete(Long id) {
		Boat b=boatRepository.getById(id);
		//this.cottageRepository.delete(c);
		List<BoatReservation> list=boatReservationRepository.findByBoat(b);
		
			for (BoatReservation boatReservation : list) {
				b.getBoatReservations().remove(boatReservation);
				Client client=boatReservation.getClient();
				
				client.getBoatReservations().remove(boatReservation);
				clientRepository.save(client);
				boatReservation.getBoatRevisions().removeAll(null);
				
			}
			this.boatRepository.save(b);
	}
	public void addBoat(Long boatOwnerId, BoatAddDTO dto) {
		BoatOwner boatOwner=boatOwnerRepository.getById(boatOwnerId);
		Address address=new Address();
		address.setStreet(dto.getAddress().getStreet());
		address.setCity(dto.getAddress().getCity());
		address.setState(dto.getAddress().getState());
		address.setLatitude(dto.getAddress().getLatitude());
		address.setLongitude(dto.getAddress().getLongitude());
		this.addressRepository.save(address);
		Set<Boat> boats=boatOwner.getBoats();
		Set<AdditionalItem> items=new HashSet<>();
		for (AdditionalItemDTO itto : dto.getItems()) {
			AdditionalItem it=AdditionalItemMapper.convertFromDTO(itto);
			items.add(it);
		}
		Set<NavigationEquipment> equipment=new HashSet<>();
		for (NavigationEquipmentDTO itto : dto.getNavigationEquipment()) {
			NavigationEquipment ne=NavigationEquipmentMapper.convertFromDTO(itto);
			equipment.add(ne);
		}
		this.additionalItemRepository.saveAll(items);
		Set<BoatBehavioralRule> rules=new HashSet<>();
		for (BoatBehavioralRuleDTO rdto : dto.getRules()) {
			BoatBehavioralRule b=BoatBehavioralRuleMapper.convertFromDTO(rdto);
			rules.add(b);
		}
		this.ruleRepository.saveAll(rules);
		Boat b=new Boat(dto.getId(),dto.getName(),address,dto.getType(),dto.getLength(),dto.getMotorNumber(),dto.getMotorPower(),dto.getMaxSpeed(),dto.getCapacity(),dto.getDescription(),0,dto.getPrice(),boatOwner,dto.getMainPicture(),null,dto.getMaxPersons(),rules,dto.getCancellationPercentage(),null,items,equipment,null);
		this.boatRepository.save(b);
	}
	
	public List<Boat> findByBoatOwner(BoatOwner boatOwner){
		return this.boatRepository.findByOwner(boatOwner);
	}
	
	public boolean editBoat(Long id,BoatDTO dto) {
		Boat b=boatRepository.getById(id);
		b.setName(dto.getName());
		b.getAddress().setStreet(dto.getAddress().getStreet());
		b.getAddress().setCity(dto.getAddress().getCity());
		b.getAddress().setState(dto.getAddress().getState());
		b.setMaxPersons(dto.getMaxPersons());
		b.setCancellationPercentage(dto.getCancellationPercentage());
		b.setPrice(dto.getPrice());
		b.setDescription(dto.getDescription());
		b.getOwner();
		this.boatRepository.save(b);
		return true;
	}
	
	public List<BoatBehavioralRuleDTO> getBoatRules(Long id){
		Optional<Boat> boat=this.boatRepository.findById(id);
		Set<BoatBehavioralRule> list=boat.get().getRules();
		List<BoatBehavioralRuleDTO> listDTO=new ArrayList<>();
		for(BoatBehavioralRule b:list) { 
			BoatBehavioralRuleDTO bDTO=BoatBehavioralRuleMapper.convertToDTO(b);
			listDTO.add(bDTO);
		}
		return listDTO;
	}
	
	public List<AdditionalItemDTO> getBoatAdditionalItems(Long id){
		Optional<Boat> boat=this.boatRepository.findById(id);
		Set<AdditionalItem> list=boat.get().getAdditionalItems();
		List<AdditionalItemDTO> listDTO=new ArrayList<>();
		for(AdditionalItem a:list) { 
			AdditionalItemDTO aDTO=AdditionalItemMapper.convertToDTO(a);
			listDTO.add(aDTO);
		}
		return listDTO;
	}
	public List<NavigationEquipmentDTO> getNavigationEquipment(Long id){
		Optional<Boat> boat=this.boatRepository.findById(id);
		Set<NavigationEquipment> list=boat.get().getNavigationEquipment();
		List<NavigationEquipmentDTO> listDTO=new ArrayList<>();
		for(NavigationEquipment n:list) { 
			NavigationEquipmentDTO nDTO=NavigationEquipmentMapper.convertToDTO(n);
			listDTO.add(nDTO);
		}
		return listDTO;
	}
	
	public void saveNewEquipment(Long id,NavigationEquipmentDTO eDTO) {
		NavigationEquipment e=NavigationEquipmentMapper.convertFromDTO(eDTO);
		
		Boat b=boatRepository.getById(id);
		this.equipmentRepository.save(e);
		Set<NavigationEquipment> list=b.getNavigationEquipment();
		list.add(e);
		//a.setEquipment(list);
		
		this.boatRepository.save(b);
	}
	
	public boolean editEquipment(Long id,NavigationEquipmentDTO eDTO) {
		NavigationEquipment e=equipmentRepository.getById(eDTO.getId());
		NavigationEquipment edited=new NavigationEquipment();
		edited.setId(e.getId());
		edited.setName(eDTO.getName());
		this.equipmentRepository.save(edited);
		Boat b=boatRepository.getById(id);
		
		Set<NavigationEquipment> list=b.getNavigationEquipment();
		list.remove(e);
		list.add(edited);	
		return true;
	}
	
	public boolean deleteEquipment (Long boatId,Long equipmentId) {
		NavigationEquipment e=this.equipmentRepository.getById(equipmentId);
		Boat b=this.boatRepository.getById(boatId);
		if(e==null || b==null) {
			return false;
		}
		Set<NavigationEquipment> list=b.getNavigationEquipment();
		list.remove(e);
		this.equipmentRepository.delete(e);
		this.boatRepository.save(b);
		return true;		
	}
}
