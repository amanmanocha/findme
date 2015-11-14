package sample.persistence;

import java.io.Serializable;

import findme.model.User;

public class AddNewNumberCommand implements Serializable {
	private static final long serialVersionUID = 1L;

	public void validate() {
	}

	public User getUser() {
		throw new UnsupportedOperationException();
	}
	
}
