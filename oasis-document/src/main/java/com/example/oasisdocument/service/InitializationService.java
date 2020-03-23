package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Paper;

import java.util.List;

public interface InitializationService {
	void initAffiliationBase();

	void initConferenceBasic();

	void initFieldBasic();

	void initCounterPOJOSummary();

}
