package findme.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String firstName;
	private final String lastName;
	private final List<PhoneNumber> phoneNumbers;

	public User(String firstName, String lastName,
			List<PhoneNumber> phoneNumbers) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumbers = phoneNumbers;
	}

}
