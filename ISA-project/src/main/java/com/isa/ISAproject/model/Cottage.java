package com.isa.ISAproject.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Cottage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String name;
	@ManyToOne
	private Address address;
	@Column(nullable=false)
	private String description;
	@Column(nullable=false)
	private double grade;
	@Column
	private String mainPicture;
	@ManyToOne(cascade=CascadeType.PERSIST) //da se ne bi obrisao vlasnik ako se obrise vikendica
	private CottageOwner owner;
	@ElementCollection
	private Set<String> pictures=new HashSet<>();
	
	@ManyToMany
	@JoinTable(joinColumns = @JoinColumn(name = "cottage_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "rule_id", referencedColumnName = "id"))
	private Set<CottageBehavioralRule> rules=new HashSet<>();
	@OneToMany(mappedBy="cottage",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private Set<Room> rooms=new HashSet<>();
	@OneToMany(mappedBy="cottage",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private Set<CottageFastReservation> cottageFastReservations;
	@OneToMany
	private Set<CottageReservation> cottageReservations=new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public Set<String> getPictures() {
		return pictures;
	}

	public void setPictures(Set<String> pictures) {
		this.pictures = pictures;
	}

	public Set<CottageBehavioralRule> getBehavioralRules() {
		return rules;
	}

	public void setBehavioralRules(Set<CottageBehavioralRule> behavioralRules) {
		this.rules = behavioralRules;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public CottageOwner getCottageOwner() {
		return owner;
	}

	public void setCottageOwner(CottageOwner cottageOwner) {
		this.owner = cottageOwner;
	}

	public Set<CottageFastReservation> getCottageFastReservations() {
		return cottageFastReservations;
	}

	public void setCottageFastReservations(Set<CottageFastReservation> cottageFastReservations) {
		this.cottageFastReservations = cottageFastReservations;
	}
	

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		this.mainPicture = mainPicture;
	}
	public CottageOwner getOwner() {
		return owner;
	}

	public void setOwner(CottageOwner owner) {
		this.owner = owner;
	}

	public Set<CottageBehavioralRule> getRules() {
		return rules;
	}

	public void setRules(Set<CottageBehavioralRule> rules) {
		this.rules = rules;
	}

	public Set<CottageReservation> getCottageReservations() {
		return cottageReservations;
	}

	public void setCottageReservations(Set<CottageReservation> cottageReservations) {
		this.cottageReservations = cottageReservations;
	}

	public Cottage(Long id, String name, Address address, String description, double grade, Set<String> pictures,
			Set<CottageBehavioralRule> behavioralRules, Set<Room> rooms, CottageOwner cottageOwner,
			Set<CottageFastReservation> cottageFastReservations, String mainPicture) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.grade = grade;
		this.pictures = pictures;
		this.rules = behavioralRules;
		this.rooms = rooms;
		this.owner = cottageOwner;
		this.cottageFastReservations = cottageFastReservations;
		this.mainPicture=mainPicture;
	}
	
	
	public Cottage(Long id, String name, Address address, String description, double grade, String mainPicture,
			CottageOwner owner, Set<String> pictures, Set<CottageBehavioralRule> rules, Set<Room> rooms,
			Set<CottageFastReservation> cottageFastReservations, Set<CottageReservation> cottageReservations) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.grade = grade;
		this.mainPicture = mainPicture;
		this.owner = owner;
		this.pictures = pictures;
		this.rules = rules;
		this.rooms = rooms;
		this.cottageFastReservations = cottageFastReservations;
		this.cottageReservations = cottageReservations;
	}

	public Cottage () {}
}
