package com.isa.ISAproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isa.ISAproject.model.Boat;
import com.isa.ISAproject.model.Cottage;
@Repository
public interface CottageRepository extends JpaRepository<Cottage, Long>{
	List<Cottage> findByName(String name);
	List<Cottage> findByAddress(String address);
	List<Cottage> findByOrderByName();
	List<Cottage> findByOrderByGradeDesc();
}
