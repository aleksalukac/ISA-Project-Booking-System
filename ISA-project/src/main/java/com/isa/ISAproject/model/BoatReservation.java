package com.isa.ISAproject.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.print.attribute.standard.DateTimeAtCompleted;

@Entity
public class BoatReservation {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private LocalDate date;
	@Column
	private LocalTime time;
	@Column
	private int duration;
	@Column
	private int maxPersons;
	@OneToMany
	private List<AdditionalItem> additionalItems;
	@Column
	private double price;
	@ManyToOne
	private Client client;
	
	@OneToMany
	private List<BoatComplaint> BoatComplaints;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

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

	public List<AdditionalItem> getAdditionalItems() {
		return additionalItems;
	}

	public void setAdditionalItems(List<AdditionalItem> additionalItems) {
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

	public List<BoatComplaint> getBoatComplaints() {
		return BoatComplaints;
	}

	public void setBoatComplaints(List<BoatComplaint> boatComplaints) {
		BoatComplaints = boatComplaints;
	}

	public BoatReservation(Long id, LocalDate date, LocalTime time, int duration, int maxPersons,
			List<AdditionalItem> additionalItems, double price, Client client, List<BoatComplaint> boatComplaints) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.maxPersons = maxPersons;
		this.additionalItems = additionalItems;
		this.price = price;
		this.client = client;
		BoatComplaints = boatComplaints;
	}
	
	public BoatReservation() {}
	
	
}
