package com.gcc.communityHall.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.service.BookingService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/victoriahallbooking_uat/caretaker")
public class CareTakerController {
	 @Autowired
	    private AppConfig appConfig;
	  
	    @Autowired
	    private BookingDetailsRepository bookingDetailsRepo;
	    
	    @Autowired
	    private BookingService bookingService;

	    @Autowired
	    private BookingDetailsRepository bookingRepository;
	    @Autowired
	    private HallDetailsRepository hallDetailsRepository;

	    @GetMapping("/dashboard")
	    public String viewdashboardPage(Model model,HttpSession session) {
	    	// model.addAttribute("session", session);
	    
	    	  String caretakerName = (String) session.getAttribute("usertype");
	    	  if(caretakerName!=null) {
	      	long mobileno = (long) session.getAttribute("mobileno");
	     	model.addAttribute("mobileno", mobileno);
	    	 int hall_id = (int) session.getAttribute("hall_id");
	    	 String hallName = hallDetailsRepository.findHallNameById(hall_id);
	    		List<Map<String, Object>> getmounthcount = bookingDetailsRepo.getCartakermounthcount(hall_id);
	      	  	 List<Map<String, Object>> count = bookingDetailsRepo.getcartakecount(hall_id);
	      		  Map<String, Object> map = count.get(0);
	      		 
	      		  model.addAttribute("mounthcount",getmounthcount);

	      		  //System.out.println( map.get("approved_count")!=null?map.get("approved_count"):1);
	      		  model.addAttribute("APPROVED", map.get("approved_count"));
	      		  model.addAttribute("REJECTED", map.get("rejected_count"));
	      		  model.addAttribute("COMPLETED", map.get("completed_count"));
	      		  model.addAttribute("CANCELED", map.get("CANCELED"));
	      		  model.addAttribute("PAYMENT_FAILURE", map.get("PAYMENT_FAILURE"));
	    		  model.addAttribute("GCC_BLOCKED", map.get("GCC_BLOCKED"));	    	  	    	  
	    	      model.addAttribute("caretakerName", caretakerName);
	    	      model.addAttribute("hallName", hallName);
	        return "caretaker/dashboard";
	    	  }else {
	    		  session.invalidate();
	    		  return "redirect:/victoriahallbooking_uat/login";
	    	  }
	    }
	    @GetMapping("/settlementlist")
	    public String viewSettlementList(Model model,HttpSession session) {
	        String caretakerName = (String) session.getAttribute("usertype");
	        if(caretakerName!=null) {
		      	long mobileno = (long) session.getAttribute("mobileno");
		     	model.addAttribute("mobileno", mobileno);
	        Integer hallid = (Integer) session.getAttribute("hall_id");
	       List<BookingDetails> settlemetList = bookingRepository.getSettlementList(hallid);
	    	 model.addAttribute("caretakerName", caretakerName);
	    	model.addAttribute("settlemetList", settlemetList);
	        return "caretaker/settlementlist";
	        }else {
	        	session.invalidate();
	    		  return "redirect:/victoriahallbooking_uat/login";
	        }
	    }
	    @GetMapping("/present-booking")
	    public String viewPresent(Model model, HttpSession session) {
	        String caretakerName = (String) session.getAttribute("usertype");
	        if(caretakerName!=null) {
		      	long mobileno = (long) session.getAttribute("mobileno");
		     	model.addAttribute("mobileno", mobileno);
	        Integer hallid = (Integer) session.getAttribute("hall_id");
	        List<BookingDetails> bookings = bookingService.getPresentsEventsByUserId(hallid);
	        model.addAttribute("caretakerName", caretakerName);
	        model.addAttribute("hallid", hallid);
	        model.addAttribute("bookings", bookings);

	        return "caretaker/present-booking";
	    }else {
	    	session.invalidate();
  		  return "redirect:/victoriahallbooking_uat/login";
	    }
	    }
	    @GetMapping("/past-booking")
	    public String viewPast(Model model,HttpSession session) {
	    	String caretakerName = (String) session.getAttribute("usertype");
	    	if(caretakerName!=null) {
		      	long mobileno = (long) session.getAttribute("mobileno");
		     	model.addAttribute("mobileno", mobileno);
	        Integer hallid = (Integer) session.getAttribute("hall_id");
	        // Assuming you have a service class to handle business logic
	       List<BookingDetails> pastBookings = bookingService.getPastsEventsByUserId(hallid);

	        model.addAttribute("caretakerName", caretakerName);
	        model.addAttribute("hallid", hallid);
	        model.addAttribute("pastBookings", pastBookings);
	        return "caretaker/past-booking";
	    	}else {
	    		session.invalidate();
	    		  return "redirect:/victoriahallbooking_uat/login";
	    	}
	    }
	    @GetMapping("/future-booking")
	    public String viewFuture(Model model,HttpSession session) {
	    	String caretakerName = (String) session.getAttribute("usertype");
	    	if(caretakerName!=null) {
		      	long mobileno = (long) session.getAttribute("mobileno");
		     	model.addAttribute("mobileno", mobileno);
	        Integer hallid = (Integer) session.getAttribute("hall_id");
	        // Assuming you have a service class to handle business logic
	       List<BookingDetails> futureBookings = bookingService.getFuturesEventsByUserId(hallid);

	        model.addAttribute("caretakerName", caretakerName);
	        model.addAttribute("hallid", hallid);
	        model.addAttribute("futureBookings", futureBookings);
	        return "caretaker/future-booking";
	    }else {
	    	session.invalidate();
  		  return "redirect:/victoriahallbooking_uat/login";
	    }
	    }

	    @GetMapping("/caretakerpage")
	    public String viewcaretakerpage(Model model,HttpSession session,@RequestParam("bookingId") int bookingId) {
	    	String caretakerName = (String) session.getAttribute("usertype");
	    	if(caretakerName!=null) {
		      	long mobileno = (long) session.getAttribute("mobileno");
		     	model.addAttribute("mobileno", mobileno);
	        model.addAttribute("caretakerName", caretakerName);
	        model.addAttribute("title","caretaker");
	        model.addAttribute("baseURL", appConfig.getBaseUrl());
	        BookingDetails booking = bookingService.getBookingsById(bookingId);
	        model.addAttribute("booking",booking);
	        return "caretaker/caretakerpage";
	    	}else {
	    		session.invalidate();
	    		  return "redirect:/victoriahallbooking_uat/login";
	    	}
	    }
	    @GetMapping("/logout")
	    public String viewLogout(Model model,HttpSession session) {
	    	session.invalidate();
	        return "redirect:/victoriahallbooking_uat/login";
	    }

}
