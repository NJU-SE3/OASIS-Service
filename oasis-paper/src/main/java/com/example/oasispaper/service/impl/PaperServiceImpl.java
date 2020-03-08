package com.example.oasispaper.service.impl;

import com.example.oasispaper.mapper.QueryMapper;
import com.example.oasispaper.model.*;
import com.example.oasispaper.model.VO.AuthorVO;
import com.example.oasispaper.model.VO.PaperVO;
import com.example.oasispaper.repository.*;
import com.example.oasispaper.service.PaperService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {
	@Autowired
	private QueryMapper queryMapper;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private AffiliationRepository affiliationRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private PaperRepository paperRepository;

	@Autowired
	private AuthorConfRepo authorConfRepo;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	@Cacheable(cacheNames = "paperQuery", unless = "#result==null")
	public List<PaperVO> paperQuery(String key, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PaperVO> papers = queryMapper.queryAll(key);
		return PageInfo.of(papers).getList();
	}

	@Override
	public void insertPaperVO(PaperVO paper) {
		Paper p = paperVO2PO(paper);
		p = paperRepository.save(p);
		//get paper id
		String pdf = paper.getPdfLink();
		List<Conference> conferenceList
				= conferenceRepository.findByName(paper.getConference());
		Conference conference = new Conference();
		if (conferenceList.isEmpty()) {
			conference.setName(paper.getConference());
			conference = conferenceRepository.save(conference);
		} else conference = conferenceList.get(0);

		List<Author> authors = new LinkedList<>();
		paper.getAuthors().forEach((AuthorVO inner) -> {
			String authorName = inner.getName(), affName = inner.getAffiliation();
			List<Author> authorRes = authorRepository.findAllByName(authorName);
			//已经存在这个人了
			if (!authorRes.isEmpty()) {
				authors.add(authorRes.get(0));
				return;
			}
			Author author = new Author();
			author.setName(authorName);
			Affiliation affiliation = new Affiliation();
			affiliation.setName(affName);
			List<Affiliation> affiliationList = affiliationRepository.findByName(affName);
			if (!affiliationList.isEmpty()) {
				affiliation = affiliationList.get(0);
			} else {
				affiliation = affiliationRepository.save(affiliation);  //机构不在数据库,那么新建一个
			}
			Long afId = affiliation.getId();
			author.setAffiliationId(afId);
			author = authorRepository.save(author);
			authors.add(author);
		});
		Long paper_id = p.getId();       //论文id
		Long con_id = conference.getId();//会议id
		//中间表存储
		List<Publication> l = new LinkedList<>();
		for (Author author : authors) {
			Publication ac = new Publication();
			ac.setAuthorId(author.getId());
			ac.setPaperId(paper_id);
			ac.setConferenceId(con_id);
			ac.setStartPage(paper.getStartPage());
			ac.setEndPage(paper.getEndPage());
			ac.setLink(paper.getPdfLink());
			ac.setYear(paper.getYear());
			l.add(ac);
		}
		authorConfRepo.saveAll(l);
	}

	private Paper paperVO2PO(PaperVO paper) {
		Paper p = new Paper();
		p.setTitle(paper.getTitle());
		p.setTerms(paper.getTerms());
		p.setAbstra(paper.getAbstra());
		p.setKeywords(paper.getKeywords());
		p.setCitationCount(paper.getCitationCount());
		p.setReferenceCount(paper.getReferenceCount());
		return p;
	}

}
