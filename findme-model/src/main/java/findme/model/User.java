package findme.model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String firstName;
	private final String lastName;
	private final PhoneNumber currentPhoneNumber;
	private final PhoneNumber oldPhoneNumber;
	private final boolean isCurrentNumberPublic;

	private User(String firstName, String lastName,
			PhoneNumber currentPhoneNumber, PhoneNumber oldPhoneNumber,
			boolean isCurrentNumberPublic) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.currentPhoneNumber = currentPhoneNumber;
		this.oldPhoneNumber = oldPhoneNumber;
		this.isCurrentNumberPublic = isCurrentNumberPublic;
	}

	public static User newUser(String firstName, String lastName,
			PhoneNumber phoneNumber) {
		return new User(firstName, lastName, phoneNumber, PhoneNumber.empty(),
				true);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public PhoneNumber getCurrentPhoneNumber() {
		return currentPhoneNumber;
	}

	public boolean isCurrentNumberPublic() {
		return isCurrentNumberPublic;
	}

	public PhoneNumber getOldPhoneNumber() {
		return oldPhoneNumber;
	}

	public User withUpdatedOldNumber(PhoneNumber oldPhoneNumber, boolean isCurrentNumberPublic) {
		return new User(firstName, lastName, currentPhoneNumber,
				oldPhoneNumber, isCurrentNumberPublic);
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", currentPhoneNumber=" + currentPhoneNumber
				+ ", oldPhoneNumber=" + oldPhoneNumber
				+ ", isCurrentNumberPublic=" + isCurrentNumberPublic + "]";
	}
	
}
