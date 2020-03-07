package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Authority;

import java.util.Collection;
import java.util.List;

public interface PermissionService {
	List<Authority> findAll();

	void save(Authority authority);

	void deleteAll();
}
