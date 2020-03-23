package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;

import java.util.List;

public interface InitializationService {
	CounterBaseEntity getSummaryInfo(String id);

	void initAffiliationBase();

	void initConferenceBasic();

	void initFieldBasic();

	void initCounterPOJOSummary();

	void initCounterPOJO(String id);
}
