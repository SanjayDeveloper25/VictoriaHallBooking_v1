package com.gcc.communityHall.service;

import java.util.Optional;

import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.entity.Users.userType;

public interface DiscountRangeService {
	 Optional<DiscountRange> getDiscountByUserType(Users.userType userType);

}
