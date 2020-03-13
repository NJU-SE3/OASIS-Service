package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Authority;

import java.util.List;

public interface PermissionService {
	List<Authority> findAll();

	void save(Authority authority);

	void deleteAll();
}
