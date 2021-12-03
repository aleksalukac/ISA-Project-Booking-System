package com.isa.ISAproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isa.ISAproject.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
	
}
