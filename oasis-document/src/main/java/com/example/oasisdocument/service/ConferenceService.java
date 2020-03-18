package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.extendDoc.Conference;

import java.math.BigInteger;

public interface ConferenceService {
	Conference fetchEnById(String id);
}
