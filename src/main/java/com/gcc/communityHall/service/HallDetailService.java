package com.gcc.communityHall.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;

public interface HallDetailService {

    HallDetails saveHallDetails(HallDetails hallDetails, List<MultipartFile> hallImages);
    HallDetails getHallById(int hallId);
    HallDetails deActivateHallById(int hallId);

    HallDetails saveHall(
            String hallName,
             float depositAmount,
             float halfDayAmt, float fullDayAmt,
             float gst ,float hallArea, String ebNumber, float ebCostPerUnit,
             int parkingCapacity, int seatingCapacity,
             String doorNo, String street,
             String area, String zone,
             String division, String landMark,
             String city,
             int pinCode,String latitude,
             String longitude, String hallInchargeName,
             long hallInchargeNo,
             String updatedId,
             MultipartFile hallImages[], float halfDepositAmt );
	List<HallImages> getHallImages(int hallId);
	
	String updateHallDetail(HallDetails hallDetails, List<MultipartFile> images);

	   List<HallDetails> removeHallById(HashSet<Integer> hallIds);
	List<HallDetails> getHallNames();
	void saveHallImage(Long hallId, MultipartFile file) throws IOException;
	List<HallImages> getHallImagesByHallId(Long hallId);
	List<HallDetails> getHallNamesbyzone(String zone);
	
	public Map<String, Object> getHallsDetailsByZone(String zone);
	//officerDashboard
		 public Map<String, Object> getHallsDetailsForOfficer(String zone);
		void updatenewHallImages(Integer hallId, MultipartFile[] newHallImages);
		String updateHallImage(Integer hallImageId, MultipartFile hallImage) throws IOException;

}
