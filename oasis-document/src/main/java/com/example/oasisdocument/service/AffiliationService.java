package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;

import java.math.BigInteger;
import java.util.List;

public interface AffiliationService {
	Affiliation fetchEnById(String id);

	List<Author> fetchAuthorsByAffiliationName(String affName);
}
