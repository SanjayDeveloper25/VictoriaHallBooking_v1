package com.gcc.communityHall.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gcc.communityHall.component.JwtUtil;
import com.gcc.communityHall.dto.ErrorResponseDto;
import com.gcc.communityHall.entity.*;
import com.gcc.communityHall.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.CounterService;
import com.gcc.communityHall.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/victoriahallbooking_uat/counter")
public class CounterController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private CounterDetailsRepository counterRepo;

    @Autowired
    private CounterService counterservice;

    @Autowired
    private BookingService bookser;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private CounterRefundListRepository counterRefundRepository;
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @GetMapping(value = {"", "/", "/counterbooking"})
    public String viewcounter(Model model, HttpSession session) {
        System.out.println("viewcounter");
        String countername = (String) session.getAttribute("usertype");
        System.out.println("serverDate....."+ LocalDate.now().toString());
        model.addAttribute("serverDate", LocalDate.now().toString()); //for server date
        model.addAttribute("countername", countername);
        return "counter/counter-booking";
    }

    @GetMapping("/counterdetailslist")
    public String viewcounterlist(Model model, HttpSession session) {
        List<CounterDetails> counterlist = counterRepo.findAll();
        String countername = (String) session.getAttribute("usertype");
        model.addAttribute("countername", countername);
        model.addAttribute("counterlist", counterlist);
        return "counter/counter-list";
    }

    @PostMapping("/saveDDdetails")
    public String saveDD(
            @RequestParam String bankName,
            @RequestParam int bankAccNo,
            @RequestParam int ddNumber,
            @RequestParam double amountReceived,
            @RequestParam MultipartFile counterddphoto,
            @RequestParam int bookingId) {
        // Call the service method and pass the bookingId along with other parameters
        String confirmBooking = bookser.confirmBooking(bookingId);
        BookingDetails booking = new BookingDetails();
        booking.setBookingStatus(BookingStatus.APPROVED);
        if (confirmBooking.equals("Booking confirmed successfully")) {
            // Perform additional actions if needed
        }
        bookingDetailsRepository.save(booking);
        counterservice.saveCounterDetails(bankName, bankAccNo, ddNumber, amountReceived, counterddphoto, bookingId);
        return "redirect:/counter/counterdetailslist";
    }

    @GetMapping("/bookinglist")
    public String viewbookingpage(Model model, HttpSession session) {
        List<Map<String, Object>> bookinglists = bookser.getOfflineBookings();

        model.addAttribute("bookinglist", bookinglists);
        String countername = (String) session.getAttribute("usertype");
        model.addAttribute("countername", countername);

        return "counter/viewhistory";
    }

    @GetMapping("/refundinitialisation")
    public String viewrefund(Model model, HttpSession session) {
        List<CounterRefundList> counterRefundLists = counterRefundRepository.findAll();
        model.addAttribute("counterRefundLists", counterRefundLists);
        String countername = (String) session.getAttribute("usertype");
        model.addAttribute("countername", countername);
        return "counter/refund-initialisation";
    }

    @GetMapping("/calculateamount")
    public String viewCalculate(Model model) {
        return "counter/calculate-hall-amount";
    }

    @GetMapping("/logout")
    public String viewLogout(Model model, HttpSession session) {
        session.invalidate();
        userSession.setUserId(null);
        userSession.setMobileno(null);
        userSession.setUserType(null);
        userSession.setDispayName(null);
        userSession.setCounter(null);
        userSession.setCounterName(null);
//        return "redirect:/victoriahallbooking_uat/login";
        return "redirect:/victoriahallbooking_uat/user/dashboard";
    }

    //    newly added for counter login
    @GetMapping("/verifyLogin")
    public ResponseEntity<?> verifyLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model, HttpSession session) {
        System.out.println("username: "+username+"\n"+"password: "+password);

        try {
            Login login = loginRepository.findByUsernameAndPass(username, password);
            System.out.println("counter controller\nCounter Id: "+userSession.getCounter()+"\nCounter Name: "+userSession.getCounterName());

            if (login != null) {
                // System.out.println("Login successful for mobile number: " + mobileNo);

                // Generate JWT token if login is successful
                String token = jwtUtil.generateToken(
                        login.getUserType(),
                        login.getHallId(),
                        login.getMobileNumber(),
                        login.getEmployeeId(),
                        login.getZone()
                );
                //System.out.println("Generated JWT Token: " + token);

                // Store JWT token and user information in session
                session.setAttribute("jwtToken", token);
                session.setAttribute("id", login.getId());
                session.setAttribute("hall_id", login.getHallId());
                session.setAttribute("mobileno", login.getMobileNumber());
                session.setAttribute("usertype", login.getUserType());
                session.setAttribute("employeeId", login.getEmployeeId());
                session.setAttribute("zone", login.getZone());
                session.setAttribute("counterName", login.getName());

                model.addAttribute("counter", userSession.getCounter());
                model.addAttribute("counterName", userSession.getCounterName());

                InetAddress inetAddress = InetAddress.getLocalHost();
                String hostname = InetAddress.getLocalHost().getHostName();
                System.out.println("Hostname: " + hostname);

                ApplicationLogs logs = new ApplicationLogs();
                logs.setComments("login as " + login.getName());
                logs.setLoginId(login.getId());
                logs.setIpaddress(inetAddress.getHostAddress());
                logs.setSystemName(hostname);
                applicationLogsRepository.save(logs);

                userSession.setCounter(login.getEmployeeId());
                userSession.setCounterName(login.getName());

                Map<String, Object> response = new HashMap<>();
                response.put("userType", login.getUserType());
                response.put("counterName", login.getName()); // Send Counter Name
                response.put("token", token);

                // Write the token to a file
                try (FileWriter fileWriter = new FileWriter("jwtToken.txt")) {
                    fileWriter.write(token);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store token in file");
                }

                // Return successful response with user type
                return ResponseEntity.ok(response);
            } else {
                // Handle case where login details are incorrect
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Invalid Username or Password");
            }
        } catch (NumberFormatException e) {
            // Handle case where mobile number format is incorrect
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile number must be 10 digits");
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto());
        }

    }
}

