package com.gcc.communityHall.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gcc.communityHall.others.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.component.JwtUtil;
import com.gcc.communityHall.dto.ErrorResponseDto;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.Login;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.LoginRepository;
import com.gcc.communityHall.service.BookingService;
import com.google.zxing.WriterException;
import com.gcc.communityHall.configuration.QRCodeGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

//@RestController
//@RequestMapping("/careTaker/api")
@Tag(
        name = "CRUD REST APIs for CareTaker in CommunityHall",
        description = "CRUD REST APIs in CommunityHall to CREATE, UPDATE, FETCH AND DELETE Booking details"
)
@RestController
@RequestMapping("/victoriahallbooking_uat/careTaker/api")
@AllArgsConstructor
@Validated
public class CareTakerAPIController {

	@Autowired
	private UserSession userSession;

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private LoginRepository loginrepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private BookingDetailsRepository bookingRepository;
	
	@Autowired
	private ApplicationLogsRepository applicationLogsRepository;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AppConfig appConfig;
	
	@Operation(
		    summary = "Retrieving the User Details by bookingId REST API",
		    description = "REST API for Retrieving the User Details by bookingId"
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "HTTP Status OK",
		        content = @Content(
		                mediaType = "application/json",
		                schema = @Schema(implementation = Users.class)
		               
		            )
		        ),
		    @ApiResponse(
		        responseCode = "500",
		        description = "Internal Server Error",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = ErrorResponseDto.class),
		            examples = @ExampleObject(value = "{ \"path\": \"/viewuserDetails\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")
		        )
		    )
		})
		@GetMapping("/viewuserDetails")
		public ResponseEntity<?> getUserDetailsByBookingId(@RequestParam("bookingId") int bookingId) {
		    BookingDetails bookingDetails = bookingService.getBookingsById(bookingId);
		    Users user = bookingDetails.getUsers();
		    return ResponseEntity.ok(bookingDetails);
		}
	
	@Operation(
		    summary = "Saving the Eb Details REST API",
		    description = "REST API for Saving the EB Details"
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "HTTP Status OK",
		        content = @Content(
		            mediaType = "application/json",
		            examples = @ExampleObject(value = "{ \"message\": \"EB details saved successfully\" }")
		        )
		    ),
		    @ApiResponse(
		        responseCode = "500",
		        description = "Internal Server Error",
		        content = @Content(
		            mediaType = "application/json",
		            schema = @Schema(implementation = ErrorResponseDto.class),
		            examples = @ExampleObject(value = "{ \"path\": \"/saveEbDetails\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")
		        )
		    )
		})
		@PostMapping(path = "/saveEbDetails")
		public ResponseEntity<?> saveEbDetails(@RequestParam int bookingId, @RequestParam double startingReading , @RequestParam(required = false) MultipartFile startingReadingimage) {
		   BookingDetails bd=bookingService.saveebdetails(bookingId,startingReading,startingReadingimage);
	        String adminname = (String) session.getAttribute("usertype");
	        Integer loginid = (Integer) session.getAttribute("id");
		    ApplicationLogs logs=new ApplicationLogs();
		    logs.setComments("Action by " + adminname);
		    logs.setLoginId(loginid);
		    logs.setBookingId(bookingId);
		    logs.setLogType("saving EB StartingReading:-"+startingReading);
		    InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getLocalHost();
		        String hostname = InetAddress.getLocalHost().getHostName();
		        logs.setIpaddress(inetAddress.getHostAddress());
		        logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			applicationLogsRepository.save(logs);
		    return ResponseEntity.ok(bookingService.updateBookingDetails(bd));
		}
    

    @Operation(
            summary = "Checking the MobileNo and PIN REST API",
            description = "REST API for Checking the Mobile No and PIN"
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK",
            content = @Content(
                mediaType = "text/plain",
                examples = @ExampleObject(value = "{ \"usertype\" }")
            )
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Expectation Failed",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{ \"error\": \"Invalid mobile number or PIN\" }")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{ \"mobileNo\": \"Mobile number must be 10 digits\" }")
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(value = "{ \"path\": \"/checkLogin\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")
            )
        )
    })
    @GetMapping("/checkLogin")
    public ResponseEntity<?> checkLogin(
            @RequestParam @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits") String mobileNo,
            @RequestParam String pin, HttpSession session) {
        try {

//			System.out.println("careTaker API Controller\nCounter Id: "+userSession.getCounter()+"\nCounter Name: "+userSession.getCounterName());
            // Fetch login details from the repository
            Login login = loginrepo.findByMobileNoAndPin(Long.parseLong(mobileNo), pin);
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
                InetAddress inetAddress = InetAddress.getLocalHost();
                String hostname = InetAddress.getLocalHost().getHostName();
                System.out.println("Hostname: " + hostname);
                ApplicationLogs logs=new ApplicationLogs();
                logs.setComments("login as "+login.getName());
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
                return ResponseEntity.ok(login.getUserType());
            } else {
                // Handle case where login details are incorrect
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Invalid mobile number or PIN");
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


    
    @Operation(
    	    summary = "Process EB Details and Damage REST API",
    	    description = "REST API for saving the EB Details and Damage"
    	)
    	@ApiResponses({
    	    @ApiResponse(
    	        responseCode = "200",
    	        description = "HTTP Status OK",
    	        content = @Content(
    	            mediaType = "text/plain",
    	            examples = @ExampleObject(value = "EB details and damage processed successfully")
    	        )
    	    ),
    	    @ApiResponse(
    	        responseCode = "500",
    	        description = "Internal Server Error",
    	        content = @Content(
    	            mediaType = "application/json",
    	            schema = @Schema(implementation = ErrorResponseDto.class),
    	            examples = @ExampleObject(value = "{ \"path\": \"/processEbDetailsAndDamage\", \"status\": 500, \"error\": \"Internal Server Error\", \"timestamp\": \"2024-06-22T05:47:04.347+00:00\" }")
    	        )
    	    )
    	})
    	@PostMapping(path = "/processEbDetailsAndDamage")
    	public ResponseEntity<?> processEbDetailsAndDamage(
    	        @RequestParam("bookingId") int bookingId,
    	        @RequestParam("ebId") int ebId,
    	        @RequestParam("startingReading") double startingReading,
    	        @RequestParam("endingReading") double endingReading,
    	        @RequestParam("usedUnit") float usedUnit,
    	        @RequestParam("ebAmount") double ebAmount,
    	        @RequestParam(name ="assetName",required = false) String assetNames[],
    	        @RequestParam(name ="comments",required = false) String comments[],
    	        @RequestParam(name ="quantity",required = false) int quantities[],
    	        @RequestParam(name ="assetAmount",required = false) Double assetAmounts[],
    	        @RequestParam(name ="assetImage",required = false) MultipartFile assetImages[]) {

    	    String processEbDetailsAndDamage = bookingService.processEbDetailsAndDamage(bookingId, ebId, startingReading, endingReading, usedUnit, ebAmount, assetNames, comments, quantities, assetAmounts, assetImages);
	        String adminname = (String) session.getAttribute("usertype");
	        Integer loginid = (Integer) session.getAttribute("id");
		    ApplicationLogs logs=new ApplicationLogs();
		    logs.setComments("Action by " + adminname);
		    logs.setLoginId(loginid);
		    logs.setBookingId(bookingId);
		    logs.setLogType("Saving the SettlementDetails");
		    InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getLocalHost();
		        String hostname = InetAddress.getLocalHost().getHostName();
		        logs.setIpaddress(inetAddress.getHostAddress());
		        logs.setSystemName(hostname);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			applicationLogsRepository.save(logs);
    	    return ResponseEntity.ok(processEbDetailsAndDamage);
    	}

    @GetMapping("/settlementlist")
    public List<BookingDetails> getApprovedBookingsWithSettlement(int hallId) {
        return bookingRepository.getSettlementList(hallId);
    }
    
    @GetMapping(value = "/checkofficialMobileExits")
    public String checkMobileExits(HttpServletRequest request, @RequestParam String phone) {
    	//System.out.println("correction in pin"+phone);
    	Login mod= loginrepo.checkUserbyMobile(Long.parseLong(phone));
    	//System.out.println(mod);
    	if(mod!=null){
    		return "success";
    	}
    	else {
    		return "error";
    	}
    }
    
    @GetMapping(value = "/updatePIN")
    public String updatePIN(long phone, String pin) {
    	Login mod= loginrepo.checkUserbyMobile(phone);
    	mod.setPassword(pin);
    	loginrepo.save(mod);
		return "Success";    	
    }
    @GetMapping(value = "/resetPIN")
    public String resetPIN(@RequestParam long resetpinmobilenumber, @RequestParam String oldpin, @RequestParam String newpin) {
        Login mod = loginrepo.checkUserbyMobile(resetpinmobilenumber);
        
        if (mod != null && mod.getPassword().equals(oldpin)) {
            mod.setPassword(newpin);
            loginrepo.save(mod);
            return "Success";
        } else {
            return "error";
        }
    }
    
    @GetMapping("/past-booking")
    public ResponseEntity<List<BookingDetails>> viewPast() {
        List<BookingDetails> pastBookings = bookingService.getPastsEvents();
        return new ResponseEntity<>(pastBookings, HttpStatus.OK);
    }
    
    @GetMapping("/getPresentBooking")
    public List<BookingDetails> getpresentBooking() {
        return bookingService.getPresentsEvents();
    }
    
    @GetMapping("/getFutureBooking")
    public List<BookingDetails> getfutureBooking() {
        return bookingService.getFuturesEvents();
    }
    
    @GetMapping("/getsettlementList")
    public List<BookingDetails> getSettlementApprovedList() {
        return bookingService.getSettlementApprovedList();
    }
	
	 @GetMapping("/settlement-list")
    public ResponseEntity<List<BookingDetails>> getSettlementList() {
        List<BookingDetails> settlementList = bookingRepository.getSettlementsList();
        
        if (settlementList != null && !settlementList.isEmpty()) {
            return ResponseEntity.ok(settlementList);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    
    @GetMapping("/getBookingDetails")
    public ResponseEntity<BookingDetails> getBookingsById(@RequestParam("bookingId") int bookingId) {
        BookingDetails bookingDetails = bookingService.getBookingsById(bookingId);
        if (bookingDetails != null) {
            return ResponseEntity.ok(bookingDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/generateQRCode")
    public ResponseEntity<String> generateQRCodeForUser(@RequestParam("bookingId") Long bookingId) {
        try {
            // Base URL and full URL construction
            String baseUrl = appConfig.getFullUrl(); // Base URL
           // String fullUrl = baseUrl + "/getuserbyid?registerId=" + registerId; // Full URL

            // Log the generated full URL for debugging
            System.out.println("Generated Full URL for QR Code: " + baseUrl);

            // Generate the QR code with the full URL
            String qrCodeImage = QRCodeGenerator.generateQRCode(baseUrl, bookingId);
            String dataUri =  qrCodeImage; // Encode QR code image as Base64

            // Return the QR code image as a Base64 string
            return ResponseEntity.ok(dataUri);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating QR code");
        }
    }

}
