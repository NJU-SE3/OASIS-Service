package com.example.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.example.common.User;
import com.example.common.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@Component
public class UserServiceImpl implements UserService {
	@Reference
	UserService userService;

	@Override
	public List<User> getUsers() {
		return userService.getUsers();
	}
}
