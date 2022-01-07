package com.isa.ISAproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
@Entity
public class AdventureComplaint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String description;
	@ManyToOne
	private Client client;
	@ManyToOne
	private Adventure adventure;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	
	public Adventure getAdventure() {
		return adventure;
	}
	
	public AdventureComplaint(Long id, String description, Client client, Adventure adventure) {
		super();
		this.id = id;
		this.description = description;
		this.client = client;
		this.adventure = adventure;
	}
	public void setAdventure(Adventure adventure) {
		this.adventure = adventure;
	}
	public AdventureComplaint () {}
}
