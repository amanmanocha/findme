package findme.model.repository;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sample.persistence.RegisterUserEvent;
import sample.persistence.SetOldNumberEvent;

import com.google.common.base.Optional;

import findme.model.PhoneNumber;
import findme.model.User;

public class UserRespository implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Set<User> users;

	public UserRespository() {
		users = new java.util.HashSet<>();
	}

	public UserRespository(Set<User> users) {
		this.users = users;
	}

	public UserRespository copy() {
		return new UserRespository(new HashSet<User>(users));
	}

	public int size() {
		return users.size();
	}

	public Set<User> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return users.toString();
	}

	public Optional<User> find(PhoneNumber currentNumber) {
		for (User user : users)
			if (user.getCurrentPhoneNumber().equals(currentNumber))
				return Optional.of(user);

		return Optional.absent();
	}

	public void update(RegisterUserEvent addUserEvent) {
		User user = User.newUser(addUserEvent.getFirstName(),
				addUserEvent.getLastName(), addUserEvent.getPhoneNumber());
		Optional<User> existingUser = find(addUserEvent.getPhoneNumber());
		if (existingUser.isPresent()) {
			System.out.println("Exists!");
			return;
		}
		users.add(user);
		System.out.println(users);
	}

	public void update(SetOldNumberEvent evt) {
		Optional<User> userOptional = find(evt.getCurrentNumber());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			User updatedUser = user.withUpdatedOldNumber(evt.getOldNumber(),
					evt.isCurrentNumberPublic());

			users.remove(user);
			users.add(updatedUser);
			System.out.println("Updated : " + users);
		} else {
			System.out.println("not present");
		}
	}

	public Optional<User> find(String firstName, String lastName,
			PhoneNumber oldPhoneNumber) {
		for (User user : users)
			if (matchesSearchCriteria(firstName, lastName, oldPhoneNumber, user))
				return Optional.of(user);

		return Optional.absent();
	}

	private boolean matchesSearchCriteria(String firstName, String lastName,
			PhoneNumber oldPhoneNumber, User user) {
		if (!user.isCurrentNumberPublic())
			return false;

		return (user.getFirstName().equals(firstName) || user.getLastName()
				.equals(lastName))
				&& user.getOldPhoneNumber().equals(oldPhoneNumber);
	}

}
