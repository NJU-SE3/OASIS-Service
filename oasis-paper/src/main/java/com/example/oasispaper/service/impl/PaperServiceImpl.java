package com.example.oasispaper.service.impl;

import com.example.oasispaper.mapper.QueryMapper;
import com.example.oasispaper.model.*;
import com.example.oasispaper.model.VO.PaperQueryVO;
import com.example.oasispaper.repository.*;
import com.example.oasispaper.service.PaperService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<PaperQueryVO> paperQuery(String key, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> queryMapper.queryAll(key));
    }

    @Override
    public void insertPaperVO(PaperQueryVO paper) {
        Paper p = paperVO2PO(paper);
        p = paperRepository.save(p);
        //get paper id

        List<Conference> conferenceList
                = conferenceRepository.findByName(paper.getConference());
        Conference conference = new Conference();
        if (conferenceList.isEmpty()) {
            conference.setName(paper.getConference());
            conference = conferenceRepository.save(conference);
        } else conference = conferenceList.get(0);

        List<Author> authors = new LinkedList<>();
        paper.getAuthors().forEach((PaperQueryVO.AuthorInner inner) -> {
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
            int afId = affiliation.getId();
            author.setAffiliationId(afId);
            author = authorRepository.save(author);
            authors.add(author);
        });
        int paper_id = p.getId();       //论文id
        int con_id = conference.getId();//会议id
        //中间表存储
        List<AuthorConference> l = new LinkedList<>();
        for (Author author : authors) {
            AuthorConference ac = new AuthorConference();
            ac.setAuthorId(author.getId());
            ac.setPaperId(paper_id);
            ac.setConferenceId(con_id);
            l.add(ac);
        }
        authorConfRepo.saveAll(l);
    }

    private static Paper paperVO2PO(PaperQueryVO paper) {
        Paper p = new Paper();
        p.setTitle(paper.getTitle());
        p.setTerms(paper.getTerms());
        p.setAbstra(paper.getAbstra());
        p.setKeywords(paper.getKeywords());
        return p;
    }
}
