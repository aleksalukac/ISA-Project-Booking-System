package com.isa.ISAproject.repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.isa.ISAproject.model.Instructor;
import com.isa.ISAproject.model.ProfileDeleteRequest;

@Repository
public interface ProfileDeleteRequestRepository extends JpaRepository<ProfileDeleteRequest, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from ProfileDeleteRequest p where p.id = :id")
	//Postgres po defaultu poziva for update bez no wait, tako da treba dodati vrednost 0 za timeout
	//kako bismo dobili PessimisticLockingFailureException ako pri pozivu ove metode torka nije dostupna

	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
	public ProfileDeleteRequest findOneById(@Param("id")Long id);
}
