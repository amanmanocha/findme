package sample.persistence;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private List<PhoneNumber> phoneNumbers;

	public User(String firstName, String lastName,
			List<PhoneNumber> phoneNumbers) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumbers = phoneNumbers;
	}

}
