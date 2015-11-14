package com.epam.hackfest.findme;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sample.persistence.SearchUserCommand;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.pattern.Patterns;
import akka.util.Timeout;
import findme.model.PhoneNumber;
import findme.model.User;

@RestController
public class SearchController {

	@RequestMapping("/search")
	public User search(@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "phone", required = true, defaultValue = "") String phone) throws Exception {

		Timeout timeout = new Timeout(Duration.create(5, "minutes"));
		Future<Object> future = Patterns.ask(Application.getActor(),
				new SearchUserCommand("Aman", "Manocha", new PhoneNumber(phone)),
				10 * 1000);

		User result = (User) Await.result(future, timeout.duration());
		System.out.println(result);
 
		return result;
	}
	
}
