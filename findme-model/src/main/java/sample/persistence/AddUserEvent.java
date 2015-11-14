package sample.persistence;

import java.io.Serializable;

import findme.model.User;

public class AddUserEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private final User user;

	public AddUserEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
