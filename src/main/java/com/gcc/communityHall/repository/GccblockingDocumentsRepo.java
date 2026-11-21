package com.gcc.communityHall.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.GccblockingDocuments;

@Repository
public interface GccblockingDocumentsRepo extends JpaRepository<GccblockingDocuments,Integer> {
	
}
