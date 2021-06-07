package com.thiago.backend.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.repository.UserRepository;
import com.thiago.backend.api.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
//	Injetando a dependência do repository (a interface)
	@Autowired // Poderia também usar o @AllArgsConstructor do lombok (na classe)
	private UserRepository userRepository;

	@Override
	public User findByEmail(String email) {
		
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User createOrUpdate(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public User findById(String id) {
		return this.userRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.userRepository.delete(id);;
	}

	@Override
	public Page<User> findAll(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		
		return userRepository.findAll(pages);
	}

}
