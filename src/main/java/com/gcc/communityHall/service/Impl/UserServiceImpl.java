package com.gcc.communityHall.service.Impl;

import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Counsellordetails;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.HallImages;
import com.gcc.communityHall.entity.UserHistory;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.CounsellorDetailsRepository;
import com.gcc.communityHall.repository.UserHistoryRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private CounsellorDetailsRepository counsellorDetailsRepository;
    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${file.upload.directory}")
    private String uploadDirectory;
    @Value("${file.fullpath}")
    private String serverPath;

    @Override
    @Transactional
    public void approveUser(int userId) {
        entityManager.createQuery("UPDATE Users u SET u.userstatus = 'APPROVED' WHERE u.userId = :userId AND u.userstatus = 'PENDING'")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Autowired
    UserHistoryRepository userHistoryRepository;

    public UserServiceImpl(UsersRepository userRepo) {
        this.usersRepository = userRepo;
    }

    public UserServiceImpl() {

    }

    @Override
    public Users user(Users user) {
        if (user.getUsertype() == null || !user.getUsertype().equals(Users.userType.PUBLIC)) {
            user.setUserstatus(Users.userStatus.PENDING);
        }
//        if(user.getUsertype().equals(Users.userType.COUNCILLOR)) {
//        	Counsellordetails counsellordetails=counsellorDetailsRepository.findByMobile(user.getMobileNo());
//        	counsellordetails.getMobile1();counsellordetails.getMobile2()
//        }
        Users saveUser = usersRepository.save(user);

        if (user.getUserPhoto() != null && !user.getUserPhoto().isEmpty()) {
            String fileName = saveUser.getUserId() + "_" + user.getUserPhoto().getOriginalFilename();
            String filePath = uploadDirectory + "/userPhoto/" + fileName;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] bytes = user.getUserPhoto().getBytes();
                fos.write(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            saveUser.setProfilephoto(serverPath + "images/userPhoto/" + fileName);
            saveUser = usersRepository.save(saveUser);
        }

        return saveUser;
    }


    @Override
    public Users getUserById(int userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public List<Users> getUserByStatus(Users.userStatus status) {
        return usersRepository.findByUserstatus(status);
    }

    @Override
    public List<Users> getUserByType(Users.userType userType) {
        return usersRepository.findByUsertype(userType);
    }

    @Override
    public Users updateUserStatus(int userId, Users.userStatus status, String comments) {
        Optional<Users> user = usersRepository.findById(userId);
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        if (user.isPresent()) {
            Users usr = user.get();

            UserHistory userHistory = new UserHistory();
            userHistory.setUsers(usr);
            userHistory.setUserstatus(status);
            userHistory.setComments(comments);
            userHistory.setCdate(new Date());
            userHistory.setUpdatedBy(adminname);
            userHistoryRepository.save(userHistory);

            usr.setUserstatus(status);
            usr.setUpdatedBy(adminname);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            usr.setUpdatedDate(formattedDate);
            Users users = usersRepository.save(usr);
            ApplicationLogs logs = new ApplicationLogs();
            logs.setComments("Action by " + adminname);
            logs.setLoginId(loginid);
            logs.setLogType("User Status:-" + status.toString());
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getLocalHost();
                String hostname = InetAddress.getLocalHost().getHostName();
                logs.setIpaddress(inetAddress.getHostAddress());
                logs.setSystemName(hostname);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            applicationLogsRepository.save(logs);
            return users;


        }
        return null;
    }

    @Override
    public Users findByMobileno(Long mobileNo) {
        // TODO Auto-generated method stub
        return usersRepository.findByMobileNo(mobileNo);
    }

    //yoga
    @Override
    public List<Users> getAllUsers(String fromDate, String toDate) {
        return usersRepository.getAllUsers(fromDate, toDate);

    }


    @Override
    public List<Users> getAllUsersWithFilters(String fromDate, String toDate, String userStatus, String userType) {
        return usersRepository.getAllUsersWithFilters(fromDate, toDate, userStatus, userType);
    }

    @Override
    public List<Users> getAllUsersWithStatusFilters(String fromDate, String toDate, String userStatus) {

        return usersRepository.getAllUsersWithStatusFilters(fromDate, toDate, userStatus);
    }

    @Override
    public List<Users> getAllUsersWithTypeFilters(String fromDate, String toDate, String userType) {

        return usersRepository.getAllUsersWithTypeFilters(fromDate, toDate, userType);
    }


    public void updateProfile(Users userProfileRequest) {
        // Retrieve the user entity from the repository based on the ID
        Optional<Users> optionalUser = usersRepository.findById(userProfileRequest.getUserId());
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            // Update the user entity with the new profile details
            user.setName(userProfileRequest.getName());
            user.setEmail(userProfileRequest.getEmail());
            user.setEmail(userProfileRequest.getArea());
            user.setEmail(userProfileRequest.getStreet());
            user.setEmail(userProfileRequest.getDoorNo());
            usersRepository.save(user);
        } else {
            System.out.println("User with ID " + userProfileRequest.getUserId() + " not found");
        }
    }

    @Override
    public String updateUserDetails(Users userDetails) {
        Users existingUser = usersRepository.findById(userDetails.getUserId()).orElse(null);

        if (existingUser != null) {
            // Update user details
            existingUser.setName(userDetails.getName());
            existingUser.setDoorNo(userDetails.getDoorNo());
            existingUser.setArea(userDetails.getArea());
            existingUser.setLocation(userDetails.getLocation());
            existingUser.setUsertype(userDetails.getUsertype());
            existingUser.setPinCode(userDetails.getPinCode());
            existingUser.setEmail(userDetails.getEmail());

            if (userDetails.getProfilephoto() != null && !userDetails.getProfilephoto().isEmpty()) {
                existingUser.setProfilephoto(userDetails.getProfilephoto()); // Update profile photo path
            }

            if (userDetails.getUsertype() == Users.userType.PUBLIC) {
                existingUser.setUserstatus(Users.userStatus.APPROVED);
            } else {
                existingUser.setUserstatus(Users.userStatus.PENDING);
            }

            usersRepository.save(existingUser);
            return "success";
        }
        return "failed";
    }


    @Override
    public void saveUsers(Users user) {
        // TODO Auto-generated method stub

    }

    public Counsellordetails findByMobile(long mobileno) {
        return counsellorDetailsRepository.findByMobile(mobileno);
    }

    @Override
    public Users getUserDetailsByMobileNo(long mobileNo) {
        return usersRepository.findByMobileNo(mobileNo);
    }

    public void toggleUserStatus(Integer userId, boolean active) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setActive(active);
            usersRepository.save(user);
        }
    }
}