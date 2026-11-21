package com.gcc.communityHall.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gcc.communityHall.entity.BookingDetails;
import com.gcc.communityHall.entity.BookingDocuments;
import com.gcc.communityHall.entity.BookingHistory;
import com.gcc.communityHall.entity.CounterDetails;
import com.gcc.communityHall.entity.CounterRefundList;
import com.gcc.communityHall.entity.DiscountRange;
import com.gcc.communityHall.entity.CounterRefundList.InitialisationStatus;
import com.gcc.communityHall.entity.HallDetails;
import com.gcc.communityHall.entity.ReasonDocumentMaster;
import com.gcc.communityHall.entity.ReasonMaster;
import com.gcc.communityHall.entity.Users;
import com.gcc.communityHall.others.UserSession;
import com.gcc.communityHall.repository.BookingDetailsRepository;
import com.gcc.communityHall.repository.BookingDocumentsRepository;
import com.gcc.communityHall.repository.CounterDetailsRepository;
import com.gcc.communityHall.repository.CounterRefundListRepository;
import com.gcc.communityHall.repository.HallDetailsRepository;
import com.gcc.communityHall.repository.HallImagesRepository;
import com.gcc.communityHall.service.CounterService;
import com.gcc.communityHall.service.DiscountRangeService;
import com.gcc.communityHall.service.HallDetailService;
import com.gcc.communityHall.service.ReasonMasterService;
import com.gcc.communityHall.service.UserService;

@Service
public class CounterServiceImpl implements CounterService {
    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private BookingDocumentsRepository bookingDocumentsRepository;

    @Autowired
    private ReasonMasterService reasonMasterService;

    @Autowired
    private HallDetailService hallDetailService;

    @Autowired
    private CounterRefundListRepository counterRefundListRepository;

    @Autowired
    private DiscountRangeService discountRangeService;


