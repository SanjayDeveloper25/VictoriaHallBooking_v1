package com.gcc.communityHall.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.entity.Users.userType;
import com.gcc.communityHall.repository.DiscountRangeRepository;
import com.gcc.communityHall.service.DiscountRangeService;

@Service
public class DiscountRangeServiceImpl implements DiscountRangeService {

	@Autowired
	private DiscountRangeRepository discountRangeRepository;
	
	 public Optional<DiscountRange> getDiscountByUserType(Users.userType userType) {
	        return discountRangeRepository.findByUsertype(userType.name());
	    }

}
