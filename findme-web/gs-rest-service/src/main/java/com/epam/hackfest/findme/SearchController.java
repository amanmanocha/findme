package com.epam.hackfest.findme;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import findme.model.User;

@RestController
public class SearchController {

	@RequestMapping("/search")
	public User search(@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "phone", required = true, defaultValue = "") String phone) {
		
		
		return null;
	}
}
