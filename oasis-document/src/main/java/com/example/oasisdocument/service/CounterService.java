package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;

import java.util.List;

public interface CounterService {
	CounterBaseEntity getSummaryInfo(String id);

	void initCounterPOJOSummary();

	void initCounterPOJO(String id);
}
