package sample.persistence;

import findme.model.User;


public class RegisterUserCommand {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private final User user;
	
	public RegisterUserCommand(User user) {
		this.user = user;
	}

	public void validate() {
	}

	public User getUser() {
		return user;
	}

}
