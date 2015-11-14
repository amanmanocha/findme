package sample.persistence;

import java.io.Serializable;

import findme.model.PhoneNumber;

public class SetOldNumberCommand implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final boolean isCurrentNumberPublic;
	private final PhoneNumber currentNumber;
	private final PhoneNumber oldNumber;

	public SetOldNumberCommand(boolean isCurrentNumberPublic,
			PhoneNumber currentNumber, PhoneNumber oldNumber) {
		super();
		this.isCurrentNumberPublic = isCurrentNumberPublic;
		this.currentNumber = currentNumber;
		this.oldNumber = oldNumber;
	}

	public PhoneNumber getCurrentNumber() {
		return currentNumber;
	}

	public boolean isCurrentNumberPublic() {
		return isCurrentNumberPublic;
	}

	public PhoneNumber getOldNumber() {
		return oldNumber;
	}
	
}
