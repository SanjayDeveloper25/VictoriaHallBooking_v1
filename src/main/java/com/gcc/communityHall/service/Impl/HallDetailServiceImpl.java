package com.gcc.communityHall.service.Impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.HallImagesRepository;
import com.gcc.communityHall.service.HallDetailService;

@Service
public class HallDetailServiceImpl implements HallDetailService {

//    @Autowired
    private HallDetailsRepository hallDetailsRepository;
    private HallImagesRepository hallImagesRepository;
    private final String hallDirectory;
    private final String hallImageUrl;
    @Autowired
    HallDetailServiceImpl(HallDetailsRepository hallDetailsRepository, HallImagesRepository hallImagesRepository, @Value("${file.hall.upload.directory}") String hallDirectory, @Value("${file.hall.fullpath}") String hallImageUrl){
        this.hallDetailsRepository=hallDetailsRepository;
        this.hallDirectory=hallDirectory;
        this.hallImageUrl=hallImageUrl;
        this.hallImagesRepository = hallImagesRepository;
    }
    @Override
    public HallDetails saveHallDetails(HallDetails hallDetails, List<MultipartFile> hallImages) {
        if(hallImages.size()>0){

        }
        return null;
    }

    @Override
    public HallDetails getHallById(int hallId) {
        Optional<HallDetails> hallDetails = hallDetailsRepository.findById(hallId);
        if(hallDetails.isPresent()){
            return hallDetails.get();
        }

        return null;
    }

    @Override
    public HallDetails deActivateHallById(int hallId) {
        return null;
    }

