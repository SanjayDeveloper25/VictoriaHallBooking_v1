package com.gcc.communityHall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcc.communityHall.others.UserSession;

	@Controller
	public class Maincontroller {

		@Autowired UserSession userSession;

		@GetMapping("/victoriahallbooking_uat/login")
		public String viewLoginPage(Model model) {
	        String msg="Login to Your Account";
	        model.addAttribute("errortxt",msg);
		        return "officiallogin";
		    }

	}
