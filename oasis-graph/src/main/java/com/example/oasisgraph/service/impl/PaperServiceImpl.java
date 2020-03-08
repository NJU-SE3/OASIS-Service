package com.example.oasisgraph.service.impl;

import com.example.oasisgraph.VO.PaperVO;
import com.example.oasisgraph.nodes.Affiliation;
import com.example.oasisgraph.nodes.Conference;
import com.example.oasisgraph.repository.*;
import com.example.oasisgraph.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl implements PaperService {
	@Autowired
	private PaperRepository paperRepository;

	@Autowired
	private AffiliationRepository affiliationRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private JournalRepository journalRepository;

	@Override
	public void insertNewPaper(PaperVO paperVO) {

	}
}
