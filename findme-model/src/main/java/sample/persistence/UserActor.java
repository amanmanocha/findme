package sample.persistence;

//#persistent-actor-example
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import findme.model.User;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;

import com.google.common.collect.Lists;


class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<User> users;
    
    public Users() {
    	users = new ArrayList<>();
    }

    public Users(List<User> users) {
        this.users = users;
    }

    public Users copy() {
        return new Users(new ArrayList<User>(users));
    }

    public int size() {
        return users.size();
    }

    @Override
    public String toString() {
        return users.toString();
    }

	public void update(AddUserEvent addUserEvent) {
		users.add(addUserEvent.getUser());
	}
}

public class UserActor extends UntypedPersistentActor {

	@Override
	public String persistenceId() {
		return "user-id-1";
	}

	private Users state = new Users();

	public int getNumEvents() {
		return state.size();
	}

	@Override
	public void onReceiveRecover(Object msg) {
		if (msg instanceof AddUserEvent) {
			state.update((AddUserEvent) msg);
		} else if (msg instanceof SnapshotOffer) {
			state = (Users) ((SnapshotOffer) msg).snapshot();
		} else {
			unhandled(msg);
		}
	}

	@Override
	public void onReceiveCommand(Object command) {
		if (command instanceof RegisterUserCommand) {
			RegisterUserCommand addUserCommand = (RegisterUserCommand) command;
			addUserCommand.validate();
			
			AddUserEvent addUserEvent = new AddUserEvent(addUserCommand.getUser());
			persistAll(Lists.newArrayList(addUserEvent), new Procedure<AddUserEvent>() {
				public void apply(AddUserEvent evt) throws Exception {
					state.update(addUserEvent);
						getContext().system().eventStream().publish(evt);
				}
			});
		}
		if (command instanceof String) {
			String commandStr = (String) command;
			if(commandStr.equals("print")) {
				System.out.println(getNumEvents());
			}
		}
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

