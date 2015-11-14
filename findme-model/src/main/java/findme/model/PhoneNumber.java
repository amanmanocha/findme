package findme.model;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String number;

	public PhoneNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public static PhoneNumber empty() {
		return new PhoneNumber("");
	}
	
}
