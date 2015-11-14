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

	@Override
	public String toString() {
		return "PhoneNumber [number=" + number + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		PhoneNumber other = (PhoneNumber) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}
	
}
