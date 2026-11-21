package com.gcc.communityHall.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Counsellordetails;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;
import com.gcc.communityHall.entity.Users;

public interface UserService {

    public Users user(Users user);
    public Users getUserById(int userId);
    public List<Users> getUserByStatus(Users.userStatus status);
    public List<Users> getUserByType(Users.userType userType);
    public Users updateUserStatus(int userId,Users.userStatus status,String comments);

    public void approveUser(int userId);
	Users findByMobileno(Long mobileNo);
	
	//yoga
List<Users> getAllUsers(String fromDate, String toDate);
	
	List<Users> getAllUsersWithFilters(String fromDate, String toDate,String userStatus,String userType);

	List<Users> getAllUsersWithStatusFilters(String fromDate, String toDate, String userStatus);
	
	List<Users> getAllUsersWithTypeFilters(String fromDate, String toDate, String userType);
	void saveUsers(Users user);
	
	String updateUserDetails(Users userDetails);
	public Counsellordetails findByMobile(long mobileno);
	public Users getUserDetailsByMobileNo(long mobileNo);
	public void toggleUserStatus(Integer userId, boolean active);

}