package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.docs.Authority;
import com.example.oasisdocument.repository.docs.AuthorityRepository;
import com.example.oasisdocument.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private AuthorityRepository authorityRepository;


	@Override
	public List<Authority> findAll() {
		return authorityRepository.findAll();
	}

	@Override
	public void save(Authority authority) {
		authorityRepository.save(authority);
	}

	@Override
	public void deleteAll() {
		authorityRepository.deleteAll();
	}
}
