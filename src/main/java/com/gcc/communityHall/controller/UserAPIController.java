package com.gcc.communityHall.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcc.communityHall.dto.BookingDetailsDTO;
import com.gcc.communityHall.dto.EmployeeDetails;
import com.gcc.communityHall.dto.EmployeeDetailsResponse;
import com.gcc.communityHall.dto.ErrorResponseDto;
import com.gcc.communityHall.dto.FamilyPensionerDetails;
import com.gcc.communityHall.dto.FamilyPensionerDetailsResponse;
import com.gcc.communityHall.dto.PensionerDetails;
import com.gcc.communityHall.dto.PensionerDetailsResponse;
import com.gcc.communityHall.entity.BookingCalendar;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Counsellordetails;
import com.gcc.communityHall.entity.CounterDetails;
import com.gcc.communityHall.entity.CounterRefundList;
import com.gcc.communityHall.entity.FloorDetails;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.RefundTransaction;
import com.gcc.communityHall.entity.SmsSender;
import com.gcc.communityHall.entity.UserHistory;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.entity.BookingDetails.BookingStatus;
import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.Users.userStatus;
import com.gcc.communityHall.entity.Users.userType;
import com.gcc.communityHall.exception.SlotOccupiedException;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.paymentService.entity.RefundResponse;
import com.gcc.communityHall.paymentService.entity.TransactionResponse;
import com.gcc.communityHall.paymentService.refund.DTO.TransactionRefundResponse;
import com.gcc.communityHall.paymentService.repository.TransRefundResRepository;
import com.gcc.communityHall.paymentService.service.PaymentGatewayServiceImpl;
import com.gcc.communityHall.repository.BookingCalendarRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.BookingHistoryRepository;
import com.gcc.communityHall.repository.CounsellorDetailsRepository;
import com.gcc.communityHall.repository.FloorDetailsRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.RefundTransactionRepository;
import com.gcc.communityHall.repository.SmsSenderRepository;
import com.gcc.communityHall.repository.UsersRepository;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.CalendarService;
import com.gcc.communityHall.service.CounterService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.UserService;
import com.gcc.communityHall.service.Impl.WhatsappMsgServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import static org.hibernate.query.sqm.tree.SqmNode.log;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Tag(
        name = "CRUD REST APIs for User in CommunityHall",
        description = "CRUD REST APIs in CommunityHall to CREATE, UPDATE, FETCH AND DELETE Booking details"
)
@RestController
@RequestMapping("/victoriahallbooking_uat/user/api")
@AllArgsConstructor
@Validated
public class UserAPIController {
    @Autowired
    private HallDetailsRepository hallDetailsRepository;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private UserSession userSession;
    @Autowired
    private SmsSenderRepository smsSenderRepository;
    @Autowired
    private BookingCalendarRepository bookingCalendarRepository;
    @Autowired
    private UserService userservice;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private CounterService counterservice;

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private UserService userServices;

    @Autowired
    private HallDetailService hallDetailService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private UsersRepository usersRepository;

    private final String uploadDirectory;

    @Autowired
    private WhatsappMsgServiceImpl whatsappMsgServiceImpl;

    @Autowired
    private PaymentGatewayServiceImpl PaymentGatewayServiceImpl;

    @Autowired
    private TransRefundResRepository transRefundResRepository;

