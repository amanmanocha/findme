package com.epam.hackfest.findme;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

	@RequestMapping("/search")
	public User search(@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "phone", required = true, defaultValue = "") String phone) {
		User user = new User(name);
		user.addPhone(phone);
		user.addPhone("138xxxxx");
		user.addPhone("139xxxxx");
		return user;
	}
}