    @Autowired
    private UserService userService;
    @Autowired
    private UserSession userSession;
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.fullpath}")
    private String serverPath;

    @Autowired
    private CounterDetailsRepository counterRepo;

    private final String ddDirectory;
    private final String ddPhotoUrl;
    private final String refundDirectory;
    private final String refundPhotoUrl;

    @Autowired
    CounterServiceImpl(CounterDetailsRepository counterRepo,
                       @Value("${file.dd.upload.directory}") String ddDirectory,
                       @Value("${file.dd.fullpath}") String ddPhotoUrl,
                       @Value("${file.counterrefund.upload.directory}") String refundDirectory,
                       @Value("${file.counterrefund.fullpath}") String refundPhotoUrl) {
        this.counterRepo = counterRepo;
        this.ddDirectory = ddDirectory;
        this.ddPhotoUrl = ddPhotoUrl;
        this.refundDirectory = refundDirectory;
        this.refundPhotoUrl = refundPhotoUrl;
    }

    @Override
    public CounterDetails saveCounterDetails(String bankName, int bankAccNo, int ddNumber, double amountReceived, MultipartFile counterddphoto, int bookingId) {
        BookingDetails bookingDetails = bookingDetailsRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        CounterDetails counterDetails = new CounterDetails();
        counterDetails.setBankName(bankName);
        counterDetails.setBankAccNo(bankAccNo);
        counterDetails.setDdNumber(ddNumber);
        counterDetails.setAmountReceived(amountReceived);

        if (counterddphoto != null && !counterddphoto.isEmpty()) {
            try {
                String fileName = bookingId + "_" + counterddphoto.getOriginalFilename();
                String filePath = ddDirectory + "/" + fileName;
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(counterddphoto.getBytes());
                }
                counterDetails.setDdphoto(ddPhotoUrl + "dd/" + fileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        counterDetails.setBookingDetails(bookingDetails);
        bookingDetails.setCounterDetails(counterDetails); // Ensure bidirectional linkage
        return counterRepo.save(counterDetails);
    }

    @Override
    public CounterDetails getDdImageById(int counterId) {
        Optional<CounterDetails> counterdetails = counterRepo.findById(counterId);

        return counterdetails.orElse(null);
    }


    @Override
    public Map<String, Object> saveCounterBooking(Map<String, Object> formData) {
        Map<String, Object> response = new HashMap<>();
        BookingDetails bookingDetails = new BookingDetails();

        ReasonMaster reasonMaster = reasonMasterService.getReasonById(Integer.parseInt(formData.get("reasonId").toString()));
        HallDetails hallDetails = hallDetailService.getHallById(Integer.parseInt(formData.get("hallId").toString()));
        Users user = userService.getUserById(userSession.getUserId());
        String bookingId = formData.get("bookingId").toString();
        if (bookingId != null && bookingId.equals("0")) {
            bookingDetails = new BookingDetails();
        } else {
            BookingDetails book = getBookingsById(Integer.parseInt(formData.get("bookingId").toString()));
            if (book != null) {
                bookingDetails = book;
                bookingDocumentsRepository.deleteByBookingId(book.getBookingId());
            } else {
                bookingDetails = new BookingDetails();
            }
        }
        if (user != null) {
            bookingDetails.setUsers(user);

            if (hallDetails != null) {
                bookingDetails.setHallDetails(hallDetails);
//                bookingDetails.setDepositAmount(hallDetails.getDepositAmount());
                String startDate = formData.get("startDate").toString();
                String endDate = formData.get("endDate").toString();
                String relation = formData.get("relation").toString();
                double days = Double.parseDouble(formData.get("days").toString());
                int daysrange = Integer.parseInt(formData.get("daysrange").toString());
                Optional<DiscountRange> discountOpt = discountRangeService.getDiscountByUserType(user.getUsertype());
                DiscountRange discountrange = discountOpt.get();
                float discount = discountrange.getDiscountRange();
                int startingSlot = Integer.parseInt(formData.get("startingSlot").toString());
                int endingSlot = Integer.parseInt(formData.get("endingSlot").toString());
                double hallAmount = calcHallAmount(hallDetails, daysrange, startingSlot, endingSlot);
                double discountAmount = 0;
                String usertype = user.getUsertype().toString();
                if (usertype.equals("COUNCILLOR") || usertype.equals("PENSIONER") || usertype.equals("GCC_EMPLOYEE") || usertype.equals("PUBLIC")) {
                    discountAmount = (hallAmount * discount) / 100;
                }
                hallAmount = hallAmount - discountAmount;
                double gstAmount = (hallAmount * hallDetails.getGst()) / 100;
                //double discountAmount=hallAmount*(1-(discount/100));

                double totalAmount = hallAmount + gstAmount + bookingDetails.getDepositAmount();

//                BigDecimal roundedTotalAmount = new BigDecimal(totalAmount).setScale(0, RoundingMode.HALF_UP); // Round to nearest whole number

                // Ensure two decimal places for display
//                String formattedTotalAmount = String.format("%.2f", roundedTotalAmount.doubleValue());

                // Print the result
//                System.out.println("Total Amount: " + formattedTotalAmount);


                bookingDetails.setHallAmount(hallAmount);
                bookingDetails.setGst(gstAmount);
                bookingDetails.setNoOfDays(days);
                bookingDetails.setAmountReceived(totalAmount);
                bookingDetails.setStartingSlot(startingSlot);
                bookingDetails.setEndingSlot(endingSlot);
                bookingDetails.setStartDate(startDate);
                bookingDetails.setEndDate(endDate);
                bookingDetails.setBookingDate(LocalDate.now());
                bookingDetails.setReason(reasonMaster.getReason());
                bookingDetails.setRefundStatus(BookingDetails.RefundStatus.WAITING);
                bookingDetails.setBookingtype(BookingDetails.BookingType.OFFLINE);


                bookingDetails = bookingDetailsRepository.save(bookingDetails);

                response.put("bookingId", bookingDetails.getBookingId());
                response.put("hallName", bookingDetails.getHallDetails().getHallName());
                response.put("noOfDays", bookingDetails.getNoOfDays());
                response.put("startDate", bookingDetails.getStartDate());
                response.put("endDate", bookingDetails.getEndDate());
                response.put("startingSlot", bookingDetails.getStartingSlot());
                response.put("endingSlot", bookingDetails.getEndingSlot());
                response.put("hallAmount", bookingDetails.getHallAmount());
                response.put("gst", bookingDetails.getGst());
                response.put("depositAmount", bookingDetails.getDepositAmount());
                response.put("discount", discountrange.getDiscountRange());
                //response.put("totalAmount",formattedTotalAmount);
                response.put("username", bookingDetails.getUsers().getName());
                response.put("userId", bookingDetails.getUsers().getUserId());
                response.put("mobileNo", bookingDetails.getUsers().getMobileNo());
                response.put("userType", bookingDetails.getUsers().getUsertype());
                response.put("reason", bookingDetails.getReason());

                if (reasonMaster != null) {
                    List<ReasonDocumentMaster> reasonDocMasterList = reasonMaster.getReasonDocumentMasterList();
                    List<BookingDocuments> uploadedDocs = new ArrayList<>();

                    for (ReasonDocumentMaster reasonDocMaster : reasonDocMasterList) {
                        BookingDocuments bookingDocument = new BookingDocuments();
                        bookingDocument.setBookingDetails(bookingDetails);
                        if (reasonDocMaster.isAddTextField()) {
                            bookingDocument.setName(formData.get(reasonDocMaster.getFieldName()).toString());
                            bookingDocument.setBooking_label(reasonDocMaster.getFieldName());
                        }
                        if (reasonDocMaster.isAddDocumentField()) {
                            MultipartFile file = (MultipartFile) formData.get(reasonDocMaster.getDocFieldName());
                            String fileName = bookingDetails.getBookingId() + "_" + file.getOriginalFilename();
                            String filePath = uploadDirectory + "/" + reasonMaster.getReason().toLowerCase() + "/" + fileName;
                            bookingDocument.setBookingDocUrl(serverPath + "images/" + reasonMaster.getReason().toLowerCase() + "/" + fileName);
                            bookingDocument.setDocumentName(file.getOriginalFilename());
                            bookingDocument.setDocumentType(file.getContentType());
                            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                                fos.write(file.getBytes());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            bookingDocumentsRepository.save(bookingDocument);
                            uploadedDocs.add(bookingDocument);
                        }
                    }
                    response.put("uploadedDocs", uploadedDocs);
                }
            }
        }
        return response;
    }

    private double calcHallAmount(HallDetails hallDetails, int days, int startingSlot, int endingSlot) {
        double hallAmount = 0;
        if (days == 1 && startingSlot == 1 && endingSlot == 2) { //full day
            hallAmount = hallDetails.getFullDayRent();

        } else if (days == 1 && (startingSlot == 1 && endingSlot == 1) || (startingSlot == 2 && endingSlot == 2)) { // half day
            hallAmount = hallDetails.getHalfDayRent();
        } else if (days > 1) {
            if (startingSlot == 2 && endingSlot == 1) {
                hallAmount = hallDetails.getHalfDayRent() * 2;
                days = days - 2;
            } else if (startingSlot == 2 || endingSlot == 1) {
                hallAmount = hallDetails.getHalfDayRent();
                days = days - 1;
            }
            for (int i = 0; i < days; i++) {
                hallAmount = hallAmount + hallDetails.getFullDayRent();
            }
        }

        return hallAmount;
    }

    @Override
    public BookingDetails getBookingsById(int BookingId) {
        Optional<BookingDetails> OPBookingDetails = bookingDetailsRepository.findById(BookingId);
        if (OPBookingDetails.isPresent()) {
            return OPBookingDetails.get();
        }
        return null;
    }

    public CounterRefundList saveCancellation(int counterId, MultipartFile letterdocs, String reason) throws IOException {

        CounterRefundList counterRefundList = new CounterRefundList();
        counterRefundList.setInitialisationStatus(CounterRefundList.InitialisationStatus.INITIATED);
        counterRefundList.setReasonforCancelling(reason);


        if (letterdocs != null && !letterdocs.isEmpty()) {

            try {
                String fileName = counterId + "_" + letterdocs.getOriginalFilename();
                String filePath = refundDirectory + "/" + fileName;
                String imageUrl = refundPhotoUrl + "/" + fileName;

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(letterdocs.getBytes());
                    counterRefundList.setLetterdocument(imageUrl);
                }
                counterRefundList.setLetterdocument(refundPhotoUrl + "cr/" + fileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }


        CounterDetails counterDetails = new CounterDetails();
        counterDetails.setCounterId(counterId);
        counterRefundList.setCounterDetails(counterDetails);

        return counterRefundListRepository.save(counterRefundList);

    }
}
