package com.epam.hackfest.findme;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sample.persistence.RegisterUserCommand;
import sample.persistence.SearchUserCommand;
import sample.persistence.SetOldNumberCommand;
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

		String[] parts = name.split(" ");
		String firstName = parts[0];
		String secondName = "";
		if(parts.length > 1)
			secondName = parts[1];
		
		
		Timeout timeout = new Timeout(Duration.create(5, "minutes"));
		Future<Object> future = Patterns.ask(Application.getActor(),
				new SearchUserCommand(firstName, secondName, new PhoneNumber(phone)),
				10 * 1000);

		User result = (User) Await.result(future, timeout.duration());
		System.out.println(result);
 
		return result;
	}

	@RequestMapping("/register")
	public ResultState register(@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "phone", required = true, defaultValue = "") String phone,
			@RequestParam(value = "password", required = true, defaultValue = "") String password) throws Exception {
		String[] parts = name.split(" ");
		String firstName = parts[0];
		String secondName = "";
		if(parts.length > 1)
			secondName = parts[1];
		
		RegisterUserCommand registerUserCommand = new RegisterUserCommand(firstName, secondName, new PhoneNumber(phone));

		Application.getActor().tell(registerUserCommand, null);
 
		return new ResultState(0, "OK");
	}

	@RequestMapping("/update")
	public ResultState addOldNumber(
			@RequestParam(value = "curr", required = true, defaultValue = "") String currentPhone,
			@RequestParam(value = "old", required = true, defaultValue = "") String oldPhone,
			@RequestParam(value = "isPrivate", required = true, defaultValue = "") boolean isCurrentPhonePrivate) throws Exception {
		SetOldNumberCommand setOldNumberCommand = new SetOldNumberCommand(!isCurrentPhonePrivate, new PhoneNumber(currentPhone), new PhoneNumber(oldPhone));
		Application.getActor().tell(setOldNumberCommand, null);
 
		return new ResultState(0, "OK");
	}
}
