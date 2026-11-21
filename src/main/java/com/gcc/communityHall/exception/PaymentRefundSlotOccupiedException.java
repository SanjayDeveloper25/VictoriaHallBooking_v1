package com.gcc.communityHall.exception;

import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.paymentService.service.PaymentGatewayService;
import com.gcc.communityHall.service.ApplicationLogService;
import com.gcc.communityHall.service.BookingService;
import com.gcc.communityHall.service.Impl.BookingServiceImpl;
import com.gcc.communityHall.service.WhatsappMsgService;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentRefundSlotOccupiedException extends RuntimeException {

    // @Autowired
    // BookingService bookingService;

    private String exMsg;

    public PaymentRefundSlotOccupiedException(String exMsg) {
        super(exMsg);
    }
}
