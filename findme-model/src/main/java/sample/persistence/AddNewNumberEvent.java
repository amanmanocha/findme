package sample.persistence;

import java.io.Serializable;

import findme.model.User;

public class AddNewNumberEvent implements Serializable {
	private final User user;

	public AddNewNumberEvent(User user) {
		this.user = user;
	}

	private static final long serialVersionUID = 1L;

}
