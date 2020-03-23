package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.extendDoc.Conference;

import java.math.BigInteger;
import java.util.List;

public interface ConferenceService {
	Conference fetchEnById(String id);

	List<Conference> fetchConferenceList(int pageNum, int pageSize);

	List<Conference> fetchConferenceList(String refinement);

}
