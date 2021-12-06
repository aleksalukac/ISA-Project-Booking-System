package com.isa.ISAproject.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CottageReservation {
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private LocalDateTime date;
	//@Column(nullable=false)
	//private LocalTime time;
	@Column(nullable=false)
	private int duration;
	@Column(nullable=false)
	private int maxPersons;
	@OneToMany
	private Set<AdditionalItem> additionalItems=new HashSet<>();
	@Column(nullable=false)
	private double price;
	@ManyToOne
	private Client client;
	@ManyToOne
	private Cottage cottage;
	
	@OneToMany
	private Set<CottageComplaint> cottageComplaints=new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
/*
	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}*/

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getMaxPersons() {
		return maxPersons;
	}

	public void setMaxPersons(int maxPersons) {
		this.maxPersons = maxPersons;
	}

	public Set<AdditionalItem> getAdditionalItems() {
		return additionalItems;
	}

	public void setAdditionalItems(Set<AdditionalItem> additionalItems) {
		this.additionalItems = additionalItems;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Set<CottageComplaint> getCottageComplaints() {
		return cottageComplaints;
	}

	public void setCottageComplaints(Set<CottageComplaint> cottageComplaints) {
		this.cottageComplaints = cottageComplaints;
	}
	

	public Cottage getCottage() {
		return cottage;
	}

	public void setCottage(Cottage cottage) {
		this.cottage = cottage;
	}

	public CottageReservation(Long id, LocalDateTime date, int duration, int maxPersons,
			Set<AdditionalItem> additionalItems, double price, Client client,
			Set<CottageComplaint> cottageComplaints,Cottage cottage) {
		super();
		this.id = id;
		this.date = date;
		this.duration = duration;
		this.maxPersons = maxPersons;
		this.additionalItems = additionalItems;
		this.price = price;
		this.client = client;
		this.cottageComplaints = cottageComplaints;
		this.cottage=cottage;
	}
	
	
	public CottageReservation() {}
	*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "reservationStart", nullable = false)
	private LocalDate reservationStart;

	@Column(name = "reservationEnd", nullable = false)
	private LocalDate reservationEnd;

	@ManyToOne
	@JoinColumn(name = "cottage_id")
	private Cottage cottage;
	
	@Column(nullable=false)
	private int numberOfPersons;
	@Column(nullable=false)
	private double price;
	@OneToMany
	private Set<AdditionalItem> additionalItems=new HashSet<>();
	@ManyToOne
	private Client client;
	@OneToMany
	private List<CottageComplaint> cottageComplaints;
	
	
	
	public CottageReservation(Long id, LocalDate reservationStart, LocalDate reservationEnd,
			com.isa.ISAproject.model.Cottage cottage, int numberOfPersons, double price,
			Set<AdditionalItem> additionalItems, Client client, List<CottageComplaint> cottageComplaints) {
		super();
		this.id = id;
		this.reservationStart = reservationStart;
		this.reservationEnd = reservationEnd;
		this.cottage = cottage;
		this.numberOfPersons = numberOfPersons;
		this.price = price;
		this.additionalItems = additionalItems;
		this.client = client;
		this.cottageComplaints = cottageComplaints;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public LocalDate getReservationStart() {
		return reservationStart;
	}



	public void setReservationStart(LocalDate reservationStart) {
		this.reservationStart = reservationStart;
	}



	public LocalDate getReservationEnd() {
		return reservationEnd;
	}



	public void setReservationEnd(LocalDate reservationEnd) {
		this.reservationEnd = reservationEnd;
	}



	public Cottage getCottage() {
		return cottage;
	}



	public void setCottage(Cottage cottage) {
		this.cottage = cottage;
	}



	public int getNumberOfPersons() {
		return numberOfPersons;
	}



	public void setNumberOfPersons(int numberOfPersons) {
		this.numberOfPersons = numberOfPersons;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public Set<AdditionalItem> getAdditionalItems() {
		return additionalItems;
	}



	public void setAdditionalItems(Set<AdditionalItem> additionalItems) {
		this.additionalItems = additionalItems;
	}



	public Client getClient() {
		return client;
	}



	public void setClient(Client client) {
		this.client = client;
	}



	public List<CottageComplaint> getCottageComplaints() {
		return cottageComplaints;
	}



	public void setCottageComplaints(List<CottageComplaint> cottageComplaints) {
		this.cottageComplaints = cottageComplaints;
	}



	public CottageReservation() {}
}
