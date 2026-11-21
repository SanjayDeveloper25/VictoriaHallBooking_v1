package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gcc.communityHall.entity.CounterDetails;

@Repository
public interface CounterDetailsRepository extends JpaRepository<CounterDetails, Integer> 
{
	
}
