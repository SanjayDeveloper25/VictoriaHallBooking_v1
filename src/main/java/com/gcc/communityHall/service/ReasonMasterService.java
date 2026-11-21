package com.gcc.communityHall.service;

import java.util.List;

import com.gcc.communityHall.entity.ReasonDocumentMaster;
import com.gcc.communityHall.entity.ReasonMaster;

public interface ReasonMasterService {

	String saveTheReason(ReasonMaster reasonMaster);

	ReasonMaster getReasonById(int reasonId);

	List<ReasonMaster> getAllReasons();

	List<ReasonDocumentMaster> getReasonDocumentDetailsByReasonId(int reasonId);

	ReasonMaster getReasonByName(String reasonName);

	List<ReasonMaster> getAllReasonsWithStatus();
}
