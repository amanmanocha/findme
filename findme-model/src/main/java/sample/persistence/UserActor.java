package sample.persistence;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.pattern.Patterns;
import akka.persistence.UntypedPersistentActor;
import akka.util.Timeout;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import findme.model.PhoneNumber;
import findme.model.User;
import findme.model.repository.UserRespository;

public class UserActor extends UntypedPersistentActor {
	@Override
	public String persistenceId() {
		return "user-id-1";
	}

	private UserRespository userRepository = new UserRespository();

	public int getNumEvents() {
		return userRepository.size();
	}

	@Override
	public void onReceiveRecover(Object msg) {
		System.out.println("recover " + msg);
		if (msg instanceof RegisterUserEvent) {
			userRepository.update((RegisterUserEvent) msg);
		}
		if (msg instanceof SetOldNumberEvent) {
			userRepository.update((SetOldNumberEvent) msg);
		} else {
			unhandled(msg);
		}
	}

	@Override
	public void onReceiveCommand(Object command) {
		System.out.println(command);
		if (command instanceof RegisterUserCommand) {
			registerUser(command);
		}
		if (command instanceof SetOldNumberCommand) {
			setPrivateNumber(command);
		}
		if (command instanceof SearchUserCommand) {
			SearchUserCommand searchUserCommand = (SearchUserCommand) command;
			Optional<User> optional = userRepository.find(
					searchUserCommand.getFirstName(),
					searchUserCommand.getLastName(),
					searchUserCommand.getOldPhoneNumber());
			sender().tell(optional, null);
		}
		if (command instanceof String) {
			String commandStr = (String) command;
			if (commandStr.equals("print"))
				System.out.println(userRepository.getUsers());

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
		SetOldNumberCommand setPrivateNumberCommand = (SetOldNumberCommand) command;

		final SetOldNumberEvent setPrivateNumberEvent = new SetOldNumberEvent(
				setPrivateNumberCommand.getCurrentNumber(),
				setPrivateNumberCommand.getOldNumber(),
				setPrivateNumberCommand.isCurrentNumberPublic());
		persist(setPrivateNumberEvent, new Procedure<SetOldNumberEvent>() {
			public void apply(SetOldNumberEvent evt) throws Exception {
				userRepository.update(evt);
				getContext().system().eventStream().publish(evt);
			}
		});
	}

	public static void main(String... args) throws Exception {
		final ActorSystem system = ActorSystem.create("example");
		final ActorRef userActor = system.actorOf(
				Props.create(UserActor.class), "persistentUserActor-4-java");

		userActor.tell("print", null);

		PhoneNumber currentPhoneNumber = new PhoneNumber("123456");
		userActor.tell(new RegisterUserCommand("Aman", "Manocha",
				currentPhoneNumber), null);

		PhoneNumber oldPhoneNumber = new PhoneNumber("1234567");
		userActor.tell(new SetOldNumberCommand(true, currentPhoneNumber,
				oldPhoneNumber), null);

		userActor.tell("print", null);

		Timeout timeout = new Timeout(Duration.create(5, "minutes"));
		Future<Object> future = Patterns.ask(userActor, new SearchUserCommand(
				"Aman", "", oldPhoneNumber), 10 * 1000);

		Optional<User> result = (Optional<User>) Await.result(future,
				timeout.duration());
		if (result.isPresent()) {
			System.out.println(result.get());
		} else {
			System.out.println("not present");
		}

		system.terminate();
	}

}
