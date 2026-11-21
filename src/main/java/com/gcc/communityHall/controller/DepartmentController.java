package com.gcc.communityHall.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.repository.UsersRepository;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/victoriahallbooking_uat/departments")
public class DepartmentController {
	
	@Autowired
	private UsersRepository usersRepository; 
	
	
    @GetMapping("/pensionerdashboard")
    public String viewpensionerdashboard(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Map<String, Object>> totalcounts=usersRepository.totalPensioneruserCount(); 
    	List<Map<String, Object>> monthcounts=usersRepository.getpensionermonthCount();
    	model.addAttribute("monthcounts",monthcounts);
  		  Map<String, Object> map = totalcounts.get(0);
  		  model.addAttribute("APPROVED", map.get("approved_count"));
  		  model.addAttribute("REJECTED", map.get("rejected_count"));
 		  model.addAttribute("PENDING", map.get("pending_count"));
        return "departments/pensioner/dashboard";
    }
	
    @GetMapping("/pensioner-userpending") 
    public String viewPending(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> pensionerPendingList=usersRepository.getPensionerPendingList();
		model.addAttribute("pensionerPendingList",pensionerPendingList);
        return "departments/pensioner/user-pending";
    }

    @GetMapping("/pensioner-userapproved")
    public String viewUserApproved(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Users> pensionerApprovedList=usersRepository.getPensionerApprovedList();
        model.addAttribute("pensionerApprovedList",pensionerApprovedList);
        return "departments/pensioner/user-approved";
    }

    @GetMapping("/pensioner-userreject")
    public String viewUserReject(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> pensionerRejectedList= usersRepository.getPensionerRejectedList();
        model.addAttribute("pensionerRejectedList",pensionerRejectedList);
        return "departments/pensioner/user-reject";
    }
    @GetMapping("/logout")
    public String viewLogout(Model model,HttpSession session) {
    	session.invalidate();
        return "redirect:/victoriahallbooking_uat/login";
    }
    
    //gccstaff
    @GetMapping("/gccstaffdashboard")
    public String viewgccstaffdashboard(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Map<String, Object>> totalcounts=usersRepository.totalEmployeeuserCount();
    	List<Map<String, Object>> monthcounts=usersRepository.getemployeemonthCount();
    	model.addAttribute("monthcounts",monthcounts);
		  Map<String, Object> map = totalcounts.get(0);
		  model.addAttribute("APPROVED", map.get("approved_count"));
		  model.addAttribute("REJECTED", map.get("rejected_count"));
		  model.addAttribute("PENDING", map.get("pending_count"));
        return "departments/gccstaff/dashboard";
    }
    @GetMapping("/gccstaff-userpending") 
    public String viewgccstaffuserPending(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> gccstaffPendingList=usersRepository.getGccStaffpendingList();
		model.addAttribute("gccstaffPendingList",gccstaffPendingList);
        return "departments/gccstaff/user-pending";
    }
    @GetMapping("/gccstaff-userapproved")
    public String viewGCCstaffApproved(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Users> gccstaffApprovedList=usersRepository.getGccStaffApprovedList();
        model.addAttribute("gccstaffApprovedList",gccstaffApprovedList);
        return "departments/gccstaff/user-approved";
    }

    @GetMapping("/gccstaff-userreject")
    public String viewGCCstaffReject(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> gccstaffRejectedList= usersRepository.getGccStaffRejectedList();
        model.addAttribute("gccstaffRejectedList",gccstaffRejectedList);
        return "departments/gccstaff/user-reject";
    }
    
    //counsellor
    @GetMapping("/counsellordashboard") 
    public String viewcounsellorrdashboard(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Map<String, Object>> totalcounts=usersRepository.totalCounselloruserCount();
    	List<Map<String, Object>> monthcounts=usersRepository.getcounsellormonthCount();
    	model.addAttribute("monthcounts",monthcounts);
		  Map<String, Object> map = totalcounts.get(0);
		  model.addAttribute("APPROVED", map.get("approved_count"));
		  model.addAttribute("REJECTED", map.get("rejected_count"));
		  model.addAttribute("PENDING", map.get("pending_count"));
        return "departments/counsellor/dashboard";
    }
    @GetMapping("/counsellor-userpending") 
    public String viewcounsellorPending(Model model,HttpSession session) {
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> counsellorPendingList=usersRepository.getCounsellorPendingList();
		model.addAttribute("counsellorPendingList",counsellorPendingList);
        return "departments/counsellor/user-pending";
    }
    @GetMapping("/counsellor-userapproved")
    public String viewCounsellorApproved(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
    	List<Users> counsellorApprovedList=usersRepository.getCounsellorApprovedList();
        model.addAttribute("counsellorApprovedList",counsellorApprovedList);
        return "departments/counsellor/user-approved";
    }

    @GetMapping("/counsellor-userreject")
    public String viewCounsellorReject(Model model,HttpSession session){
    	String departmentname = (String) session.getAttribute("usertype");
    	model.addAttribute("departmentname", departmentname);
        List<Users> counsellorRejectedList= usersRepository.getCounsellorRejectedList();
        model.addAttribute("counsellorRejectedList",counsellorRejectedList);
        return "departments/counsellor/user-reject";
    }
}
