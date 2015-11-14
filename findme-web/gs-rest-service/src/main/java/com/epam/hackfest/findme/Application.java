package com.epam.hackfest.findme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import sample.persistence.UserActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		final ActorSystem system = ActorSystem.create("findMe");
		final ActorRef persistentActor = system.actorOf(
				Props.create(UserActor.class), "persistentUserActor-4-java");
	}
	
	public static void getActor() {
		
	}
}
