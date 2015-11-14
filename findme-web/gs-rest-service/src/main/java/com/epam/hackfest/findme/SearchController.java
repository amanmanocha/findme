package com.epam.hackfest.findme;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import findme.model.PhoneNumber;
import findme.model.User;
import sample.persistence.RegisterUserCommand;

@RestController
public class SearchController {

	@RequestMapping("/search")
	public User search(@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "phone", required = true, defaultValue = "") String phone) {
		
		int number = 9899;
		PhoneNumber phoneNumber = new PhoneNumber(number);
		List<PhoneNumber> numbers = Lists.newArrayList(phoneNumber);
		User user = new User("Aman", "Manocha", numbers);
		RegisterUserCommand registerUserCommand = new RegisterUserCommand(user);
		
		return user;
	}
}
