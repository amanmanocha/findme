package sample.persistence;

import findme.model.PhoneNumber;

public class SearchUserCommand {

	private final String firstName;
	private final String lastName;
	private final PhoneNumber oldPhoneNumber;

	public SearchUserCommand(String firstName, String lastName, PhoneNumber oldPhoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.oldPhoneNumber = oldPhoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public PhoneNumber getOldPhoneNumber() {
		return oldPhoneNumber;
	}

}
