package sample.persistence;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.pattern.Patterns;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;
import akka.util.Timeout;

import com.google.common.collect.Lists;

import findme.model.PhoneNumber;
import findme.model.User;
import findme.model.repository.Users;

public class UserActor extends UntypedPersistentActor {
	@Override
	public String persistenceId() {
		return "user-id-1";
	}

	private Users userRepository = new Users();

	public int getNumEvents() {
		return userRepository.size();
	}

	@Override
	public void onReceiveRecover(Object msg) {
		if (msg instanceof RegisterUserEvent) {
			userRepository.update((RegisterUserEvent) msg);
		} else if (msg instanceof SnapshotOffer) {
			userRepository = (Users) ((SnapshotOffer) msg).snapshot();
		} else {
			unhandled(msg);
		}
	}

	@Override
	public void onReceiveCommand(Object command) {
		if (command instanceof RegisterUserCommand) {
			registerUser(command);
		}
		if (command instanceof SetPrivateNumberCommand) {
			setPrivateNumber(command);
		}
		if (command instanceof String) {
			String commandStr = (String) command;
			if (commandStr.equals("print")) {
				System.out.println(getNumEvents());
			}
			if (commandStr.equals("get")) {
				sender().tell(userRepository.getUsers().get(0), null);
			}
		}
	}

	private void registerUser(Object command) {
		RegisterUserCommand addUserCommand = (RegisterUserCommand) command;
		addUserCommand.validate();

		final RegisterUserEvent addUserEvent = new RegisterUserEvent(
				addUserCommand.getFirstName(), addUserCommand.getLastName(),
				addUserCommand.getPhoneNumber());
		persistAll(Lists.newArrayList(addUserEvent),
				new Procedure<RegisterUserEvent>() {
					public void apply(RegisterUserEvent evt) throws Exception {
						userRepository.update(addUserEvent);
						getContext().system().eventStream().publish(evt);
					}
				});
	}

	private void setPrivateNumber(Object command) {
		SetPrivateNumberCommand setPrivateNumberCommand = (SetPrivateNumberCommand) command;

		final SetPrivateNumberEvent setPrivateNumberEvent = new SetPrivateNumberEvent(
				setPrivateNumberCommand.getCurrentNumber(),
				setPrivateNumberCommand.getOldNumber(),
				setPrivateNumberCommand.isCurrentNumberPublic());
		persist(setPrivateNumberEvent, new Procedure<SetPrivateNumberEvent>() {
			public void apply(SetPrivateNumberEvent evt) throws Exception {
				userRepository.update(evt);
				getContext().system().eventStream().publish(evt);
			}
		});
	}

	public static void main(String... args) throws Exception {
		final ActorSystem system = ActorSystem.create("example");
		final ActorRef persistentActor = system.actorOf(
				Props.create(UserActor.class), "persistentUserActor-4-java");

		persistentActor
				.tell(new RegisterUserCommand("Aman", "Manocha", new PhoneNumber("123456")), null);

		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		Future<Object> future = Patterns.ask(persistentActor, "get", timeout);

		User result = (User) Await.result(future, timeout.duration());
		System.out.println(result);
		Thread.sleep(1000);
		persistentActor.tell("print", null);

		system.terminate();
	}
}
