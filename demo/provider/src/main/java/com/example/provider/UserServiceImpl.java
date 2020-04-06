package com.example.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.common.User;
import com.example.common.UserService;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Service
@Component
public class UserServiceImpl implements UserService {

	@Override
	public List<User> getUsers() {
		List ans = new LinkedList();
		ans.add(new User());
		ans.add(new User());
		ans.add(new User());
		return ans;
	}
}
