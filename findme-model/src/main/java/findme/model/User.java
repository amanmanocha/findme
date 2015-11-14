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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((currentPhoneNumber == null) ? 0 : currentPhoneNumber
						.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (isCurrentNumberPublic ? 1231 : 1237);
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((oldPhoneNumber == null) ? 0 : oldPhoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (currentPhoneNumber == null) {
			if (other.currentPhoneNumber != null)
				return false;
		} else if (!currentPhoneNumber.equals(other.currentPhoneNumber))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (isCurrentNumberPublic != other.isCurrentNumberPublic)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (oldPhoneNumber == null) {
			if (other.oldPhoneNumber != null)
				return false;
		} else if (!oldPhoneNumber.equals(other.oldPhoneNumber))
			return false;
		return true;
	}

}
