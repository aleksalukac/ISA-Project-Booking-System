package com.isa.ISAproject.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Instructor extends User{
	@OneToMany
	private List<Adventure> adventures;
}
