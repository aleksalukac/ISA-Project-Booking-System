package com.isa.ISAproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isa.ISAproject.model.RegistrationRequest;

@Repository
public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {

}
