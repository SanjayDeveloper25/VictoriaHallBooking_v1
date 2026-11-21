package com.gcc.communityHall.service.Impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcc.communityHall.configuration.WebConfig;
import com.gcc.communityHall.entity.ApplicationLogs;
import com.gcc.communityHall.entity.ReasonDocumentMaster;
import com.gcc.communityHall.entity.ReasonMaster;
import com.gcc.communityHall.repository.ApplicationLogsRepository;
import com.gcc.communityHall.repository.ReasonDocMasterRepository;
import com.gcc.communityHall.repository.ReasonMasterRepository;
import com.gcc.communityHall.service.ReasonMasterService;

import jakarta.servlet.http.HttpSession;

@Service
public class ReasonMasterImpl implements ReasonMasterService {

    @Autowired
    ReasonMasterRepository reasonMasterRepository;

    @Autowired
    ReasonDocMasterRepository reasonDocMasterRepository;

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private ApplicationLogsRepository applicationLogsRepository;

    @Autowired
    private HttpSession session;

    @Override
    public String saveTheReason(ReasonMaster reasonMaster) {
        List<ReasonDocumentMaster> reasonDocumentMasterList = reasonMaster.getReasonDocumentMasterList();
        reasonMaster.setReasonDocumentMasterList(null);
        ReasonMaster resMaster = reasonMasterRepository.save(reasonMaster);
        webConfig.addResourceHandlers(webConfig.registryHandler);
        if (reasonDocumentMasterList != null) {
            for (ReasonDocumentMaster reasonDocMaster : reasonDocumentMasterList) {
                reasonDocMaster.setReasonMaster(resMaster);
                reasonDocMasterRepository.save(reasonDocMaster);

            }
        }
        String adminname = (String) session.getAttribute("usertype");
        Integer loginid = (Integer) session.getAttribute("id");
        ApplicationLogs logs = new ApplicationLogs();
        logs.setComments("Action by " + adminname);
        logs.setLoginId(loginid);
        logs.setLogType("Reasons:-" + reasonMaster.getReason());
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
        return "Success";
    }

    @Override
    public ReasonMaster getReasonById(int reasonId) {
        return reasonMasterRepository.findById(reasonId).get();
    }

    @Override
    public List<ReasonMaster> getAllReasons() {
        return reasonMasterRepository.findAll();
    }

    @Override
    public List<ReasonDocumentMaster> getReasonDocumentDetailsByReasonId(int reasonId) {
        return reasonDocMasterRepository.findByReasonMasterReasonId(reasonId);
    }

    @Override
    public ReasonMaster getReasonByName(String reasonName) {
        return reasonMasterRepository.findByReason(reasonName);
    }

    @Override
    public List<ReasonMaster> getAllReasonsWithStatus() {
        return reasonMasterRepository.getAllReasonsWithStatus();

    }
}
