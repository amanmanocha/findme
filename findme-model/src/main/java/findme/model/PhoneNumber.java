package findme.model;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	private int number;

	public PhoneNumber(int number) {
		this.number = number;
	}

	public static PhoneNumber newNumber(int number) {
		return new PhoneNumber(number);
	}

}
