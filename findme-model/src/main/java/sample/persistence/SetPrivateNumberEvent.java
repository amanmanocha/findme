package sample.persistence;

import java.io.Serializable;

import findme.model.PhoneNumber;

public class SetPrivateNumberEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final PhoneNumber currentNumber;
	private final PhoneNumber oldNumber;
	private final boolean currentNumberPublic;
	
	public SetPrivateNumberEvent(PhoneNumber currentNumber,
			PhoneNumber oldNumber, boolean currentNumberPublic) {
		super();
		this.currentNumber = currentNumber;
		this.oldNumber = oldNumber;
		this.currentNumberPublic = currentNumberPublic;
	}

	public PhoneNumber getCurrentNumber() {
		return currentNumber;
	}

	public PhoneNumber getOldNumber() {
		return oldNumber;
	}

	public boolean isCurrentNumberPublic() {
		return currentNumberPublic;
	}

}
