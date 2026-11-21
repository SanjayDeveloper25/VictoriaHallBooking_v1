package com.gcc.communityHall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.ReasonMaster;

@Repository
public interface ReasonMasterRepository extends JpaRepository<ReasonMaster, Integer> {

	ReasonMaster findByReason(String reason);

	@Query(value = "select * from reason_master where is_active =1 and is_delete=0 ", nativeQuery = true)
	List<ReasonMaster> getAllReasonsWithStatus();
}
