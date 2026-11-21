package com.gcc.communityHall.component;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gcc.communityHall.others.AppConfig;
import com.gcc.communityHall.others.UserSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserSessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserSession userSession;
    @Autowired
    private AppConfig appConfig;
    private static final List<String> EXCLUDED_URLS = Arrays.asList("/user", "/main");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the current request URL
        String requestUrl = request.getRequestURI();
        request.setAttribute("baseURL", appConfig.getBaseUrl());


        // Exclude static resources from the check
        if (requestUrl.startsWith("/victoriahallbooking_uat/") || requestUrl.startsWith("/user/") || requestUrl.startsWith("/admin/") || requestUrl.startsWith("/caretaker/") || requestUrl.startsWith("/careTaker/") || requestUrl.startsWith("/css/") || requestUrl.startsWith("/js/") || requestUrl.startsWith("/images/")  || requestUrl.startsWith("/api/") || requestUrl.startsWith("/payment/") || requestUrl.startsWith("/") ) {
            return true; // Allow the request to proceed without checking the login status
        }

        // Exclude the login page from the check
        // Exclude specific URLs from the check
        if (EXCLUDED_URLS.contains(requestUrl)) {
            return true; // Allow the request to proceed without checking the login status
        }
        // Check if the user is logged in
        // If logged in, add the userSession to the model
        // Return true to allow the request to proceed
        //if (isLoggedIn()) {
        if(userSession.getUserId() != null && userSession.getUserType() != null) {
            request.setAttribute("userSession", userSession);
        } else {
            request.setAttribute("title", "Login");
            //request.setAttribute("baseURL", appConfig.getBaseUrl());
            response.sendRedirect("/victoriahallbooking_uat/login");
            //System.out.println("communityhall logout");
            return false; // Stop further processing of the request
        }
        return true; // Allow the request to proceed
    }

    private boolean isLoggedIn() {
        // Implement your login check logic here
        // For example, check if the userSession contains the necessary information
        return userSession.getUserId() != null && userSession.getUserType() != null;
    }
}
