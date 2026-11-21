package com.gcc.communityHall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.ReasonDocumentMaster;

@Repository
public interface ReasonDocMasterRepository extends JpaRepository<ReasonDocumentMaster,Integer> {

	//List<ReasonDocumentMaster> findByReasonId(int reasonId);
	List<ReasonDocumentMaster> findByReasonMasterReasonId(int reasonId);

}
