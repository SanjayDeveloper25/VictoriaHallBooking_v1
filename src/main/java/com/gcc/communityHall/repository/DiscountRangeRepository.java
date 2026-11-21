package com.gcc.communityHall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.Users.userType;

@Repository
public interface DiscountRangeRepository extends JpaRepository<DiscountRange, Integer>{
	
	@Query("SELECT dr FROM DiscountRange dr WHERE dr.usertype = :userType")
    Optional<DiscountRange> findByUsertype(String userType);

}