package com.gcc.communityHall.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcc.communityHall.entity.BookingDetails.RefundStatus;
import com.gcc.communityHall.service.Impl.WhatsappMsgServiceImpl;

@RestController
@RequestMapping("/victoriahallbooking_uat/username")
public class apicontroller {
	
	@Autowired
	WhatsappMsgServiceImpl whatsappMsgServiceImpl;
	
	@GetMapping("/systemname")
    public String getUsername() {
		String systemName=System.getProperty("user.name");
		System.out.println("System.name:-"+systemName);
		long mobile = 7989346866L;
		int bookingId= 1;
		double refundAmt = 1000;
		String settlementrefundsms="Your refund amount of Rs. "+refundAmt+" for Booking ID "+bookingId+" has been initiated. The amount will be credited to your account within 3 to 15 days. Thanks for using the GCC Community Hall Booking System.";
		String settlementrefundurlString ="https://tmegov.onex-aura.com/api/sms?key=pfTEYN6H&to="+mobile+"&from=GCCCRP&body="+settlementrefundsms+"&entityid=1401572690000011081&templateid=1407172346804951524";	
		String settlementrefundwhatsupmsg="https://media.smsgupshup.com/GatewayAPI/rest?userid=2000233507&password=h2YjFNcJ&send_to="+mobile+"&v=1.1&format=json&msg_type=TEXT&method=SENDMESSAGE&msg=Your+refund+of+Rs.+"+refundAmt+"+for+Booking+ID+"+bookingId+"+has+been+initiated.+%0AThe+amount+will+be+credited+to+your+account+within+3+to+15+days.";
    	whatsappMsgServiceImpl.sendMsg(settlementrefundwhatsupmsg);
    	whatsappMsgServiceImpl.sendSMS(settlementrefundurlString.trim());
    	System.out.println(settlementrefundurlString);
        return systemName;
    }
	@GetMapping("/systemnamesss")
	public String getUsernamess() throws UnknownHostException {

            // Get the local host (your computer)
            InetAddress inetAddress = InetAddress.getLocalHost();

            // Print the system IP address
            System.out.println("System IP Address: " + inetAddress.getHostAddress());
            try {
                String hostname = java.net.InetAddress.getLocalHost().getHostName();
                System.out.println("Hostname: " + hostname);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

		return inetAddress.getHostAddress();
	}
}