package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gcc.communityHall.entity.FloorDetails;

@Repository
public interface FloorDetailsRepository extends JpaRepository<FloorDetails,Integer> {

}
