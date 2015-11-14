package findme.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;

import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String firstName;
	private final String lastName;
	private final List<PhoneNumber> phoneNumbers;

	public User(String firstName, String lastName,
			List<PhoneNumber> phoneNumbers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumbers = phoneNumbers;
	}
	
	private void validate() {
		if(firstName == null)
			throw new NullPointerException();
		if(lastName == null)
			throw new NullPointerException();
		if(phoneNumbers == null)
			throw new NullPointerException();
		if(phoneNumbers.isEmpty())
			throw new IllegalArgumentException("Phone number can not be empty");
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

}
