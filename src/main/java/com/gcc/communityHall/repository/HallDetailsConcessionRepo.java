package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallDetailsConcession;

@Repository
public interface HallDetailsConcessionRepo extends JpaRepository<HallDetailsConcession,Integer>{
	
	@Query(value = "SELECT * FROM hall_details_concession WHERE hall_details_hall_id = :hallId", nativeQuery = true)
	HallDetailsConcession findbyhallId(int hallId);

}
