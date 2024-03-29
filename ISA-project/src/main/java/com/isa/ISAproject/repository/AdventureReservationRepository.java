package com.isa.ISAproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isa.ISAproject.model.Adventure;
import com.isa.ISAproject.model.AdventureReservation;
import com.isa.ISAproject.model.BoatReservation;
import com.isa.ISAproject.model.Instructor;

public interface AdventureReservationRepository extends JpaRepository<AdventureReservation, Long>{
	List<AdventureReservation> findByOrderByReservationStartDesc();
	List<AdventureReservation> findByOrderByPriceDesc();
	List<AdventureReservation> findByAdventure(Adventure adventure);
}
