package com.isa.ISAproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isa.ISAproject.model.AdventureFishingEquipment;


@Repository
public interface AdventureFishingEquipmentRepository extends JpaRepository<AdventureFishingEquipment, Long> {

}