    @Autowired
    public UserAPIController(@Value("${file.upload.directory}") String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    @Operation(
            summary = "Retrieving the HallDetails by HallId REST API",
            description = "REST API to Retrieving the HallDetails by HallId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status UPDATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    //retrieving Hall Details by HallId
    @GetMapping("/gethalldetails/{hallId}")
    public ResponseEntity<HallDetails> getHallDetails(@PathVariable int hallId) {
        Optional<HallDetails> hallDetails = hallDetailsRepository.findById(hallId);
        return hallDetails.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @Operation(
            summary = "Retrieving the Hallnames only REST API",
            description = "REST API to Retrieving the Hallnames only"
    )


    @GetMapping("/gethallnames")
    public ResponseEntity<List<HallDetails>> getHallNames() {
        List<HallDetails> hallDetailsList = hallDetailsRepository.findActiveHalls();
        List<HallDetails> hallDataList = new ArrayList<>();
        for (HallDetails hallDetails : hallDetailsList) {
            HallDetails hallData = new HallDetails();
            hallData.setHallId(hallDetails.getHallId());
            hallData.setHallName(hallDetails.getHallName());
            hallDataList.add(hallData);
        }
        return ResponseEntity.ok(hallDataList); // Return ResponseEntity with list of HallData objects containing only id and hallname
    }


    @Operation(
            summary = "Send OTP REST API",
            description = "REST API For Sending OTP to user"
    )
    //SendOTP
    @GetMapping(value = "/sendOTP")
    public String sendOtp(HttpServletRequest request, @RequestParam Long mobileNo, @RequestParam String purpose) {

        Long MobileNo = mobileNo;
        //System.out.println(purpose);
        //System.out.print("\n MobileNo---:"+mobileNo);
        Random rand = new Random();
//       int otp = rand.nextInt(90000) + 10000;
         int otp = 11111;
        System.out.print("\n otp---:" + otp);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy%20HH:mm");
        LocalDateTime now = LocalDateTime.now();
        //System.out.println(dtf.format(now));
       String key = "pfTEYN6H";
       String urlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" + mobileNo
                + "&from=GCCCRP&body=OTP for your Victoria Public Hall booking " + otp
                + "\r\n- GCC&entityid=1401572690000011081&templateid=1407176491887361640";
        System.out.println(urlString);
        System.out.println("\n urlString---:  " + urlString);
        String bodyMessage = "" + otp;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String Response = restTemplate.getForObject(urlString, String.class);
            bodyMessage=Response;
            SmsSender sms = new SmsSender();
            sms.setPhone(MobileNo);
            sms.setCdate(new Date());
            sms.setBodyMsg(bodyMessage);
            sms.setPurpose(purpose);
            smsSenderRepository.save(sms);

            String whatsappMsg="https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="+MobileNo+"&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Welcome+to+GCC%21+%0A%0AYour+OTP+for+GCC+Community+Centre+is+"+otp+"+.";
            String sendMsg = whatsappMsgServiceImpl.sendMsg(whatsappMsg);
            System.out.println("sendwhatsuprepsosne===="+sendMsg);
        } catch (Exception e) {
           // System.out.print("ERROR in Sending---:  "+e);
            bodyMessage = "Error";
        }
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        return bodyMessage;
    }

    @Operation(
            summary = "Vetify OTP REST API",
            description = "REST API For verifying OTP"
    )
    //VerifyOTP
    @GetMapping(value = "/verifyOTP")
    public String verifyOtp(HttpServletRequest request, @RequestParam String otp, @RequestParam long mobileNo) {
        HttpSession session = request.getSession();
        try {
            Integer session_otp = (Integer) session.getAttribute("otp");
            System.out.println("session_otp---:" + session_otp);
            Integer entered_otp = Integer.parseInt(otp);

            if (session_otp != null && session_otp.equals(entered_otp)) {
                System.out.println("Entered OTP Correct user API");
                Users user = userservice.findByMobileno(mobileNo);
                if (user != null) {
                    userSession.setMobileno(user.getMobileNo());
                    //userSession.setUserType(user.getUserType());
                    userSession.setDispayName(user.getName());
                    userSession.setUserId(user.getUserId());
                    String userType = user.getUsertype().toString();
                    userSession.setUserType(userType);
                }
                session.setAttribute("verifiedotp", "success");
                return "Success";

            } else {
                System.out.println("Incorrect OTP. Please enter correct OTPs.");
                session.setAttribute("user", "");
                session.setAttribute("mobileNo", "");
                session.setAttribute("id", "");
                session.setAttribute("verifiedotp", "error");
                return "Error";
            }
        } catch (NumberFormatException e) {
            //System.out.println("Invalid OTP format: " + otp);
            return "Error";
        } catch (Exception e) {
            // System.out.println("Error verifying OTP: " + e.getMessage());
            return "Error";
        }
    }

    @GetMapping(value = "/verifyofficialOTP")
    public String verifyOtp(HttpServletRequest request, @RequestParam String otp) {
        HttpSession session = request.getSession();
        try {
            Integer session_otp = (Integer) session.getAttribute("otp");
            // System.out.println("session_otp---:" + session_otp);
            Integer entered_otp = Integer.parseInt(otp);

            if (session_otp != null && session_otp.equals(entered_otp)) {
                //System.out.println("Entered OTP Correct");
                session.setAttribute("verifiedotp", "success");
                return "Success";
            } else {
                // System.out.println("Incorrect OTP. Please enter correct OTPs.");
                session.setAttribute("verifiedotp", "error");
                return "Error";
            }
        } catch (NumberFormatException e) {
            // System.out.println("Invalid OTP format: " + otp);
            return "Error";
        } catch (Exception e) {
            //System.out.println("Error verifying OTP: " + e.getMessage());
            return "Error";
        }
    }

    @Operation(
            summary = "User Mobileno Validation REST API",
            description = "REST API For Validating User with Mobileno"
    )
    //Validating User with the mobileno 
    @GetMapping(value = "/checkMobileExits")
    public String checkMobileExits(@RequestParam long mobileno) {
        try {
            Users form = userservice.findByMobileno(mobileno);
            if (form != null) {
                return "success";
            } else {
                return "error";
            }
        } catch (NumberFormatException e) {
            // Handle the case where the phone number cannot be parsed as a Long
            return "error";
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return "error";

        }
    }

    @Operation(
            summary = "Cousellor Mobileno Validation REST API",
            description = "REST API For Validating the Counsellor Mobileno"
    )
    //Counsellor Mobile no Validation
    @GetMapping(value = "/checkCounsellorMobileExits")
    public ResponseEntity<String> checkCounsellorMobileExits(@RequestParam long mobileno) {
        // Find the counsellor by the provided mobile number
        Counsellordetails counsellor = userservice.findByMobile(mobileno); // This checks both mobileNo1 and mobileNo2 in the counsellor table
        if (counsellor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Counsellor Mobile Number Not Found");
        }

        // Check if mobileNo1 or mobileNo2 of the counsellor is already used in the Users table
        boolean isMobile1Used = usersRepository.existsByMobileNo(counsellor.getMobile1());
        boolean isMobile2Used = usersRepository.existsByMobileNo(counsellor.getMobile2());

        if (isMobile1Used || isMobile2Used) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("One of the counsellor's mobile numbers is already used.");
        }

        // If no conflict, return success with the counsellor's name
        return ResponseEntity.ok(counsellor.getName());
    }


    @GetMapping("/checkEmployee")
    public Map<String, Boolean> checkEmployee(@RequestParam String id) {
        boolean isRegistered = usersRepository.existsByGovernmentId(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isRegistered", isRegistered);
        return response;
    }


    @Operation(
            summary = "Retriving the zone REST API",
            description = "REST API to Retriev the zone for displaying in Frontend"
    )
    //retrieving the zone
    @GetMapping(value = "/getZone")
    public List<Map<String, Object>> getZone() {
        return hallDetailsRepository.getZoneAndDivision();
    }

    @Operation(
            summary = "Zones by Hall REST API",
            description = "REST API to Retrieve the zone by Hall for displaying in GccBlocking page"
    )
    //retrieving the zones by hall
    @GetMapping(value = "/hallsbyzone")
    public List<Map<String, Object>> getHallsByZone(@RequestParam(required = false) String zones) {
        if (zones == null || zones.isEmpty()) {
            return hallDetailsRepository.getAllHalls();
        } else {
            List<String> zoneList = Arrays.asList(zones.split(","));
            return hallDetailsRepository.getHallsByZones(zoneList);
        }
    }

    @Operation(
            summary = "Past Data from the booking Calendar Hall REST API",
            description = "REST API to Retrieve the Pat Data from booking calendar"
    )
    //Getting the Past Data from the Calendar
    @GetMapping("/getPastData")
    public List<BookingCalendar> getPastData(@RequestParam("hall_id") int hall_id) {
        List<BookingCalendar> data = new ArrayList();
        data = bookingService.getPastData(hall_id);
        if (data != null) {
            return data;
        }
        return data;
    }

    @Operation(
            summary = "Pensioner Details ERP REST API",
            description = "REST API to retrieving the Pensioner Details From ERP"
    )
    //retrieving the Pensioner Details From ERP
    @GetMapping("/PensionerDetail")
    public PensionerDetailsResponse getPensionerDetails(@RequestParam("pensionerId") String pensionerId) {
        //System.out.println(pensionerId);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getPensionerDetailsList&pensionerId=" + pensionerId;


        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PensionerDetailsResponse pensionerResponse = objectMapper.readValue(jsonString, PensionerDetailsResponse.class);

            // Now you can access the pensionerResponse object as usual
            // e.g., pensionerResponse.getPensioners() to get the list of pensioners
            return pensionerResponse;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        //return null;

    }

    @Operation(
            summary = "Employee Details ERP REST API",
            description = "REST API to retrieving the Employee Details From ERP"
    )
    @GetMapping("/EmployeeDetails")
    public EmployeeDetailsResponse getEmployeeDetails(@RequestParam("employeeId") Long employeeId) {
        //System.out.println("Employee ID:"+employeeId);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getEmployeeDetail&EmployeeID=" + employeeId;


        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EmployeeDetailsResponse employeeResponse = objectMapper.readValue(jsonString, EmployeeDetailsResponse.class);

            // Now you can access the pensionerResponse object as usual
            // e.g., pensionerResponse.getPensioners() to get the list of pensioners
            return employeeResponse;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        //return null;

    }

    @Operation(
            summary = "FamilyPensioner Details ERP REST API",
            description = "REST API to retrieving the FamilyPensioner Details From ERP"
    )
    //retrieving the FamilyPensionerDetails Details From ERP
    @GetMapping("/FamilyPensionerDetail")
    public FamilyPensionerDetailsResponse getFamilyPensionerDetails(@RequestParam("id") Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getFamPensionerDetailsList&familPensionId=" + id;


        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            FamilyPensionerDetailsResponse familyPensionerDetailsResponse = objectMapper.readValue(jsonString, FamilyPensionerDetailsResponse.class);

            // Now you can access the pensionerResponse object as usual
            // e.g., pensionerResponse.getPensioners() to get the list of pensioners
            return familyPensionerDetailsResponse;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Operation(
            summary = "Hall Details coordinates REST API",
            description = "REST API to retrieving the Hall Details coordinates for Map"
    )
    //retrieving the All Hall Details coordinates
    @GetMapping("/getallhallcoordinates")
    public List<Map<String, Object>> getAllHallCoordinates() {
        List<HallDetails> allHallDetails = hallDetailsRepository.findActiveHalls();
        return allHallDetails.stream().map(this::extractRequiredDetails).collect(Collectors.toList());
    }

    private Map<String, Object> extractRequiredDetails(HallDetails hallDetails) {
        Map<String, Object> details = new HashMap<>();

        details.put("latitude", hallDetails.getLatitude());
        details.put("longitude", hallDetails.getLongitude());
        details.put("hallId", String.valueOf(hallDetails.getHallId()));
        details.put("hallName", hallDetails.getHallName());
        details.put("seatingCapacity", String.valueOf(hallDetails.getSeatingCapacity()));
        details.put("parkingCapacity", hallDetails.getParkingCapacity());
        details.put("hallImage", hallDetails.getHallImagesList());
        details.put("fullDayRent", hallDetails.getFullDayRent());
        details.put("halfDayRent", hallDetails.getHalfDayRent());
        details.put("depositAmount", hallDetails.getDepositAmount());
        details.put("area", hallDetails.getArea());
        details.put("street", hallDetails.getStreet());
        details.put("landMark", hallDetails.getLandMark());
        details.put("doorNo", hallDetails.getDoorNo());
        details.put("hallArea", hallDetails.getHallArea());
        return details;
    }

    @Operation(
            summary = "Booking Limitation Details ERP REST API",
            description = "REST API to check the Booking Limitation & UserStatus pending"
    )
    //Booking Limitation & UserStatus pending
    @GetMapping("/bookingLimit")
    public String CheckbookingLimitation(Model model) {
        int userId = userSession.getUserId();
        Users userById = userService.getUserById(userId);
        if (userById != null && userById.getUserstatus() == Users.userStatus.PENDING) {
            return "pending";
        }
        if (userById != null && userById.getUserstatus() == Users.userStatus.REJECTED) {
            return "rejected";
        }
        int checkUserBookingLimitation = bookingService.checkUserBookinglimitation(userId);
//        System.out.println("checkUserBookingLimitation="+checkUserBookingLimitation);
        if (checkUserBookingLimitation < 2) {
//        	System.out.println("checkUserBookingLimitationss="+checkUserBookingLimitation);
            return "success";
        }
        return "failure";
    }

    @Operation(
            summary = "Counter Booikng Limit REST API",
            description = "REST API to check the Counter Booking Limitation"
    )
    @GetMapping("/counterbookinglimit")
    public String checkCounterBookingLimitation(@RequestParam long mobileNo) {
        Users user = userService.getUserDetailsByMobileNo(mobileNo);

        if (user == null) {
            // System.err.println("User not found for mobile number: " + mobileNo);
            return "checkmobilenoexists"; // You can return a different status or handle it accordingly
        }
        int userId = user.getUserId();
        int checkUserBookingLimitation = bookingService.checkUserBookinglimitation(userId);

        if (checkUserBookingLimitation >= 2) {
            // System.err.println("bookinglimitreached");
            return "bookinglimitreached";
        }
        if (user != null && !user.isActive()) {
            return "deactive";
        }

        return "checkmobilenoexists";
    }

    @Operation(
            summary = "User Status Pending REST API",
            description = "REST API to check User Status is Pending and not allow user to login"
    )
    //UserStatus pending
    @GetMapping("/checkuserstatus")
    public String checkUserStatus(@RequestParam long mobileNo) {
        Users user = userService.getUserDetailsByMobileNo(mobileNo); // Assuming you have a method to retrieve a user by mobile number
        if (user != null && user.getUserstatus() == Users.userStatus.PENDING) {
            return "pending";
        }
        if (user != null && !user.isActive()) {
            return "deactive";
        }
        return "checkmobileexist";
    }

    @Operation(
            summary = "Username and Mobileno from userSession REST API",
            description = "REST API to get the Username and Mobileno from userSession"
    )
    //Getting Username and Mobileno from userSession
    @GetMapping("/getUsername")
    public ResponseEntity<Map<String, Object>> checkUsername() {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("displayName", userSession.getDispayName());
        userDetails.put("mobileNo", userSession.getMobileno());
        userDetails.put("userType", userSession.getUserType());

        return ResponseEntity.ok(userDetails);
    }

    @GetMapping(value = "/getZonehalls")
    public List<Map<String, Object>> getZonehalls() {
        return hallDetailsRepository.getZonesAndDivisions();
    }

    @Operation(
            summary = "ZONES from ERP REST API",
            description = "REST API to get the zones from ERP"
    )
    // Rest Api From ERP for getting Zones
    @GetMapping(value = "/getZoneNo")
    public String getZoneNo() {

        String urlString = appConfig.getErpUrl() + "serviceId=getZones";
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        String bodyMessage = "";
        HttpHeaders headers = new HttpHeaders();
        try {
            HttpEntity<String> postData = new HttpEntity<String>(headers);
            response = restTemplate.exchange(urlString, HttpMethod.POST, postData, String.class);
            bodyMessage = response.getBody();
            //System.out.println("BodyMessage" + bodyMessage);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.print("\n ERROR---:  " + e);
            bodyMessage = "Error";
        }
        return bodyMessage;
    }

    @Operation(
            summary = "DIVISIONS from ERP REST API",
            description = "REST API to get the divisions from ERP"
    )
    // Rest Api From ERP for getting Divisions
    @GetMapping(value = "/getDiv")
    public String getDiv(@RequestParam String zoneId) {
        String urlString = appConfig.getErpUrl() + "serviceId=getWards&Zoneid=" + zoneId + "";
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        String bodyMessage = "";
        HttpHeaders headers = new HttpHeaders();
        try {
            HttpEntity<String> postData = new HttpEntity<String>(headers);
            response = restTemplate.exchange(urlString, HttpMethod.POST, postData, String.class);
            bodyMessage = response.getBody();
            //System.out.println("bodyMessage" + bodyMessage);

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.print("\n ERROR---:  " + e);
            bodyMessage = "Error";
        }
        return bodyMessage;
    }

    @Operation(
            summary = "HAlls By Division ERP REST API",
            description = "REST API to get the HAlls by Divisions"
    )
    //retrieving the Halls By Division
    @GetMapping("/hallsbydivision")
    public List<HallDetails> getHallsByDivision(@RequestParam("division") String division) {
        return hallDetailsRepository.findByDivision(division);
    }

    @Operation(
            summary = "HAlls By zone ERP REST API",
            description = "REST API to get the HAlls by zone"
    )
    @GetMapping("/findhallsbyzone")
    public List<HallDetails> getHallsByzone(@RequestParam("zone") String zone) {
        return hallDetailsRepository.findByZone(zone);
    }

    @Operation(
            summary = "Booking Details By bookingId ERP REST API",
            description = "REST API to get the Booking Details based on BookingId"
    )
    //retrieving the Booking Details based on BookingId
    @GetMapping("/viewAllBookingDetails")
    public ResponseEntity<?> getUserDetailsByBookingId(@RequestParam("bookingId") int bookingId) {

        LocalDateTime rescheduledUpdatedDate = null;

        BookingDetails bookingDetails = bookingService.getBookingsById(bookingId);
        /*if (bookingDetails.getComments().equals("slots Resheduled the Dates")) {
            rescheduledUpdatedDate = LocalDateTime.parse(bookingDetails.getUpdatedDate());
        } else if(bookingDetails.getScheduleCount()==1){
            List<BookingHistory> histories = bookingDetails.getBookingHistories();

            if (histories != null && !histories.isEmpty()) {
                rescheduledUpdatedDate = histories.stream()
                        .filter(history -> "slots Resheduled the Dates".equals(history.getComments()))
                        .map(BookingHistory::getUpdatedDate)
                        .findFirst()
                        .orElse(null);            }

        }*/

        if (bookingDetails != null) {
            return ResponseEntity.ok(bookingDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "New Registration in Counter REST API",
            description = "REST API for New Registration in Counter"
    )
    //New Registration in Counter
    @PostMapping("/newcounterregistrationform")
    public ResponseEntity<?> showRegistrationForm(
            @ModelAttribute Users counterform,
            @RequestParam(value = "userPhoto", required = false) MultipartFile userPhoto) {

        try {
            if (userPhoto != null && !userPhoto.isEmpty()) {
                counterform.setUserPhoto(userPhoto); // Set the photo if it exists
            }

            Users user = userService.user(counterform);
            userSession.setMobileno(user.getMobileNo());
            userSession.setDispayName(user.getName());
            userSession.setUserId(user.getUserId());
            userSession.setUserType(user.getUsertype().toString());

            // Return the userId in the response
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("mobileNo", user.getMobileNo());
            response.put("name", user.getName());
            response.put("usertype", user.getUsertype());
            response.put("message", "Registration successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the exception and return an appropriate error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }


    @Operation(
            summary = "Counter User Details REST API",
            description = "REST API to get the User Details by Mobileno for counter"
    )
    //retrieving the User Details by Mobileno for Counter
    @GetMapping("/getUserDetails")
    public ResponseEntity<?> getUserDetailsByMobileNo(@RequestParam("mobileNo") long mobileNo) {
        try {
            // Call service method to retrieve user details by mobile number
            Users userDetails = userService.getUserDetailsByMobileNo(mobileNo);
            if (userDetails != null) {
                return ResponseEntity.ok(userDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            // Log the exception and return an appropriate error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user details");
        }
    }

    @Operation(
            summary = "DD Image by CounterID REST API",
            description = "REST API to get the DD Image by counterId"
    )
    //retrieving the Dd Image by counterId
    @GetMapping("/getImageByDdId")
    public ResponseEntity<CounterDetails> getImageByDdId(@RequestParam int counterId) {
        CounterDetails counterDetails = counterservice.getDdImageById(counterId);
        if (counterDetails != null) {
            //System.out.println("CounterDetails found: " + counterDetails.getDdphoto());
            return ResponseEntity.ok(counterDetails);
        } else {
            //System.out.println("CounterDetails not found for ID: " + counterId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(
            summary = "Counter Booking Cancellation by Letter and Reason REST API",
            description = "REST API save the counter Booking Cancellation Letter and reason"
    )
    //Saving the counter Booking Cancellation Letter and reason
    @PostMapping("/saveCancellation")
    public ResponseEntity<CounterRefundList> saveCancellation(
            @RequestParam("counterId") int counterId,
            @RequestParam("uploadLetter") MultipartFile uploadLetter,
            @RequestParam("reason") String reason) throws IOException {

        //System.out.println("Received counterId: " + counterId);
        //System.out.println("Received file: " + uploadLetter.getOriginalFilename());

        // Fetch booking details using counterId
        List<BookingDetails> bookingList = bookingDetailsRepository.findByCounterId(counterId);

        if (bookingList.isEmpty()) {
            //System.out.println("No bookings found for counterId: " + counterId);
        }

        for (BookingDetails booking : bookingList) {
            booking.setBookingStatus(BookingStatus.CANCELED);
            bookingDetailsRepository.save(booking);

            List<BookingCalendar> bookingCalendarList = bookingCalendarRepository.getCancellingdata1(String.valueOf(booking.getBookingId()));

            for (BookingCalendar calendar : bookingCalendarList) {
                boolean isUpdated = false;

                if (String.valueOf(booking.getBookingId()).equals(calendar.getBookingId_1())) {
                    calendar.setBookingId_1(null);
                    isUpdated = true;
                }
                if (String.valueOf(booking.getBookingId()).equals(calendar.getBookingId_2())) {
                    calendar.setBookingId_2(null);
                    isUpdated = true;
                }
                if (calendar.isMorningSlot()) {
                    calendar.setMorningSlot(false);
                    isUpdated = true;
                }
                if (calendar.isEveningSlot()) {
                    calendar.setEveningSlot(false);
                    isUpdated = true;
                }
                if (isUpdated) {
                    calendar.setIs_delete(true);
                    bookingCalendarRepository.save(calendar);
                }
            }
        }
        CounterRefundList counterRefundList = counterservice.saveCancellation(counterId, uploadLetter, reason);

        return ResponseEntity.ok(counterRefundList);
    }


    @Operation(
            summary = "Counter Booking Details by CounterId REST API",
            description = "REST API to get the Counter Details by CounterId"
    )
    //retrieving Counter Booking Details by counterId
    @GetMapping("/counterdetails")
    public List<BookingDetails> getUserDetailsbycounterId(@RequestParam("counterId") int counterId) {
        List<BookingDetails> counterRefundList = bookingDetailsRepository.getCounterDetailsbycounterId(counterId);
        return counterRefundList;
    }


//  @GetMapping("/getUser")
//  public ResponseEntity<?> getUserByID(@RequestParam int userId){
//  	if(userId == 0) {
//  		userId=userSession.getUserId();
//  	}
//      return ResponseEntity.ok(userService.getUserById(userId));
//  }

    @Operation(
            summary = "Updating the User Details  Mobileno Validation REST API",
            description = "REST API to Update the User Details if the User status is Rejected"
    )
    //Updating the Rejected user details
    @PostMapping("/updateUserDetails")
    public String updateUserDetails(
            @RequestParam("userId") int userId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "doorNo", required = false) String doorNo,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "usertype", required = false) String userType,
            @RequestParam(value = "pinCode", required = false) int pinCode,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "profilephoto", required = false) MultipartFile profilephoto) {

        Users userDetails = new Users();
        userDetails.setUserId(userId);
        userDetails.setName(name);
        userDetails.setDoorNo(doorNo);
        userDetails.setArea(area);
        userDetails.setLocation(location);
        userDetails.setUserstatus(userStatus.PENDING);
        userDetails.setPinCode(pinCode);
        userDetails.setEmail(email);
        if (userType != null) {
            userDetails.setUsertype(Users.userType.valueOf(userType));
        }

        // Handle profile photo
        if (profilephoto != null && !profilephoto.isEmpty()) {
            //System.out.println("Profile photo uploaded: " + profilephoto.getOriginalFilename());
            String fileName = userId + "_" + profilephoto.getOriginalFilename();
            String filePath = "/images/userPhoto/" + fileName;
            File file = new File(uploadDirectory + "/userPhoto/" + fileName);

            try {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(profilephoto.getBytes());
                }

                userDetails.setProfilephoto(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "failed";
            }
        } else {
            // If no new profile photo is uploaded, retain the existing one
            Users existingUser = usersRepository.findById(userId).orElse(null);
            if (existingUser != null) {
                userDetails.setProfilephoto(existingUser.getProfilephoto());
            }
        }

        return userServices.updateUserDetails(userDetails);

    }

    @Operation(
            summary = "Counter User Details REST API",
            description = "REST API to Get the User Details for Counter"
    )
    //Retrive user details for counter
    @GetMapping("/getuserforcounter")
    public ResponseEntity<?> getCounterUserById(@RequestParam int userId) {
        if (userId == 0) {
            userId = userSession.getUserId();
            // System.out.println("User ID retrieved from session: " + userId);
        } else {
            //System.out.println("User ID provided as parameter: " + userId);
        }

        Users user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "User Details REST API",
            description = "REST API to Get the User Details"
    )
    //retrieving User Details
    @GetMapping("/getUser")
    public ResponseEntity<?> getUserByID(@RequestParam int userId) {
        if (userId == 0) {
            userId = userSession.getUserId();
        }
        Users user = userService.getUserById(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("mobileNo", user.getMobileNo());
        response.put("doorNo", user.getDoorNo());
        response.put("area", user.getArea());
        response.put("location", user.getLocation());
        response.put("pinCode", user.getPinCode());
        response.put("userstatus", user.getUserstatus());
        response.put("profilephoto", user.getProfilephoto());
        response.put("usertype", user.getUsertype());

        if ("REJECTED".equalsIgnoreCase(user.getUserstatus().name())) {
            List<UserHistory> histories = user.getUserHistories();
            String rejectionReason = histories.stream()
                    .filter(history -> history.getComments() != null)
                    .map(UserHistory::getComments)
                    .findFirst()
                    .orElse("No reason provided");
            response.put("rejectionReason", rejectionReason);
        }

        //System.out.println("Response: " + response); // Log the response
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Saving the Booking Details REST API",
            description = "REST API to Save the Booking Details"
    )
    //APi for Saving the Booking Details
    @PostMapping(path = "/saveTheRequest2", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveTheBookingDetails2(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, Object> bookingDetails = splitFormData(multipartRequest);
        Map<String, Object> saveBookingDetails = bookingService.saveBookingDetails(bookingDetails);
        return ResponseEntity.ok(saveBookingDetails);
    }

    public Map<String, Object> splitFormData(MultipartHttpServletRequest multipartRequest) {
        Map<String, Object> formData = new HashMap<>();

        // Iterate through the request parameters
        Enumeration<String> parameterNames = multipartRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValues = multipartRequest.getParameter(paramName);
            formData.put(paramName, paramValues);
        }

        // Iterate through the request parts (files)
        try {
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile file = entry.getValue();
                if (!file.isEmpty()) {
                    formData.put(entry.getKey(), file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return formData;
    }

    @Operation(
            summary = "Retrieving the Booking Details by BookingId REST API",
            description = "REST API to get the Booking Details by Booking Id"
    )
    //retrieving the Booking Details BY Booking ID
    @GetMapping("/getBooking")
    public ResponseEntity<?> getBookingByID(@RequestParam int bookingId) {
        return ResponseEntity.ok(bookingService.getBookingsById(bookingId));
    }

    @Operation(
            summary = "User Booking Cancelling by BookingId REST API",
            description = "REST API to Cancel the Booking by BookingId by User"
    )
    //Booking Cancelling by BookingId
//Booking Cancelling by BookingId
    @GetMapping("/cancelBookingbyId")
    public ResponseEntity<?> cancelBooking(@RequestParam int bookingId,
                                           @RequestParam BookingDetails.BookingStatus status,
                                           @RequestParam String comments) {
        log.info("Received request to cancel booking with ID: {}");
        log.info("Status: {}, Comments: {}");
        Optional<BookingDetails> bookings = bookingDetailsRepository.findById(bookingId);
        BookingDetails bookingDetails = bookings.get();
        try {
            BookingDetails cancelledBooking = bookingService.updateCancelledBookingStatus(bookingId, status, comments);

            String cancelledbookingsms = "Your booking ID " + bookingId + " on " + bookingDetails.getStartDate() + " has been cancelled successfully. The deposit amount will be refunded to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
            String cancelledurlString = "https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to=" + bookingDetails.getUsers().getMobileNo() + "&from=GCCCRP&body=" + cancelledbookingsms + "&entityid=1401572690000011081&templateid=1407172346829376988";
            String cancelledwhatsupmsg = "https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to=" + bookingDetails.getUsers().getMobileNo() + "&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+booking+with+ID+" + bookingId + "+on+" + bookingDetails.getStartDate() + "+has+been+cancelled+successfully.+%0AThe+deposit+amount+will+be+refunded+to+your+account+within+3+to+15+days.";
            // whatsappMsgServiceImpl.sendMsg(cancelledwhatsupmsg);
            // whatsappMsgServiceImpl.sendSMS(cancelledurlString);
            Optional<BookingDetails> book = bookingDetailsRepository.findById(bookingDetails.getBookingId());
            BookingDetails booking = book.get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            booking.setUpdatedDate(formattedDate);
            bookingDetailsRepository.save(booking);
            log.info("Booking cancelled successfully");
            return ResponseEntity.ok(cancelledBooking);
        } catch (Exception e) {
            log.error("Error cancelling booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling booking: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Family Pensioner Details from ERP REST API",
            description = "REST API to check the Family Pensioner Details From ERP"
    )
    //checking Family pensioner Details
    @GetMapping("/FamilyPensionerDetails")
    @ResponseBody // Ensure Spring treats the returned object as the response body
    public Map<String, Object> getFamilyPensionerDetails(HttpServletRequest request, @RequestParam("id") Long id) {
        String purpose = "Checking Pensioner";
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getFamPensionerDetailsList&familPensionId="
                + id;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            FamilyPensionerDetailsResponse familyPensionerDetailsResponse = objectMapper.readValue(jsonString,
                    FamilyPensionerDetailsResponse.class);

            if (familyPensionerDetailsResponse != null) {
                List<FamilyPensionerDetails> familyPensionerDetails = familyPensionerDetailsResponse
                        .getFamilyPensionerDetails();

                if (familyPensionerDetails != null && !familyPensionerDetails.isEmpty()) {
                    //Long mobileNo = Long.parseLong(familyPensionerDetailsResponse.getFamilyPensionerDetails().get(0).getMobile_no());

                    Long mobileNo = 8220798937l;
                    //System.out.println("step 1");
                    responseMap.put("mobileNo", mobileNo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMap;
    }

    @Operation(
            summary = "GCC Employee Details ERP REST API",
            description = "REST API to retrieving the GCC Employee Details From ERP"
    )
    @GetMapping("/gccemployee")
    @ResponseBody // Ensure Spring treats the returned object as the response body
    public Map<String, Object> getGccEmployee(HttpServletRequest request, @RequestParam("id") Long id) {
        String purpose = "Checking GccEmployee";
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getEmployeeDetail&EmployeeID="
                + id;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            EmployeeDetailsResponse employeeDetailsResponse = objectMapper.readValue(jsonString,
                    EmployeeDetailsResponse.class);

            if (employeeDetailsResponse != null) {
                List<EmployeeDetails> employeeDetails = employeeDetailsResponse
                        .getEmployeeDetailsResult();

                if (employeeDetails != null && !employeeDetails.isEmpty()) {
                    //Long mobileNo = Long.parseLong(familyPensionerDetailsResponse.getFamilyPensionerDetails().get(0).getMobile_no());

                    //Long mobileNo = 8220798933l;
                    //System.out.println("step 1");
                    responseMap.put("EmployeeDetailsResult", employeeDetails);
                } else {
                    responseMap.put("EmployeeDetailsResult", null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMap;
    }

    @Operation(
            summary = "Pensioner Details ERP REST API",
            description = "REST API to retrieving the Pensioner Details From ERP"
    )
    //checking pensioner Details
    @GetMapping("/PensionerDetails")
    @ResponseBody
    public Map<String, Object> getPensionerDetails(HttpServletRequest request, @RequestParam("id") Long id) {
        String purpose = "Checking Employee";
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://erp.chennaicorporation.gov.in/pgr/external/mobileservice?serviceId=getPensionerDetailsList&pensionerId=" + id;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // Extract the JSON string from the response body
        String jsonString = responseEntity.getBody();

        // Extract the Java object from the response body
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            PensionerDetailsResponse pensionerDetailsResponse = objectMapper.readValue(jsonString, PensionerDetailsResponse.class);

            if (pensionerDetailsResponse != null) {
                List<PensionerDetails> pensionerDetails = pensionerDetailsResponse.getEmployees();

                if (pensionerDetails != null && !pensionerDetails.isEmpty()) {
                    Long mobileNo = Long.parseLong(pensionerDetailsResponse.getEmployees().get(0).getMobile_no());
//              	Long mobileNo = 9874561230l;
                    // System.out.println("step 2");
                    responseMap.put("mobileNo", mobileNo);
                    return responseMap;
                } else {
                    responseMap.put("error", "No pensioner details found for the given ID");
                    return responseMap;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", "An error occurred while processing the request");
        }

        return responseMap;
    }

    @Operation(
            summary = "Saving Counter Details ERP REST API",
            description = "REST API to save the Counter Details"
    )
    //Saving the Counter Details
    @PostMapping(path = "/counter-booking", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveCounterBookingDetails(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, Object> bookingDetails = splitFormData(multipartRequest);
        Map<String, Object> saveCounterBooking = counterService.saveCounterBooking(bookingDetails);
        return ResponseEntity.ok(saveCounterBooking);
    }

    @GetMapping("/checkAvail")
    public List<HallDetails> checkAvail(@RequestParam("fromDate") String fromDate,
                                        @RequestParam("toDate") String toDate,
                                        @RequestParam("starting") String starting,
                                        @RequestParam("ending") String ending,
                                        @RequestParam(name = "hallId", required = false) String hallId) {
        List<BookingCalendar> data = new ArrayList<>();
        HashSet<Integer> removeHallId = new HashSet<Integer>();
        int hallIdd = 0;
        if (hallId != null && !hallId.equals("")) {
            hallIdd = Integer.parseInt(hallId);
        }
        if (!fromDate.equals(toDate) && (starting.equals(ending) || starting.equals("evening") && ending.equals("morning"))) {
            Map<String, String> mp = incrementAndDecrementAndDaysCount(fromDate, toDate);
            long noOfDays = Long.parseLong(mp.get("NoOfDays").toString());
            if (noOfDays > 2) {
                data = calendarService.getCalendarByDateAndHallIdAndSlot(mp.get("fromDate"), mp.get("toDate"), starting, ending, hallIdd);
                for (int i = 0; i < data.size(); i++) {
                    removeHallId.add(data.get(i).getHallId());
                }
            }
        }

        data = calendarService.getCalendarByDateAndHallIdAndSlot(fromDate, toDate, starting, ending, hallIdd);
        //System.out.println("data size :-"+data.size());

        if (starting != null && ending != null) {
            boolean checkAvail = false;
            int checkHallId = 0;
            for (int i = 0; i < data.size(); i++) {

                if ((starting.equals("evening") || ending.equals("morning")) && fromDate.equals(data.get(i).getBdate().toString()) && toDate.equals(fromDate)) {
                    if (data.get(i).isEveningSlot() == false) {
                        continue;
                    } else if (data.get(i).isMorningSlot() == false) {
                        continue;
                    }
                } else if ((starting.equals(ending) || starting.equals("evening") && ending.equals("morning")) && !toDate.equals(fromDate)) {
                    if (checkHallId == 0 || checkHallId != data.get(i).getHallId()) {
                        checkHallId = data.get(i).getHallId();
                        checkAvail = false;
                    }

                    if (starting.equals("morning") && ending.equals("morning")) {
                        if (fromDate.equals(data.get(i).getBdate().toString()) && data.get(i).isMorningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId()) {

                                continue;
                            }
                        } else if (toDate.equals(data.get(i).getBdate().toString()) && data.get(i).isMorningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId() && checkAvail == false) {
                                checkHallId = data.get(i).getHallId();
                                continue;
                            }
                        }
                    } else if (starting.equals("evening") && ending.equals("morning")) {

                        if (fromDate.equals(data.get(i).getBdate().toString()) && data.get(i).isEveningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId()) {
                                continue;
                            }
                        } else if (toDate.equals(data.get(i).getBdate().toString()) && data.get(i).isMorningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId() && checkAvail == false) {
                                checkHallId = data.get(i).getHallId();
                                continue;
                            }
                        }
                    } else if (starting.equals("evening") && ending.equals("evening")) {
                        if (fromDate.equals(data.get(i).getBdate().toString()) && data.get(i).isEveningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId()) {
                                continue;
                            }
                        } else if (toDate.equals(data.get(i).getBdate().toString()) && data.get(i).isEveningSlot() == false) {
                            if (checkHallId == data.get(i).getHallId() && checkAvail == false) {
                                checkHallId = data.get(i).getHallId();
                                continue;
                            }
                        }
                    } else {
                        if (checkHallId == data.get(i).getHallId()) {
                            checkAvail = true;
                        }
                    }
                }
                //System.out.println(data.get(i).getHallId());
                removeHallId.add(data.get(i).getHallId());
            }
        }
        List<HallDetails> removeHallByid = new ArrayList<>();
        if (hallIdd != 0) {
            if (hallIdd != data.get(0).getHallId()) {
                HallDetails hallDetails = hallDetailService.getHallById(hallIdd);
                removeHallByid.add(hallDetails);
            }

        } else {
            removeHallByid = hallDetailService.removeHallById(removeHallId);
        }
        return removeHallByid;
    }

    Map<String, String> incrementAndDecrementAndDaysCount(String fromdate, String todate) {
        Map mp = new HashMap<>();
        LocalDate increasement = LocalDate.parse(fromdate);
        LocalDate decrement = LocalDate.parse(todate);

        LocalDate outputDateFrom = increasement;
        LocalDate outputDateToday = decrement;

        long daysDifference = increasement.until(decrement).getDays();
        daysDifference = Math.abs(daysDifference);

        if (fromdate != todate) {
            outputDateFrom = increasement.plusDays(1);
            outputDateToday = decrement.minusDays(1);
        }

        mp.put("fromDate", outputDateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mp.put("toDate", outputDateToday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mp.put("NoOfDays", daysDifference + "");
        return mp;
    }

    @GetMapping("/getuserDetailsbymobileno")
    public Users getuserDetailsbymobileno(@RequestParam long mobileNo) {
        return userservice.getUserDetailsByMobileNo(mobileNo);

    }

    //Reschedule
    @PostMapping("/reschedule-dates")
    public ResponseEntity<String> updateBookingDates(
            @RequestParam int bookingId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Optional<String> updateResult = bookingService.updateBookingDatesAndCalendar(bookingId, startDate, endDate);
            if (updateResult.isPresent()) {
                if (updateResult.get().equals("Booking dates updated successfully.")) {
                    return new ResponseEntity<>(updateResult.get(), HttpStatus.OK);
                } else {
                    // Any other message is treated as an error
                    return new ResponseEntity<>(updateResult.get(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
            }
        } catch (SlotOccupiedException e) {
            // Return a bad request status with the exception message
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/trackBooking")
    public List<Map<String, Object>> getTrackingDetails(@RequestParam int bookingId) {
        Optional<BookingDetails> booking = bookingDetailsRepository.findById(bookingId);
        List<Map<String, Object>> trackingDetails = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        if (booking.isPresent()) {
            BookingDetails bookingDetails = booking.get();

            // Payment Success
            if (bookingDetails.getPayments() != null) {
                Map<String, Object> paymentDetail = new HashMap<>();
                paymentDetail.put("status", "PAYMENT_SUCCESS");
                paymentDetail.put("description", "Payment Success");
                paymentDetail.put("dateTime", bookingDetails.getPayments().getCdate()); // Assuming Cdate is the created date
                trackingDetails.add(paymentDetail);
            }

            // Booking Approved
            if (bookingDetails.getBookingStatus() == BookingStatus.APPROVED) {
                Map<String, Object> bookingApprovedDetail = new HashMap<>();
                bookingApprovedDetail.put("status", "APPROVED");
                bookingApprovedDetail.put("description", "Booking Approved");
                bookingApprovedDetail.put("dateTime", bookingDetails.getBookingDate()); // Assuming BookingDate is correct
                trackingDetails.add(bookingApprovedDetail);
            }

            // Event Completed
            LocalDateTime endDate = null;
            try {
                // Try parsing as LocalDateTime first
                try {
                    endDate = LocalDateTime.parse(bookingDetails.getEndDate(), dateTimeFormatter);
                } catch (Exception e) {
                    // If parsing fails, try parsing as LocalDate
                    LocalDate endDateOnly = LocalDate.parse(bookingDetails.getEndDate(), dateFormatter);
                    endDate = endDateOnly.atStartOfDay(); // Convert LocalDate to LocalDateTime at start of the day
                }
            } catch (Exception e) {
                // Handle parsing exception
                e.printStackTrace();
            }

            if (endDate != null && bookingDetails.getBookingStatus() == BookingStatus.APPROVED &&
                    endDate.isBefore(LocalDateTime.now().plusDays(1))) { // Compare endDate with current date
                Map<String, Object> eventCompletedDetail = new HashMap<>();
                eventCompletedDetail.put("status", "EVENT_COMPLETED");
                eventCompletedDetail.put("description", "Event Completed");
                eventCompletedDetail.put("dateTime", bookingDetails.getEndDate());
                trackingDetails.add(eventCompletedDetail);
            }

            // Settlement Raising
            if (bookingDetails.getSettlement() != null && bookingDetails.getRefundStatus() == RefundStatus.WAITING) {
                Map<String, Object> settlementRaisingDetail = new HashMap<>();
                settlementRaisingDetail.put("status", "SETTLEMENT_RAISING");
                settlementRaisingDetail.put("description", "Settlement Raising");
                settlementRaisingDetail.put("dateTime", bookingDetails.getSettlement().getUpdatedDate());
                trackingDetails.add(settlementRaisingDetail);
            }

            // Refund Completed
            if (bookingDetails.getBookingStatus() == BookingStatus.COMPLETED
                    && bookingDetails.getRefundStatus() == BookingDetails.RefundStatus.COMPLETED) {
                Map<String, Object> refundCompletedDetail = new HashMap<>();
                refundCompletedDetail.put("status", "COMPLETED");
                refundCompletedDetail.put("description", "Refund Completed");
                refundCompletedDetail.put("dateTime", bookingDetails.getUpdatedDate());
                trackingDetails.add(refundCompletedDetail);
            }


            if (bookingDetails.getBookingStatus() == BookingStatus.CANCELED) {
                Map<String, Object> bookingCanceledDetail = new HashMap<>();
                bookingCanceledDetail.put("status", "CANCELED");
                bookingCanceledDetail.put("description", "Booking Cancelled");
                bookingCanceledDetail.put("dateTime", bookingDetails.getUpdatedDate()); // Assuming BookingDate is correct
                trackingDetails.add(bookingCanceledDetail);
            }


            if (bookingDetails.getBookingStatus() == BookingStatus.CANCELED
                    && bookingDetails.getRefundStatus() == RefundStatus.WAITING) {
                Map<String, Object> canceledRaisingDetail = new HashMap<>();
                canceledRaisingDetail.put("status", "SETTLEMENT_RAISING");
                canceledRaisingDetail.put("description", "Settlement Raising");
                canceledRaisingDetail.put("dateTime", bookingDetails.getUpdatedDate()); // Assuming BookingDate is correct
                trackingDetails.add(canceledRaisingDetail);
            }

            if (bookingDetails.getBookingStatus() == BookingStatus.CANCELED
                    && bookingDetails.getRefundStatus() == BookingDetails.RefundStatus.COMPLETED) {
                Map<String, Object> cancelRefundCompletedDetail = new HashMap<>();
                cancelRefundCompletedDetail.put("status", "COMPLETED");
                cancelRefundCompletedDetail.put("description", "Refund Completed");
                cancelRefundCompletedDetail.put("dateTime", bookingDetails.getUpdatedDate());
                trackingDetails.add(cancelRefundCompletedDetail);
            }

            if (bookingDetails.getBookingStatus() == BookingStatus.REJECTED) {
                Map<String, Object> bookingRejectedDetail = new HashMap<>();
                bookingRejectedDetail.put("status", "REJECTED");
                bookingRejectedDetail.put("description", "Booking Rejected");
                bookingRejectedDetail.put("dateTime", bookingDetails.getUpdatedDate()); // Assuming BookingDate is correct
                trackingDetails.add(bookingRejectedDetail);
            }


            if (bookingDetails.getBookingStatus() == BookingStatus.REJECTED
                    && bookingDetails.getRefundStatus() == RefundStatus.WAITING) {
                Map<String, Object> rejectedRaisingDetail = new HashMap<>();
                rejectedRaisingDetail.put("status", "SETTLEMENT_RAISING");
                rejectedRaisingDetail.put("description", "Settlement Raising");
                rejectedRaisingDetail.put("dateTime", bookingDetails.getUpdatedDate()); // Assuming BookingDate is correct
                trackingDetails.add(rejectedRaisingDetail);
            }

            if (bookingDetails.getBookingStatus() == BookingStatus.REJECTED
                    && bookingDetails.getRefundStatus() == BookingDetails.RefundStatus.COMPLETED) {
                Map<String, Object> rejectedRefundCompletedDetail = new HashMap<>();
                rejectedRefundCompletedDetail.put("status", "COMPLETED");
                rejectedRefundCompletedDetail.put("description", "Refund Completed");
                rejectedRefundCompletedDetail.put("dateTime", bookingDetails.getUpdatedDate());
                trackingDetails.add(rejectedRefundCompletedDetail);
            }


            if (bookingDetails.getBookingStatus() == BookingStatus.GCC_BLOCKED) {
                Map<String, Object> gccBlockedDetail = new HashMap<>();
                gccBlockedDetail.put("status", "GCC_BLOCKED");
                gccBlockedDetail.put("description", "GCC Blocked");
                gccBlockedDetail.put("dateTime", bookingDetails.getUpdatedDate());
                trackingDetails.add(gccBlockedDetail);
            }
        }
        return trackingDetails;
    }


    @GetMapping("/confirmstatebooking")
    public String Confirmstatebookig(@RequestParam int bookingId) {
        String response = bookingService.confirmStateBooking(bookingId);
        return response;
    }

    @PostMapping("/uploaddocs")
    public ResponseEntity<String> uploadDocs(@RequestParam("nocFile") MultipartFile file,
                                             @RequestParam("bookingssId") int bookingId) {
        try {
            bookingService.uploadNOCFile(file, bookingId);
            return ResponseEntity.ok("File uploaded and saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }
  /*@GetMapping("/calculatehallamount")
  public ResponseEntity<Map<String, Object>> getHallDetailsConcession(
      @RequestParam int numDays,
      @RequestParam(value = "startPeriod", required = false) String startPeriod,
      @RequestParam(value = "endPeriod", required = false) String endPeriod,
      @RequestParam(value = "reasonforBooking", required = false) String reasonforBooking,
      @RequestParam int hallId) {

      Optional<HallDetails> hallDetailsOpt = hallDetailsRepository.findById(hallId);
      
      if (!hallDetailsOpt.isPresent()) {
          return ResponseEntity.status(404).body(null); // Handle not found
      }

      HallDetails hallDetails = hallDetailsOpt.get();
      boolean isSGE = hallDetails.isSGE();
      boolean isCGE = hallDetails.isCGE();

      double fullDayAmount = hallDetails.getFullDayRent();
      double halfDayAmount = hallDetails.getHalfDayRent();
      double depositAmount = hallDetails.getDepositAmount();
      double gstRate = hallDetails.getGst();

      Map<String, Object> response = new HashMap<>();
      
      double amount = calculateAmount(numDays, startPeriod, endPeriod, fullDayAmount, halfDayAmount);
      double totalDepositAmount = depositAmount * numDays;
      double totalAmount;
      double discountAmount = 0.0;
      response.put("reasonforBooking", reasonforBooking);

      if (!isSGE) {
          totalAmount = amount + (amount * gstRate / 100) + totalDepositAmount;
          response.put("discount", 0);
          response.put("amount", amount);
          response.put("totalAmount", totalAmount);
          response.put("depositAmount", totalDepositAmount);
          double gstValue =gstRate * amount / 100.0;
          response.put("gst", gstValue);
          response.put("discountAmount", amount);
      } else {
          if ("SGE".equals(reasonforBooking)) {
        	  double cleaningCharges = hallDetails.getHallDetailsConcession().get(0).getFullDayCleaningcharges();
        	  double pasystem=hallDetails.getHallDetailsConcession().get(0).getFulldayPAsystem();
        	  double ebCharges=hallDetails.getHallDetailsConcession().get(0).getFulldayEBcharges();
        	  double halfcleaningCharges = hallDetails.getHallDetailsConcession().get(0).getHalfDayCleaningcharges();
        	  double halfpasystem=hallDetails.getHallDetailsConcession().get(0).getHalfdayPAsystem();
        	  double halfebCharges=hallDetails.getHallDetailsConcession().get(0).getHalfdayEBcharges();
              double fulladditionalCharges = cleaningCharges+pasystem+ebCharges;
              double halfadditionalCharges = halfcleaningCharges+halfpasystem+halfebCharges;
              double fulldayhallAmount=hallDetails.getHallDetailsConcession().get(0).getFulldayhallamount();
              double halfdayhallAmount=hallDetails.getHallDetailsConcession().get(0).getHalfdayhallamount();
              double additionalCharges=calculateAmount(numDays, startPeriod, endPeriod, fulladditionalCharges, halfadditionalCharges);
              totalAmount = additionalCharges + totalDepositAmount + (additionalCharges * gstRate / 100.0);
              discountAmount = (fulldayhallAmount * hallDetails.getSGEdiscount()) / 100.0;
              response.put("startperiod", startPeriod);
              response.put("endPeriod", endPeriod);
              response.put("fulldayhallAmount", fulldayhallAmount);
              response.put("halfdayhallAmount", halfdayhallAmount);
        	  response.put("discount", hallDetails.getSGEdiscount());
        	  response.put("rentAmount", 0);
              response.put("additionalCharges", additionalCharges);
              response.put("netAmount", additionalCharges);
              response.put("gst", gstRate * additionalCharges / 100);
              response.put("depositAmount", totalDepositAmount);
              response.put("totalAmount", totalAmount);
              response.put("discountAmount", discountAmount);
          } else if ("CGE".equals(reasonforBooking)) {
        	  response.put("discount", hallDetails.getCGEdiscount());
              discountAmount = (amount * hallDetails.getCGEdiscount()) / 100.0;
              totalAmount = discountAmount + totalDepositAmount + (discountAmount * gstRate / 100);
              response.put("amount", amount);
              response.put("totalAmount", totalAmount);
              response.put("depositAmount", totalDepositAmount);
              double gstValue = gstRate * discountAmount / 100;
              response.put("gst", gstValue);
              response.put("discountAmount", discountAmount);
          } else {
              totalAmount = amount + (amount * gstRate / 100) + totalDepositAmount;
              response.put("discount", 0);
              response.put("amount", amount);
              response.put("totalAmount", totalAmount);
              response.put("depositAmount", totalDepositAmount);
              double gstValue =gstRate * amount / 100.0;
              response.put("gst", gstValue);
              response.put("discountAmount", amount);
          }
      }

      return ResponseEntity.ok(response);
  }
  public double calculateAmount(double numDays, String startPeriod, String endPeriod, double fullDayAmount, double halfDayAmount) {
      double amount = 0;

      // If there is only one day
      if (numDays == 1) {
          if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
              amount = fullDayAmount;
          } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
              amount = halfDayAmount;
          } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
              amount = halfDayAmount;
          }else {
        	  amount=numDays;
          }
      } else {
          // For multiple days
          if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
              amount = fullDayAmount * numDays;
          } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
              amount = fullDayAmount * (numDays - 1) + halfDayAmount;
          } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
              amount = fullDayAmount * (numDays - 1) + halfDayAmount;
          } else if ("2".equals(startPeriod) && "1".equals(endPeriod)) {
              amount = fullDayAmount * (numDays - 2) + halfDayAmount * 2;
          }else {
        	  amount=numDays;
          }
      }

      return amount;
  }*/

    @GetMapping("/calculatehallamount")
    public ResponseEntity<Map<String, Object>> getHallDetailsConcession(
            @RequestParam int numDays,
            @RequestParam(value = "startPeriod", required = false) String startPeriod,
            @RequestParam(value = "endPeriod", required = false) String endPeriod,
            @RequestParam(value = "reasonforBooking", required = false) String reasonforBooking,
            @RequestParam int hallId) {

        Optional<HallDetails> hallDetailsOpt = hallDetailsRepository.findById(hallId);

        if (!hallDetailsOpt.isPresent()) {
            return ResponseEntity.status(404).body(null); // Handle not found
        }

        HallDetails hallDetails = hallDetailsOpt.get();
        boolean isSGE = hallDetails.isSGE();
        boolean isCGE = hallDetails.isCGE();

        double fullDayAmount = hallDetails.getFullDayRent();
        double halfDayAmount = hallDetails.getHalfDayRent();
        double depositAmount = hallDetails.getDepositAmount();
        double halfDepositAmount = hallDetails.getHalfDepositAmount();
        double gstRate = hallDetails.getGst();

        Map<String, Object> response = new HashMap<>();

        double amount = calculateAmount(numDays, startPeriod, endPeriod, fullDayAmount, halfDayAmount);
        double totalDepositAmount = calculateDepositAmount(numDays, startPeriod, endPeriod, depositAmount, halfDepositAmount);
        double totalAmount;
        double discountAmount = 0.0;
        response.put("reasonforBooking", reasonforBooking);

        if (!isSGE) {
            totalAmount = amount + (amount * gstRate / 100) + totalDepositAmount;
            response.put("discount", 0);
            response.put("amount", amount);
            response.put("totalAmount", totalAmount);
            response.put("depositAmount", totalDepositAmount);
            double gstValue = gstRate * amount / 100.0;
            response.put("gst", gstValue);
            response.put("discountAmount", amount);
        } else {
            if ("SGE".equals(reasonforBooking)) {
                double cleaningCharges = hallDetails.getHallDetailsConcession().get(0).getFullDayCleaningcharges();
                double pasystem = hallDetails.getHallDetailsConcession().get(0).getFulldayPAsystem();
                double ebCharges = hallDetails.getHallDetailsConcession().get(0).getFulldayEBcharges();
                double halfcleaningCharges = hallDetails.getHallDetailsConcession().get(0).getHalfDayCleaningcharges();
                double halfpasystem = hallDetails.getHallDetailsConcession().get(0).getHalfdayPAsystem();
                double halfebCharges = hallDetails.getHallDetailsConcession().get(0).getHalfdayEBcharges();
                double fulladditionalCharges = cleaningCharges + pasystem + ebCharges;
                double halfadditionalCharges = halfcleaningCharges + halfpasystem + halfebCharges;
                double fulldayhallAmount = hallDetails.getHallDetailsConcession().get(0).getFulldayhallamount();
                double halfdayhallAmount = hallDetails.getHallDetailsConcession().get(0).getHalfdayhallamount();
                double additionalCharges = calculateAmount(numDays, startPeriod, endPeriod, fulladditionalCharges, halfadditionalCharges);
                totalAmount = additionalCharges + totalDepositAmount + (additionalCharges * gstRate / 100.0);
                discountAmount = (fulldayhallAmount * hallDetails.getSGEdiscount()) / 100.0;
                response.put("startperiod", startPeriod);
                response.put("endPeriod", endPeriod);
                response.put("fulldayhallAmount", fulldayhallAmount);
                response.put("halfdayhallAmount", halfdayhallAmount);
                response.put("discount", hallDetails.getSGEdiscount());
                response.put("rentAmount", 0);
                response.put("additionalCharges", additionalCharges);
                response.put("netAmount", additionalCharges);
                response.put("gst", gstRate * additionalCharges / 100);
                response.put("depositAmount", totalDepositAmount);
                response.put("totalAmount", totalAmount);
                response.put("discountAmount", discountAmount);
            } else if ("CGE".equals(reasonforBooking)) {
                double cleaningCharges = hallDetails.getHallDetailsConcession().get(0).getFullDayCleaningcharges();
                double pasystem = hallDetails.getHallDetailsConcession().get(0).getFulldayPAsystem();
                double ebCharges = hallDetails.getHallDetailsConcession().get(0).getFulldayEBcharges();
                double halfcleaningCharges = hallDetails.getHallDetailsConcession().get(0).getHalfDayCleaningcharges();
                double halfpasystem = hallDetails.getHallDetailsConcession().get(0).getHalfdayPAsystem();
                double halfebCharges = hallDetails.getHallDetailsConcession().get(0).getHalfdayEBcharges();
                double fulladditionalCharges = cleaningCharges + pasystem + ebCharges;
                double halfadditionalCharges = halfcleaningCharges + halfpasystem + halfebCharges;
                double additionalCharges = calculateAmount(numDays, startPeriod, endPeriod, fulladditionalCharges, halfadditionalCharges);
                double rentAmount = (amount * hallDetails.getCGEdiscount()) / 100.0;
                double netAmount = rentAmount + additionalCharges;
                double gst = ((additionalCharges + rentAmount) * gstRate / 100.0);
                totalAmount = Math.floor((netAmount + gst + totalDepositAmount) * 100) / 100.0;
                response.put("startperiod", startPeriod);
                response.put("endPeriod", endPeriod);
                response.put("fulldayhallAmount", fullDayAmount);
                response.put("halfdayhallAmount", halfDayAmount);
                response.put("discount", hallDetails.getCGEdiscount());
                response.put("rentAmount", rentAmount);
                response.put("additionalCharges", additionalCharges);
                response.put("netAmount", netAmount);
                response.put("gst", gst);
                response.put("depositAmount", totalDepositAmount);
                response.put("totalAmount", totalAmount);
                response.put("discountAmount", discountAmount);
            } else {
                double gstValue = (gstRate * amount) / 100.0;
                totalAmount = Math.ceil((amount + gstValue + totalDepositAmount) * 100) / 100;
                response.put("discount", 0);
                response.put("amount", amount);
                response.put("totalAmount", totalAmount);
                response.put("depositAmount", totalDepositAmount);
                response.put("gst", gstValue);
                response.put("discountAmount", amount);
            }
        }

        return ResponseEntity.ok(response);
    }

    public double calculateAmount(double numDays, String startPeriod, String endPeriod, double fullDayAmount, double halfDayAmount) {
        double amount = 0;

        // If there is only one day
        if (numDays == 1) {
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                amount = fullDayAmount;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                amount = halfDayAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                amount = halfDayAmount;
            } else {
                amount = numDays;
            }
        } else {
            // For multiple days
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                amount = fullDayAmount * numDays;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                amount = fullDayAmount * (numDays - 1) + halfDayAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                amount = fullDayAmount * (numDays - 1) + halfDayAmount;
            } else if ("2".equals(startPeriod) && "1".equals(endPeriod)) {
                amount = fullDayAmount * (numDays - 2) + halfDayAmount * 2;
            } else {
                amount = numDays;
            }
        }

        return amount;
    }

    @GetMapping("/getTransactionDetails")
    public Map<String, Object> getTransactionDetails(@RequestParam String userId) {
        // Fetch all transaction details for the user
        List<TransactionResponse> transactions = PaymentGatewayServiceImpl.getTransactionResponseByuserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);

        // Fetch all refund details for the user in a single query
        List<RefundResponse> refundDetails = transRefundResRepository.getRefundResponseByuserId(userId);

        response.put("refundDetails", refundDetails);

        System.out.println("Response: " + response);

        return response;
    }


    @GetMapping("/collectionpendingacknowledgementsss")
    public BookingDetails viewCollectionpendingAck(Model model, int bookingId) {
        BookingDetails bookingsDetails = bookingService.getBookingsById(bookingId);
        model.addAttribute("bookingsDetails", bookingsDetails); // Add single BookingDetails object
        model.addAttribute("userName", userSession.getDispayName());
        return bookingsDetails;
    }

    @GetMapping("/checkQueue")
    public ResponseEntity<List<BookingDetails>> checkQueue(
            @RequestParam String bookingDate,
            @RequestParam Long hallId) {
        try {
            List<BookingDetails> bookings = bookingService.getBookingsByDateAndHall(bookingDate, hallId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public double calculateDepositAmount(double numDays, String startPeriod, String endPeriod, double depositAmount, double HalfDepositAmount) {
        double damount = 0;
    // FOR FULL DAY
        // If there is only one day
        if (numDays == 1) {
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = HalfDepositAmount;
            } else {
                damount = numDays;
            }
        } else {
            // For multiple days
            if ("1".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount * numDays;
            } else if ("1".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = depositAmount * (numDays - 1) + HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "2".equals(endPeriod)) {
                damount = depositAmount * (numDays - 1) + HalfDepositAmount;
            } else if ("2".equals(startPeriod) && "1".equals(endPeriod)) {
                damount = depositAmount * (numDays - 2) + depositAmount;
            } else {
                damount = numDays;
            }
        }
        return damount;
    }


}

