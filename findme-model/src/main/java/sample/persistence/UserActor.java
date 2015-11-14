package sample.persistence;

//#persistent-actor-example
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;

import com.google.common.collect.Lists;

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
		if (msg instanceof AddUserEvent) {
			userRepository.update((AddUserEvent) msg);
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
		if(command instanceof AddNewNumberCommand) {
			addNewNumber(command);
		}
		if (command instanceof String) {
			String commandStr = (String) command;
			if(commandStr.equals("print")) {
				System.out.println(getNumEvents());
			}
		}
	}

	private void addNewNumber(Object command) {
		AddNewNumberCommand addUserCommand = (AddNewNumberCommand) command;
		addUserCommand.validate();
		
		final AddNewNumberEvent addNumberEvent = new AddNewNumberEvent(addUserCommand.getUser());
		persist(addNumberEvent, new Procedure<AddNewNumberEvent>() {
			public void apply(AddNewNumberEvent evt) throws Exception {
			}
		});
	}

	private void registerUser(Object command) {
		RegisterUserCommand addUserCommand = (RegisterUserCommand) command;
		addUserCommand.validate();
		
		final AddUserEvent addUserEvent = new AddUserEvent(addUserCommand.getUser());
		persistAll(Lists.newArrayList(addUserEvent), new Procedure<AddUserEvent>() {
			public void apply(AddUserEvent evt) throws Exception {
				userRepository.update(addUserEvent);
					getContext().system().eventStream().publish(evt);
			}
		});
	}
	
	public static void main(String... args) throws Exception {
		final ActorSystem system = ActorSystem.create("example");
		final ActorRef persistentActor = system.actorOf(
				Props.create(UserActor.class),
				"persistentUserActor-4-java");

//		persistentActor.tell(new AddUserCommand(new User("Aman", "Manocha", Lists.newArrayList(PhoneNumber.newNumber(1)))), null);

		Thread.sleep(1000);
		persistentActor.tell("print", null);

		system.terminate();
	}
}

