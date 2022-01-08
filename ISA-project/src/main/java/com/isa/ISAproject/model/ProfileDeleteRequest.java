package com.isa.ISAproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ProfileDeleteRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private User user;
	@Column
	private String reason;
	@Enumerated(EnumType.STRING)
    private ProfileDeleteRequestType type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public ProfileDeleteRequestType getType() {
		return type;
	}
	public void setType(ProfileDeleteRequestType type) {
		this.type = type;
	}
	
	public ProfileDeleteRequest(Long id, User user, String reason, ProfileDeleteRequestType type) {
		super();
		this.id = id;
		this.user = user;
		this.reason = reason;
		this.type = type;
	}
	public ProfileDeleteRequest() {
		super();
	}
}
