package com.example.oasisdocument.controller;

import com.example.oasisdocument.model.docs.Authority;
import com.example.oasisdocument.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class AuthorityController {
	@Autowired
	private PermissionService PermissionService;

	@GetMapping("/paper")
	public boolean getPaperPermission() {
		return !PermissionService.findAll().isEmpty();
	}

	@PostMapping("/paper")
	public void paperAppend() {
		if (PermissionService.findAll().isEmpty()) {
			Authority authority = new Authority();
			PermissionService.save(authority);
		}
	}

	@DeleteMapping("/paper")
	public void paperRemoveAll() {
		PermissionService.deleteAll();
	}
}