    @Override
    public HallDetails saveHall(
                                String hallName,float depositAmount, float halfDayAmt,
                                float fullDayAmt, float gst,float hallArea, String ebNumber, float ebCostPerUnit,
                                int parkingCapacity, int seatingCapacity,
                                String doorNo, String street, String area, String zone,
                                String division, String landMark, String city, int pinCode,
                                String latitude, String longitude, String hallInchargeName,
                                long hallInchargeNo,String updatedId, MultipartFile[] hallImages, float halfDepositAmt) {
        HallDetails hall = new HallDetails();
        hall.setHallName(hallName);
        hall.setDoorNo(doorNo);
        hall.setHallArea(hallArea);
        hall.setStreet(street);
        hall.setArea(area);
        hall.setCity(city);
        hall.setPinCode(pinCode);
        hall.setDivision(division);
        hall.setZone(zone);
        hall.setEbNumber(ebNumber); 
        hall.setEbCostPerUnit(ebCostPerUnit);
        hall.setFullDayRent(fullDayAmt);
        hall.setHalfDayRent(halfDayAmt);
        hall.setDepositAmount(depositAmount);
        hall.setGst(gst);
        hall.setHallInchargeName(hallInchargeName);
        hall.setHallInchargeMobno(hallInchargeNo);
        hall.setSeatingCapacity(seatingCapacity);
        hall.setParkingCapacity(String.valueOf(parkingCapacity));
        hall.setLatitude(latitude);
        hall.setLongitude(longitude);
        hall.setLankMark(landMark);
        hall.setUpdatedId(updatedId);
        hall.setCreateddate(LocalDate.now());
        hall.setHalfDepositAmount(halfDepositAmt);
        HallDetails hallDetails = hallDetailsRepository.save(hall);
        if(hallImages!=null && hallImages.length>0){
        	int i=0;
            for (MultipartFile hallImage : hallImages) {
                HallImages hallImages1 = new HallImages();
                String filePath = hallDirectory+"/"+hallDetails.getHallId()+ "_"+i+"_" + hallImage.getOriginalFilename();
                String imageUrl = hallImageUrl+"hallImages/"+hallDetails.getHallId()+ "_"+i+"_" + hallImage.getOriginalFilename();
                try(FileOutputStream fos = new FileOutputStream(filePath)){
                    byte [] bytes = hallImage.getBytes();
                    fos.write(bytes);
                    hallImages1.setHallDetails(hallDetails);
                    hallImages1.setHallImage(imageUrl); // hall/imagename.jpg
                    hallImagesRepository.save(hallImages1); 
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }
        return null;
    }
    public void updatenewHallImages(Integer hallId, MultipartFile[] newHallImages) {
        // Fetch the existing hall details from the database
        HallDetails hallDetails = hallDetailsRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + hallId));

        if (newHallImages != null && newHallImages.length > 0) {
            int i = hallDetails.getHallImagesList().size();  // Start index after existing images
            for (MultipartFile hallImage : newHallImages) {
                HallImages hallImageEntity = new HallImages();
                String filePath = hallDirectory + "/" + hallDetails.getHallId() + "_" + i + "_" + hallImage.getOriginalFilename();
                String imageUrl = hallImageUrl + "hallImages/" + hallDetails.getHallId() + "_" + i + "_" + hallImage.getOriginalFilename();

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    byte[] bytes = hallImage.getBytes();
                    fos.write(bytes);

                    hallImageEntity.setHallDetails(hallDetails);
                    hallImageEntity.setHallImage(imageUrl);
                    hallImagesRepository.save(hallImageEntity); // Save the new image in the database

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save hall image", e);
                }
                i++;
            }
        }
    }
    public String updateHallImage(Integer hallImageId, MultipartFile hallImage) throws IOException {
        Optional<HallImages> optionalHallImage = hallImagesRepository.findById(hallImageId);
        if (optionalHallImage.isPresent()) {
            HallImages hallImages = optionalHallImage.get();

            // Generate the new file path and URL
            String filePath = hallDirectory + "/" + hallImageId + "_" + hallImage.getOriginalFilename();
            String imageUrl = hallImageUrl + "hallImages/" + hallImageId + "_" + hallImage.getOriginalFilename();

            // Save the new image file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] bytes = hallImage.getBytes();
                fos.write(bytes);
                hallImages.setHallImage(imageUrl);
                hallImagesRepository.save(hallImages);
            }

            return imageUrl;
        } else {
            throw new IllegalArgumentException("Hall image not found with ID: " + hallImageId);
        }
    }

	@Override
	public List<HallImages> getHallImages(int hallId) {
		// TODO Auto-generated method stub
		HallDetails hall = new HallDetails();
		hall.setHallId(hallId);

		return hallImagesRepository.findByHallDetails(hall);
	}
	
	@Override
    public String updateHallDetail(HallDetails hallDetails, List<MultipartFile> images) {
        HallDetails existingHall = hallDetailsRepository.findById(hallDetails.getHallId()).orElse(null);

        if (existingHall != null) {
            // Update hall details
            existingHall.setZone(hallDetails.getZone());
            existingHall.setDivision(hallDetails.getDivision());
            existingHall.setHallName(hallDetails.getHallName());
            existingHall.setDoorNo(hallDetails.getDoorNo());
            existingHall.setStreet(hallDetails.getStreet());
            existingHall.setArea(hallDetails.getArea());
            existingHall.setCity(hallDetails.getCity());
            existingHall.setPinCode(hallDetails.getPinCode());
            existingHall.setHallInchargeName(hallDetails.getHallInchargeName());
            existingHall.setHallInchargeMobno(hallDetails.getHallInchargeMobno());
            existingHall.setLandMark(hallDetails.getLandMark());
            existingHall.setLatitude(hallDetails.getLatitude());
            existingHall.setLongitude(hallDetails.getLongitude());
            existingHall.setDepositAmount(hallDetails.getDepositAmount());
            existingHall.setHalfDepositAmount(hallDetails.getHalfDepositAmount());
            existingHall.setFullDayRent(hallDetails.getFullDayRent());
            existingHall.setHalfDayRent(hallDetails.getHalfDayRent());
            existingHall.setParkingCapacity(hallDetails.getParkingCapacity());
            existingHall.setSeatingCapacity(hallDetails.getSeatingCapacity());
            existingHall.setGst(hallDetails.getGst());
            existingHall.setEbCostPerUnit(hallDetails.getEbCostPerUnit());
            existingHall.setActive(hallDetails.isActive()); 
            existingHall.setEbNumber(hallDetails.getEbNumber());

            // Update images
            if (images != null) {
                List<HallImages> hallImagesList = existingHall.getHallImagesList();
                if (hallImagesList == null) {
                    hallImagesList = new ArrayList<>();
                } else {
                    hallImagesList.clear(); // Clear existing images
                }
                int i=0;
                for (MultipartFile image : images) {
                    HallImages hallImage = new HallImages();
                    try {
                        // Save image to storage location
                        String filePath = hallDirectory + "/" + existingHall.getHallId() + "_"+i+"_" + image.getOriginalFilename();
                        String imageUrl = hallImageUrl + "hall/" + existingHall.getHallId() + "_" +i+"_"+ image.getOriginalFilename();
                        try (FileOutputStream fos = new FileOutputStream(filePath)) {
                            byte[] bytes = image.getBytes();
                            fos.write(bytes);
                        }

                        hallImage.setHallImage(imageUrl);
                        hallImage.setHallDetails(existingHall);
                        hallImagesList.add(hallImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                existingHall.setHallImagesList(hallImagesList);
            }

            hallDetailsRepository.save(existingHall);
            return "success";
        }
        return "failed";
    }
	
    @Override
    public List<HallDetails> removeHallById(HashSet<Integer> hallIds) {
        if(hallIds.isEmpty()){
            hallIds.add(0);
        }
        //System.out.println(hallIds);
        return hallDetailsRepository.removeHallById(hallIds);
    }
    public List<HallDetails> getHallNames() {
        List<HallDetails> halls = hallDetailsRepository.findAll();
        return halls.stream()
                    .map(hall -> {
                        HallDetails hallDetails = new HallDetails();
                        hallDetails.setHallId(hall.getHallId());
                        hallDetails.setHallName(hall.getHallName());
                        return hallDetails;
                    })
                    .collect(Collectors.toList());
    }
    @Override
    public void saveHallImage(Long hallId, MultipartFile file) throws IOException {
        try {
            HallDetails hallDetails = hallDetailsRepository.findById(Math.toIntExact(hallId))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid hall ID"));

            HallImages hallImage = new HallImages();
            hallImage.setHallImage(Arrays.toString(file.getBytes())); // Check if this line needs adjustment based on file handling
            hallImage.setHallDetails(hallDetails);
            hallImagesRepository.save(hallImage);
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            throw new RuntimeException("Failed to save hall image: " + e.getMessage());
        }
    }


    @Override
    public List<HallImages> getHallImagesByHallId(Long hallId) {
        return hallImagesRepository.findByHallDetails_HallId(hallId);
    }
	@Override
	public List<HallDetails> getHallNamesbyzone(String zone) {
		List<HallDetails> halls = hallDetailsRepository.findByZone(zone);
        return halls.stream()
                    .map(hall -> {
                        HallDetails hallDetails = new HallDetails();
                        hallDetails.setHallId(hall.getHallId());
                        hallDetails.setHallName(hall.getHallName());
                        return hallDetails;
                    })
                    .collect(Collectors.toList());
	}
	
	@Override
    public Map<String, Object> getHallsDetailsByZone(String zone) {
        // Get the total bookings and amount received by zone
        Map<String, Object> halldetails = hallDetailsRepository.getTotalBookingsAndAmountReceivedByZone(zone);

        // Create a new modifiable map and copy values from the unmodifiable map
        Map<String, Object> modifiableHalldetails = new HashMap<>(halldetails);

        // Get the details of each hall in the zone
        List<Map<String, Object>> zonehalls = hallDetailsRepository.getHallDetailsByZone(zone);

        // Add the list of hall details to the new modifiable map
        modifiableHalldetails.put("zonehalls", zonehalls);

        return modifiableHalldetails;
    }
	
	//officer dashboard
		@Override
		public Map<String, Object> getHallsDetailsForOfficer(String zone) {
			// Get the total bookings and amount received by zone
		    Map<String, Object> halldetails = hallDetailsRepository.getTotalCountsForOfficer(zone);

		    // Create a new modifiable map and copy values from the unmodifiable map
		    Map<String, Object> modifiableHalldetails = new HashMap<>(halldetails);

		    // Get the details of each hall in the zone
		    List<Map<String, Object>> zonehalls = hallDetailsRepository.getHallCountsForOfficer(zone);

		    // Add the list of hall details to the new modifiable map
		    modifiableHalldetails.put("zonehalls", zonehalls);

		    return modifiableHalldetails;
	    }
}
