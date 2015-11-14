package sample.persistence;

import java.io.Serializable;

import findme.model.PhoneNumber;


public class RegisterUserCommand implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String firstName;
	private final String lastName;
	private final PhoneNumber phoneNumber;
	
	public RegisterUserCommand(String firstName, String lastName, PhoneNumber phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public void validate() {
	}

}
